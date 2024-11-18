package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Analyticss;
import com.tale.repository.AnalyticssRepository;
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
 * Integration tests for the {@link AnalyticssResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnalyticssResourceIT {

    private static final Integer DEFAULT_ANALYTICS_ID = 1;
    private static final Integer UPDATED_ANALYTICS_ID = 2;

    private static final Boolean DEFAULT_ISREADY = false;
    private static final Boolean UPDATED_ISREADY = true;

    private static final String DEFAULT_INSIGHTS = "AAAAAAAAAA";
    private static final String UPDATED_INSIGHTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/analyticsses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnalyticssRepository analyticssRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyticssMockMvc;

    private Analyticss analyticss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analyticss createEntity(EntityManager em) {
        Analyticss analyticss = new Analyticss().analyticsId(DEFAULT_ANALYTICS_ID).isready(DEFAULT_ISREADY).insights(DEFAULT_INSIGHTS);
        return analyticss;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analyticss createUpdatedEntity(EntityManager em) {
        Analyticss analyticss = new Analyticss().analyticsId(UPDATED_ANALYTICS_ID).isready(UPDATED_ISREADY).insights(UPDATED_INSIGHTS);
        return analyticss;
    }

    @BeforeEach
    public void initTest() {
        analyticss = createEntity(em);
    }

    @Test
    @Transactional
    void createAnalyticss() throws Exception {
        int databaseSizeBeforeCreate = analyticssRepository.findAll().size();
        // Create the Analyticss
        restAnalyticssMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isCreated());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeCreate + 1);
        Analyticss testAnalyticss = analyticssList.get(analyticssList.size() - 1);
        assertThat(testAnalyticss.getAnalyticsId()).isEqualTo(DEFAULT_ANALYTICS_ID);
        assertThat(testAnalyticss.getIsready()).isEqualTo(DEFAULT_ISREADY);
        assertThat(testAnalyticss.getInsights()).isEqualTo(DEFAULT_INSIGHTS);
    }

    @Test
    @Transactional
    void createAnalyticssWithExistingId() throws Exception {
        // Create the Analyticss with an existing ID
        analyticss.setId(1L);

        int databaseSizeBeforeCreate = analyticssRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyticssMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnalyticsses() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        // Get all the analyticssList
        restAnalyticssMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyticss.getId().intValue())))
            .andExpect(jsonPath("$.[*].analyticsId").value(hasItem(DEFAULT_ANALYTICS_ID)))
            .andExpect(jsonPath("$.[*].isready").value(hasItem(DEFAULT_ISREADY.booleanValue())))
            .andExpect(jsonPath("$.[*].insights").value(hasItem(DEFAULT_INSIGHTS)));
    }

    @Test
    @Transactional
    void getAnalyticss() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        // Get the analyticss
        restAnalyticssMockMvc
            .perform(get(ENTITY_API_URL_ID, analyticss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analyticss.getId().intValue()))
            .andExpect(jsonPath("$.analyticsId").value(DEFAULT_ANALYTICS_ID))
            .andExpect(jsonPath("$.isready").value(DEFAULT_ISREADY.booleanValue()))
            .andExpect(jsonPath("$.insights").value(DEFAULT_INSIGHTS));
    }

    @Test
    @Transactional
    void getNonExistingAnalyticss() throws Exception {
        // Get the analyticss
        restAnalyticssMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnalyticss() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();

        // Update the analyticss
        Analyticss updatedAnalyticss = analyticssRepository.findById(analyticss.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnalyticss are not directly saved in db
        em.detach(updatedAnalyticss);
        updatedAnalyticss.analyticsId(UPDATED_ANALYTICS_ID).isready(UPDATED_ISREADY).insights(UPDATED_INSIGHTS);

        restAnalyticssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalyticss.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnalyticss))
            )
            .andExpect(status().isOk());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
        Analyticss testAnalyticss = analyticssList.get(analyticssList.size() - 1);
        assertThat(testAnalyticss.getAnalyticsId()).isEqualTo(UPDATED_ANALYTICS_ID);
        assertThat(testAnalyticss.getIsready()).isEqualTo(UPDATED_ISREADY);
        assertThat(testAnalyticss.getInsights()).isEqualTo(UPDATED_INSIGHTS);
    }

    @Test
    @Transactional
    void putNonExistingAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analyticss.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnalyticssWithPatch() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();

        // Update the analyticss using partial update
        Analyticss partialUpdatedAnalyticss = new Analyticss();
        partialUpdatedAnalyticss.setId(analyticss.getId());

        partialUpdatedAnalyticss.analyticsId(UPDATED_ANALYTICS_ID);

        restAnalyticssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyticss.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyticss))
            )
            .andExpect(status().isOk());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
        Analyticss testAnalyticss = analyticssList.get(analyticssList.size() - 1);
        assertThat(testAnalyticss.getAnalyticsId()).isEqualTo(UPDATED_ANALYTICS_ID);
        assertThat(testAnalyticss.getIsready()).isEqualTo(DEFAULT_ISREADY);
        assertThat(testAnalyticss.getInsights()).isEqualTo(DEFAULT_INSIGHTS);
    }

    @Test
    @Transactional
    void fullUpdateAnalyticssWithPatch() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();

        // Update the analyticss using partial update
        Analyticss partialUpdatedAnalyticss = new Analyticss();
        partialUpdatedAnalyticss.setId(analyticss.getId());

        partialUpdatedAnalyticss.analyticsId(UPDATED_ANALYTICS_ID).isready(UPDATED_ISREADY).insights(UPDATED_INSIGHTS);

        restAnalyticssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyticss.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyticss))
            )
            .andExpect(status().isOk());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
        Analyticss testAnalyticss = analyticssList.get(analyticssList.size() - 1);
        assertThat(testAnalyticss.getAnalyticsId()).isEqualTo(UPDATED_ANALYTICS_ID);
        assertThat(testAnalyticss.getIsready()).isEqualTo(UPDATED_ISREADY);
        assertThat(testAnalyticss.getInsights()).isEqualTo(UPDATED_INSIGHTS);
    }

    @Test
    @Transactional
    void patchNonExistingAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analyticss.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalyticss() throws Exception {
        int databaseSizeBeforeUpdate = analyticssRepository.findAll().size();
        analyticss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyticssMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyticss))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Analyticss in the database
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnalyticss() throws Exception {
        // Initialize the database
        analyticssRepository.saveAndFlush(analyticss);

        int databaseSizeBeforeDelete = analyticssRepository.findAll().size();

        // Delete the analyticss
        restAnalyticssMockMvc
            .perform(delete(ENTITY_API_URL_ID, analyticss.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Analyticss> analyticssList = analyticssRepository.findAll();
        assertThat(analyticssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
