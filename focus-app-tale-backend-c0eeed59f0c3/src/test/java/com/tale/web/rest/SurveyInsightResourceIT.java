package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.SurveyInsight;
import com.tale.repository.SurveyInsightRepository;
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
 * Integration tests for the {@link SurveyInsightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurveyInsightResourceIT {

    private static final Integer DEFAULT_INSIGHT_ID = 1;
    private static final Integer UPDATED_INSIGHT_ID = 2;

    private static final String DEFAULT_INSIGHTS = "AAAAAAAAAA";
    private static final String UPDATED_INSIGHTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_SURVEY_ID = 1;
    private static final Integer UPDATED_SURVEY_ID = 2;

    private static final Integer DEFAULT_CREATED_BY_USER_ID = 1;
    private static final Integer UPDATED_CREATED_BY_USER_ID = 2;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/survey-insights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurveyInsightRepository surveyInsightRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveyInsightMockMvc;

    private SurveyInsight surveyInsight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyInsight createEntity(EntityManager em) {
        SurveyInsight surveyInsight = new SurveyInsight()
            .insightId(DEFAULT_INSIGHT_ID)
            .insights(DEFAULT_INSIGHTS)
            .surveyId(DEFAULT_SURVEY_ID)
            .createdByUserId(DEFAULT_CREATED_BY_USER_ID)
            .createdDate(DEFAULT_CREATED_DATE);
        return surveyInsight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyInsight createUpdatedEntity(EntityManager em) {
        SurveyInsight surveyInsight = new SurveyInsight()
            .insightId(UPDATED_INSIGHT_ID)
            .insights(UPDATED_INSIGHTS)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
        return surveyInsight;
    }

    @BeforeEach
    public void initTest() {
        surveyInsight = createEntity(em);
    }

    @Test
    @Transactional
    void createSurveyInsight() throws Exception {
        int databaseSizeBeforeCreate = surveyInsightRepository.findAll().size();
        // Create the SurveyInsight
        restSurveyInsightMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isCreated());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeCreate + 1);
        SurveyInsight testSurveyInsight = surveyInsightList.get(surveyInsightList.size() - 1);
        assertThat(testSurveyInsight.getInsightId()).isEqualTo(DEFAULT_INSIGHT_ID);
        assertThat(testSurveyInsight.getInsights()).isEqualTo(DEFAULT_INSIGHTS);
        assertThat(testSurveyInsight.getSurveyId()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testSurveyInsight.getCreatedByUserId()).isEqualTo(DEFAULT_CREATED_BY_USER_ID);
        assertThat(testSurveyInsight.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createSurveyInsightWithExistingId() throws Exception {
        // Create the SurveyInsight with an existing ID
        surveyInsight.setId(1L);

        int databaseSizeBeforeCreate = surveyInsightRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyInsightMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurveyInsights() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        // Get all the surveyInsightList
        restSurveyInsightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyInsight.getId().intValue())))
            .andExpect(jsonPath("$.[*].insightId").value(hasItem(DEFAULT_INSIGHT_ID)))
            .andExpect(jsonPath("$.[*].insights").value(hasItem(DEFAULT_INSIGHTS)))
            .andExpect(jsonPath("$.[*].surveyId").value(hasItem(DEFAULT_SURVEY_ID)))
            .andExpect(jsonPath("$.[*].createdByUserId").value(hasItem(DEFAULT_CREATED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    void getSurveyInsight() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        // Get the surveyInsight
        restSurveyInsightMockMvc
            .perform(get(ENTITY_API_URL_ID, surveyInsight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surveyInsight.getId().intValue()))
            .andExpect(jsonPath("$.insightId").value(DEFAULT_INSIGHT_ID))
            .andExpect(jsonPath("$.insights").value(DEFAULT_INSIGHTS))
            .andExpect(jsonPath("$.surveyId").value(DEFAULT_SURVEY_ID))
            .andExpect(jsonPath("$.createdByUserId").value(DEFAULT_CREATED_BY_USER_ID))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingSurveyInsight() throws Exception {
        // Get the surveyInsight
        restSurveyInsightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurveyInsight() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();

        // Update the surveyInsight
        SurveyInsight updatedSurveyInsight = surveyInsightRepository.findById(surveyInsight.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurveyInsight are not directly saved in db
        em.detach(updatedSurveyInsight);
        updatedSurveyInsight
            .insightId(UPDATED_INSIGHT_ID)
            .insights(UPDATED_INSIGHTS)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restSurveyInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurveyInsight.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurveyInsight))
            )
            .andExpect(status().isOk());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
        SurveyInsight testSurveyInsight = surveyInsightList.get(surveyInsightList.size() - 1);
        assertThat(testSurveyInsight.getInsightId()).isEqualTo(UPDATED_INSIGHT_ID);
        assertThat(testSurveyInsight.getInsights()).isEqualTo(UPDATED_INSIGHTS);
        assertThat(testSurveyInsight.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurveyInsight.getCreatedByUserId()).isEqualTo(UPDATED_CREATED_BY_USER_ID);
        assertThat(testSurveyInsight.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyInsight.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurveyInsightWithPatch() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();

        // Update the surveyInsight using partial update
        SurveyInsight partialUpdatedSurveyInsight = new SurveyInsight();
        partialUpdatedSurveyInsight.setId(surveyInsight.getId());

        partialUpdatedSurveyInsight.insightId(UPDATED_INSIGHT_ID).surveyId(UPDATED_SURVEY_ID);

        restSurveyInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyInsight.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyInsight))
            )
            .andExpect(status().isOk());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
        SurveyInsight testSurveyInsight = surveyInsightList.get(surveyInsightList.size() - 1);
        assertThat(testSurveyInsight.getInsightId()).isEqualTo(UPDATED_INSIGHT_ID);
        assertThat(testSurveyInsight.getInsights()).isEqualTo(DEFAULT_INSIGHTS);
        assertThat(testSurveyInsight.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurveyInsight.getCreatedByUserId()).isEqualTo(DEFAULT_CREATED_BY_USER_ID);
        assertThat(testSurveyInsight.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSurveyInsightWithPatch() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();

        // Update the surveyInsight using partial update
        SurveyInsight partialUpdatedSurveyInsight = new SurveyInsight();
        partialUpdatedSurveyInsight.setId(surveyInsight.getId());

        partialUpdatedSurveyInsight
            .insightId(UPDATED_INSIGHT_ID)
            .insights(UPDATED_INSIGHTS)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restSurveyInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyInsight.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyInsight))
            )
            .andExpect(status().isOk());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
        SurveyInsight testSurveyInsight = surveyInsightList.get(surveyInsightList.size() - 1);
        assertThat(testSurveyInsight.getInsightId()).isEqualTo(UPDATED_INSIGHT_ID);
        assertThat(testSurveyInsight.getInsights()).isEqualTo(UPDATED_INSIGHTS);
        assertThat(testSurveyInsight.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurveyInsight.getCreatedByUserId()).isEqualTo(UPDATED_CREATED_BY_USER_ID);
        assertThat(testSurveyInsight.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surveyInsight.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurveyInsight() throws Exception {
        int databaseSizeBeforeUpdate = surveyInsightRepository.findAll().size();
        surveyInsight.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyInsightMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyInsight))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyInsight in the database
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurveyInsight() throws Exception {
        // Initialize the database
        surveyInsightRepository.saveAndFlush(surveyInsight);

        int databaseSizeBeforeDelete = surveyInsightRepository.findAll().size();

        // Delete the surveyInsight
        restSurveyInsightMockMvc
            .perform(delete(ENTITY_API_URL_ID, surveyInsight.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SurveyInsight> surveyInsightList = surveyInsightRepository.findAll();
        assertThat(surveyInsightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
