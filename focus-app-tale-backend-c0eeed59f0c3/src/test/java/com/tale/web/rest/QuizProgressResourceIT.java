package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.QuizProgress;
import com.tale.repository.QuizProgressRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuizProgressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizProgressResourceIT {

    private static final Integer DEFAULT_QUIZ_PROGRESS_ID = 1;
    private static final Integer UPDATED_QUIZ_PROGRESS_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final Integer DEFAULT_SURVEY_ID = 1;
    private static final Integer UPDATED_SURVEY_ID = 2;

    private static final Float DEFAULT_PROGRESS = 1F;
    private static final Float UPDATED_PROGRESS = 2F;

    private static final ZonedDateTime DEFAULT_STARTED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_COMPLETED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPLETED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/quiz-progresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizProgressRepository quizProgressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizProgressMockMvc;

    private QuizProgress quizProgress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizProgress createEntity(EntityManager em) {
        QuizProgress quizProgress = new QuizProgress()
            .quizProgressId(DEFAULT_QUIZ_PROGRESS_ID)
            .userId(DEFAULT_USER_ID)
            .surveyId(DEFAULT_SURVEY_ID)
            .progress(DEFAULT_PROGRESS)
            .startedDate(DEFAULT_STARTED_DATE)
            .completedDate(DEFAULT_COMPLETED_DATE);
        return quizProgress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizProgress createUpdatedEntity(EntityManager em) {
        QuizProgress quizProgress = new QuizProgress()
            .quizProgressId(UPDATED_QUIZ_PROGRESS_ID)
            .userId(UPDATED_USER_ID)
            .surveyId(UPDATED_SURVEY_ID)
            .progress(UPDATED_PROGRESS)
            .startedDate(UPDATED_STARTED_DATE)
            .completedDate(UPDATED_COMPLETED_DATE);
        return quizProgress;
    }

    @BeforeEach
    public void initTest() {
        quizProgress = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizProgress() throws Exception {
        int databaseSizeBeforeCreate = quizProgressRepository.findAll().size();
        // Create the QuizProgress
        restQuizProgressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isCreated());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeCreate + 1);
        QuizProgress testQuizProgress = quizProgressList.get(quizProgressList.size() - 1);
        assertThat(testQuizProgress.getQuizProgressId()).isEqualTo(DEFAULT_QUIZ_PROGRESS_ID);
        assertThat(testQuizProgress.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQuizProgress.getSurveyId()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testQuizProgress.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testQuizProgress.getStartedDate()).isEqualTo(DEFAULT_STARTED_DATE);
        assertThat(testQuizProgress.getCompletedDate()).isEqualTo(DEFAULT_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void createQuizProgressWithExistingId() throws Exception {
        // Create the QuizProgress with an existing ID
        quizProgress.setId(1L);

        int databaseSizeBeforeCreate = quizProgressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizProgressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizProgresses() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        // Get all the quizProgressList
        restQuizProgressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizProgress.getId().intValue())))
            .andExpect(jsonPath("$.[*].quizProgressId").value(hasItem(DEFAULT_QUIZ_PROGRESS_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].surveyId").value(hasItem(DEFAULT_SURVEY_ID)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS.doubleValue())))
            .andExpect(jsonPath("$.[*].startedDate").value(hasItem(sameInstant(DEFAULT_STARTED_DATE))))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(sameInstant(DEFAULT_COMPLETED_DATE))));
    }

    @Test
    @Transactional
    void getQuizProgress() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        // Get the quizProgress
        restQuizProgressMockMvc
            .perform(get(ENTITY_API_URL_ID, quizProgress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizProgress.getId().intValue()))
            .andExpect(jsonPath("$.quizProgressId").value(DEFAULT_QUIZ_PROGRESS_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.surveyId").value(DEFAULT_SURVEY_ID))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS.doubleValue()))
            .andExpect(jsonPath("$.startedDate").value(sameInstant(DEFAULT_STARTED_DATE)))
            .andExpect(jsonPath("$.completedDate").value(sameInstant(DEFAULT_COMPLETED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingQuizProgress() throws Exception {
        // Get the quizProgress
        restQuizProgressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizProgress() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();

        // Update the quizProgress
        QuizProgress updatedQuizProgress = quizProgressRepository.findById(quizProgress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizProgress are not directly saved in db
        em.detach(updatedQuizProgress);
        updatedQuizProgress
            .quizProgressId(UPDATED_QUIZ_PROGRESS_ID)
            .userId(UPDATED_USER_ID)
            .surveyId(UPDATED_SURVEY_ID)
            .progress(UPDATED_PROGRESS)
            .startedDate(UPDATED_STARTED_DATE)
            .completedDate(UPDATED_COMPLETED_DATE);

        restQuizProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuizProgress.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuizProgress))
            )
            .andExpect(status().isOk());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
        QuizProgress testQuizProgress = quizProgressList.get(quizProgressList.size() - 1);
        assertThat(testQuizProgress.getQuizProgressId()).isEqualTo(UPDATED_QUIZ_PROGRESS_ID);
        assertThat(testQuizProgress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testQuizProgress.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testQuizProgress.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testQuizProgress.getStartedDate()).isEqualTo(UPDATED_STARTED_DATE);
        assertThat(testQuizProgress.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizProgress.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizProgressWithPatch() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();

        // Update the quizProgress using partial update
        QuizProgress partialUpdatedQuizProgress = new QuizProgress();
        partialUpdatedQuizProgress.setId(quizProgress.getId());

        partialUpdatedQuizProgress
            .quizProgressId(UPDATED_QUIZ_PROGRESS_ID)
            .surveyId(UPDATED_SURVEY_ID)
            .completedDate(UPDATED_COMPLETED_DATE);

        restQuizProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizProgress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizProgress))
            )
            .andExpect(status().isOk());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
        QuizProgress testQuizProgress = quizProgressList.get(quizProgressList.size() - 1);
        assertThat(testQuizProgress.getQuizProgressId()).isEqualTo(UPDATED_QUIZ_PROGRESS_ID);
        assertThat(testQuizProgress.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQuizProgress.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testQuizProgress.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testQuizProgress.getStartedDate()).isEqualTo(DEFAULT_STARTED_DATE);
        assertThat(testQuizProgress.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateQuizProgressWithPatch() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();

        // Update the quizProgress using partial update
        QuizProgress partialUpdatedQuizProgress = new QuizProgress();
        partialUpdatedQuizProgress.setId(quizProgress.getId());

        partialUpdatedQuizProgress
            .quizProgressId(UPDATED_QUIZ_PROGRESS_ID)
            .userId(UPDATED_USER_ID)
            .surveyId(UPDATED_SURVEY_ID)
            .progress(UPDATED_PROGRESS)
            .startedDate(UPDATED_STARTED_DATE)
            .completedDate(UPDATED_COMPLETED_DATE);

        restQuizProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizProgress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizProgress))
            )
            .andExpect(status().isOk());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
        QuizProgress testQuizProgress = quizProgressList.get(quizProgressList.size() - 1);
        assertThat(testQuizProgress.getQuizProgressId()).isEqualTo(UPDATED_QUIZ_PROGRESS_ID);
        assertThat(testQuizProgress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testQuizProgress.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testQuizProgress.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testQuizProgress.getStartedDate()).isEqualTo(UPDATED_STARTED_DATE);
        assertThat(testQuizProgress.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizProgress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizProgress() throws Exception {
        int databaseSizeBeforeUpdate = quizProgressRepository.findAll().size();
        quizProgress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizProgressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizProgress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizProgress in the database
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizProgress() throws Exception {
        // Initialize the database
        quizProgressRepository.saveAndFlush(quizProgress);

        int databaseSizeBeforeDelete = quizProgressRepository.findAll().size();

        // Delete the quizProgress
        restQuizProgressMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizProgress.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizProgress> quizProgressList = quizProgressRepository.findAll();
        assertThat(quizProgressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
