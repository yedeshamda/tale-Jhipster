package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Survey;
import com.tale.repository.SurveyRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Integration tests for the {@link SurveyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
@CrossOrigin(origins = "http://localhost:3000")

class SurveyResourceIT {

    private static final Integer DEFAULT_SURVEY_ID = 1;
    private static final Integer UPDATED_SURVEY_ID = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_CREATED_BY_USER_ID = 1;
    private static final Integer UPDATED_CREATED_BY_USER_ID = 2;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/surveys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyRepository surveyRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveyMockMvc;

    private Survey survey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createEntity(EntityManager em) {
        Survey survey = new Survey()
            .surveyId(DEFAULT_SURVEY_ID)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdByUserId(DEFAULT_CREATED_BY_USER_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .status(DEFAULT_STATUS);
        return survey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createUpdatedEntity(EntityManager em) {
        Survey survey = new Survey()
            .surveyId(UPDATED_SURVEY_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .status(UPDATED_STATUS);
        return survey;
    }

    @BeforeEach
    public void initTest() {
        survey = createEntity(em);
    }

    @Test
    @Transactional
    void createSurvey() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();
        // Create the Survey
        restSurveyMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isCreated());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate + 1);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyId()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testSurvey.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSurvey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSurvey.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSurvey.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSurvey.getCreatedByUserId()).isEqualTo(DEFAULT_CREATED_BY_USER_ID);
        assertThat(testSurvey.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSurvey.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSurveyWithExistingId() throws Exception {
        // Create the Survey with an existing ID
        survey.setId(1L);

        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurveys() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get all the surveyList
        restSurveyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
            .andExpect(jsonPath("$.[*].surveyId").value(hasItem(DEFAULT_SURVEY_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].createdByUserId").value(hasItem(DEFAULT_CREATED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSurveysWithEagerRelationshipsIsEnabled() throws Exception {
        when(surveyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSurveyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(surveyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSurveysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(surveyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSurveyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(surveyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get the survey
        restSurveyMockMvc
            .perform(get(ENTITY_API_URL_ID, survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.surveyId").value(DEFAULT_SURVEY_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.createdByUserId").value(DEFAULT_CREATED_BY_USER_ID))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingSurvey() throws Exception {
        // Get the survey
        restSurveyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurvey are not directly saved in db
        em.detach(updatedSurvey);
        updatedSurvey
            .surveyId(UPDATED_SURVEY_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .status(UPDATED_STATUS);

        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurvey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurvey))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurvey.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSurvey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurvey.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSurvey.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSurvey.getCreatedByUserId()).isEqualTo(UPDATED_CREATED_BY_USER_ID);
        assertThat(testSurvey.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSurvey.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, survey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurveyWithPatch() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey using partial update
        Survey partialUpdatedSurvey = new Survey();
        partialUpdatedSurvey.setId(survey.getId());

        partialUpdatedSurvey
            .surveyId(UPDATED_SURVEY_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID);

        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurvey))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurvey.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSurvey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSurvey.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSurvey.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSurvey.getCreatedByUserId()).isEqualTo(UPDATED_CREATED_BY_USER_ID);
        assertThat(testSurvey.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSurvey.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSurveyWithPatch() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey using partial update
        Survey partialUpdatedSurvey = new Survey();
        partialUpdatedSurvey.setId(survey.getId());

        partialUpdatedSurvey
            .surveyId(UPDATED_SURVEY_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .status(UPDATED_STATUS);

        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurvey))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurvey.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSurvey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurvey.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSurvey.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSurvey.getCreatedByUserId()).isEqualTo(UPDATED_CREATED_BY_USER_ID);
        assertThat(testSurvey.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSurvey.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, survey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
        survey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(survey))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeDelete = surveyRepository.findAll().size();

        // Delete the survey
        restSurveyMockMvc
            .perform(delete(ENTITY_API_URL_ID, survey.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
