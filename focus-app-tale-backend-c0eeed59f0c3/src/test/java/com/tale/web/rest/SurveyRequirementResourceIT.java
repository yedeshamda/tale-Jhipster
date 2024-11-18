package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.SurveyRequirement;
import com.tale.repository.SurveyRequirementRepository;
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
 * Integration tests for the {@link SurveyRequirementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurveyRequirementResourceIT {

    private static final Integer DEFAULT_REQUIREMENT_ID = 1;
    private static final Integer UPDATED_REQUIREMENT_ID = 2;

    private static final String DEFAULT_REQUIREMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SURVEY_ID = 1;
    private static final Integer UPDATED_SURVEY_ID = 2;

    private static final Integer DEFAULT_CREATED_BY_USER_COMPANY_ID = 1;
    private static final Integer UPDATED_CREATED_BY_USER_COMPANY_ID = 2;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/survey-requirements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurveyRequirementRepository surveyRequirementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveyRequirementMockMvc;

    private SurveyRequirement surveyRequirement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyRequirement createEntity(EntityManager em) {
        SurveyRequirement surveyRequirement = new SurveyRequirement()
            .requirementId(DEFAULT_REQUIREMENT_ID)
            .requirementDescription(DEFAULT_REQUIREMENT_DESCRIPTION)
            .surveyId(DEFAULT_SURVEY_ID)
            .createdByUserCompanyId(DEFAULT_CREATED_BY_USER_COMPANY_ID)
            .createdDate(DEFAULT_CREATED_DATE);
        return surveyRequirement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyRequirement createUpdatedEntity(EntityManager em) {
        SurveyRequirement surveyRequirement = new SurveyRequirement()
            .requirementId(UPDATED_REQUIREMENT_ID)
            .requirementDescription(UPDATED_REQUIREMENT_DESCRIPTION)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserCompanyId(UPDATED_CREATED_BY_USER_COMPANY_ID)
            .createdDate(UPDATED_CREATED_DATE);
        return surveyRequirement;
    }

    @BeforeEach
    public void initTest() {
        surveyRequirement = createEntity(em);
    }

    @Test
    @Transactional
    void createSurveyRequirement() throws Exception {
        int databaseSizeBeforeCreate = surveyRequirementRepository.findAll().size();
        // Create the SurveyRequirement
        restSurveyRequirementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isCreated());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeCreate + 1);
        SurveyRequirement testSurveyRequirement = surveyRequirementList.get(surveyRequirementList.size() - 1);
        assertThat(testSurveyRequirement.getRequirementId()).isEqualTo(DEFAULT_REQUIREMENT_ID);
        assertThat(testSurveyRequirement.getRequirementDescription()).isEqualTo(DEFAULT_REQUIREMENT_DESCRIPTION);
        assertThat(testSurveyRequirement.getSurveyId()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testSurveyRequirement.getCreatedByUserCompanyId()).isEqualTo(DEFAULT_CREATED_BY_USER_COMPANY_ID);
        assertThat(testSurveyRequirement.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createSurveyRequirementWithExistingId() throws Exception {
        // Create the SurveyRequirement with an existing ID
        surveyRequirement.setId(1L);

        int databaseSizeBeforeCreate = surveyRequirementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyRequirementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurveyRequirements() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        // Get all the surveyRequirementList
        restSurveyRequirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].requirementId").value(hasItem(DEFAULT_REQUIREMENT_ID)))
            .andExpect(jsonPath("$.[*].requirementDescription").value(hasItem(DEFAULT_REQUIREMENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].surveyId").value(hasItem(DEFAULT_SURVEY_ID)))
            .andExpect(jsonPath("$.[*].createdByUserCompanyId").value(hasItem(DEFAULT_CREATED_BY_USER_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    void getSurveyRequirement() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        // Get the surveyRequirement
        restSurveyRequirementMockMvc
            .perform(get(ENTITY_API_URL_ID, surveyRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surveyRequirement.getId().intValue()))
            .andExpect(jsonPath("$.requirementId").value(DEFAULT_REQUIREMENT_ID))
            .andExpect(jsonPath("$.requirementDescription").value(DEFAULT_REQUIREMENT_DESCRIPTION))
            .andExpect(jsonPath("$.surveyId").value(DEFAULT_SURVEY_ID))
            .andExpect(jsonPath("$.createdByUserCompanyId").value(DEFAULT_CREATED_BY_USER_COMPANY_ID))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingSurveyRequirement() throws Exception {
        // Get the surveyRequirement
        restSurveyRequirementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurveyRequirement() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();

        // Update the surveyRequirement
        SurveyRequirement updatedSurveyRequirement = surveyRequirementRepository.findById(surveyRequirement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurveyRequirement are not directly saved in db
        em.detach(updatedSurveyRequirement);
        updatedSurveyRequirement
            .requirementId(UPDATED_REQUIREMENT_ID)
            .requirementDescription(UPDATED_REQUIREMENT_DESCRIPTION)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserCompanyId(UPDATED_CREATED_BY_USER_COMPANY_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restSurveyRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurveyRequirement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurveyRequirement))
            )
            .andExpect(status().isOk());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
        SurveyRequirement testSurveyRequirement = surveyRequirementList.get(surveyRequirementList.size() - 1);
        assertThat(testSurveyRequirement.getRequirementId()).isEqualTo(UPDATED_REQUIREMENT_ID);
        assertThat(testSurveyRequirement.getRequirementDescription()).isEqualTo(UPDATED_REQUIREMENT_DESCRIPTION);
        assertThat(testSurveyRequirement.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurveyRequirement.getCreatedByUserCompanyId()).isEqualTo(UPDATED_CREATED_BY_USER_COMPANY_ID);
        assertThat(testSurveyRequirement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyRequirement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurveyRequirementWithPatch() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();

        // Update the surveyRequirement using partial update
        SurveyRequirement partialUpdatedSurveyRequirement = new SurveyRequirement();
        partialUpdatedSurveyRequirement.setId(surveyRequirement.getId());

        partialUpdatedSurveyRequirement.requirementId(UPDATED_REQUIREMENT_ID);

        restSurveyRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyRequirement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyRequirement))
            )
            .andExpect(status().isOk());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
        SurveyRequirement testSurveyRequirement = surveyRequirementList.get(surveyRequirementList.size() - 1);
        assertThat(testSurveyRequirement.getRequirementId()).isEqualTo(UPDATED_REQUIREMENT_ID);
        assertThat(testSurveyRequirement.getRequirementDescription()).isEqualTo(DEFAULT_REQUIREMENT_DESCRIPTION);
        assertThat(testSurveyRequirement.getSurveyId()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testSurveyRequirement.getCreatedByUserCompanyId()).isEqualTo(DEFAULT_CREATED_BY_USER_COMPANY_ID);
        assertThat(testSurveyRequirement.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSurveyRequirementWithPatch() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();

        // Update the surveyRequirement using partial update
        SurveyRequirement partialUpdatedSurveyRequirement = new SurveyRequirement();
        partialUpdatedSurveyRequirement.setId(surveyRequirement.getId());

        partialUpdatedSurveyRequirement
            .requirementId(UPDATED_REQUIREMENT_ID)
            .requirementDescription(UPDATED_REQUIREMENT_DESCRIPTION)
            .surveyId(UPDATED_SURVEY_ID)
            .createdByUserCompanyId(UPDATED_CREATED_BY_USER_COMPANY_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restSurveyRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyRequirement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyRequirement))
            )
            .andExpect(status().isOk());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
        SurveyRequirement testSurveyRequirement = surveyRequirementList.get(surveyRequirementList.size() - 1);
        assertThat(testSurveyRequirement.getRequirementId()).isEqualTo(UPDATED_REQUIREMENT_ID);
        assertThat(testSurveyRequirement.getRequirementDescription()).isEqualTo(UPDATED_REQUIREMENT_DESCRIPTION);
        assertThat(testSurveyRequirement.getSurveyId()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testSurveyRequirement.getCreatedByUserCompanyId()).isEqualTo(UPDATED_CREATED_BY_USER_COMPANY_ID);
        assertThat(testSurveyRequirement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surveyRequirement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurveyRequirement() throws Exception {
        int databaseSizeBeforeUpdate = surveyRequirementRepository.findAll().size();
        surveyRequirement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyRequirement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyRequirement in the database
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurveyRequirement() throws Exception {
        // Initialize the database
        surveyRequirementRepository.saveAndFlush(surveyRequirement);

        int databaseSizeBeforeDelete = surveyRequirementRepository.findAll().size();

        // Delete the surveyRequirement
        restSurveyRequirementMockMvc
            .perform(delete(ENTITY_API_URL_ID, surveyRequirement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SurveyRequirement> surveyRequirementList = surveyRequirementRepository.findAll();
        assertThat(surveyRequirementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
