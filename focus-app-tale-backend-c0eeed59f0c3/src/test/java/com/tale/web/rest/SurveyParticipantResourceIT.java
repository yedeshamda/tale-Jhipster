package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.SurveyParticipant;
import com.tale.repository.SurveyParticipantRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SurveyParticipantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurveyParticipantResourceIT {

    private static final Integer DEFAULT_PARTICIPANT_ID = 1;
    private static final Integer UPDATED_PARTICIPANT_ID = 2;

    private static final String ENTITY_API_URL = "/api/survey-participants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurveyParticipantRepository surveyParticipantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveyParticipantMockMvc;

    private SurveyParticipant surveyParticipant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyParticipant createEntity(EntityManager em) {
        SurveyParticipant surveyParticipant = new SurveyParticipant().participantId(DEFAULT_PARTICIPANT_ID);
        return surveyParticipant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyParticipant createUpdatedEntity(EntityManager em) {
        SurveyParticipant surveyParticipant = new SurveyParticipant().participantId(UPDATED_PARTICIPANT_ID);
        return surveyParticipant;
    }

    @BeforeEach
    public void initTest() {
        surveyParticipant = createEntity(em);
    }

    @Test
    @Transactional
    void createSurveyParticipant() throws Exception {
        int databaseSizeBeforeCreate = surveyParticipantRepository.findAll().size();
        // Create the SurveyParticipant
        restSurveyParticipantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isCreated());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeCreate + 1);
        SurveyParticipant testSurveyParticipant = surveyParticipantList.get(surveyParticipantList.size() - 1);
        assertThat(testSurveyParticipant.getParticipantId()).isEqualTo(DEFAULT_PARTICIPANT_ID);
    }

    @Test
    @Transactional
    void createSurveyParticipantWithExistingId() throws Exception {
        // Create the SurveyParticipant with an existing ID
        surveyParticipant.setId(1L);

        int databaseSizeBeforeCreate = surveyParticipantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyParticipantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurveyParticipants() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        // Get all the surveyParticipantList
        restSurveyParticipantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyParticipant.getId().intValue())))
            .andExpect(jsonPath("$.[*].participantId").value(hasItem(DEFAULT_PARTICIPANT_ID)));
    }

    @Test
    @Transactional
    void getSurveyParticipant() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        // Get the surveyParticipant
        restSurveyParticipantMockMvc
            .perform(get(ENTITY_API_URL_ID, surveyParticipant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surveyParticipant.getId().intValue()))
            .andExpect(jsonPath("$.participantId").value(DEFAULT_PARTICIPANT_ID));
    }

    @Test
    @Transactional
    void getNonExistingSurveyParticipant() throws Exception {
        // Get the surveyParticipant
        restSurveyParticipantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurveyParticipant() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();

        // Update the surveyParticipant
        SurveyParticipant updatedSurveyParticipant = surveyParticipantRepository.findById(surveyParticipant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurveyParticipant are not directly saved in db
        em.detach(updatedSurveyParticipant);
        updatedSurveyParticipant.participantId(UPDATED_PARTICIPANT_ID);

        restSurveyParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurveyParticipant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurveyParticipant))
            )
            .andExpect(status().isOk());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
        SurveyParticipant testSurveyParticipant = surveyParticipantList.get(surveyParticipantList.size() - 1);
        assertThat(testSurveyParticipant.getParticipantId()).isEqualTo(UPDATED_PARTICIPANT_ID);
    }

    @Test
    @Transactional
    void putNonExistingSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyParticipant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurveyParticipantWithPatch() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();

        // Update the surveyParticipant using partial update
        SurveyParticipant partialUpdatedSurveyParticipant = new SurveyParticipant();
        partialUpdatedSurveyParticipant.setId(surveyParticipant.getId());

        restSurveyParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyParticipant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyParticipant))
            )
            .andExpect(status().isOk());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
        SurveyParticipant testSurveyParticipant = surveyParticipantList.get(surveyParticipantList.size() - 1);
        assertThat(testSurveyParticipant.getParticipantId()).isEqualTo(DEFAULT_PARTICIPANT_ID);
    }

    @Test
    @Transactional
    void fullUpdateSurveyParticipantWithPatch() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();

        // Update the surveyParticipant using partial update
        SurveyParticipant partialUpdatedSurveyParticipant = new SurveyParticipant();
        partialUpdatedSurveyParticipant.setId(surveyParticipant.getId());

        partialUpdatedSurveyParticipant.participantId(UPDATED_PARTICIPANT_ID);

        restSurveyParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveyParticipant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveyParticipant))
            )
            .andExpect(status().isOk());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
        SurveyParticipant testSurveyParticipant = surveyParticipantList.get(surveyParticipantList.size() - 1);
        assertThat(testSurveyParticipant.getParticipantId()).isEqualTo(UPDATED_PARTICIPANT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surveyParticipant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurveyParticipant() throws Exception {
        int databaseSizeBeforeUpdate = surveyParticipantRepository.findAll().size();
        surveyParticipant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveyParticipant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurveyParticipant in the database
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurveyParticipant() throws Exception {
        // Initialize the database
        surveyParticipantRepository.saveAndFlush(surveyParticipant);

        int databaseSizeBeforeDelete = surveyParticipantRepository.findAll().size();

        // Delete the surveyParticipant
        restSurveyParticipantMockMvc
            .perform(delete(ENTITY_API_URL_ID, surveyParticipant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SurveyParticipant> surveyParticipantList = surveyParticipantRepository.findAll();
        assertThat(surveyParticipantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
