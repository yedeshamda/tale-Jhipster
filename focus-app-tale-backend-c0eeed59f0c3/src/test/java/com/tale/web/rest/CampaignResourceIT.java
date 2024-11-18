package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Campaign;
import com.tale.repository.CampaignRepository;
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
 * Integration tests for the {@link CampaignResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CampaignResourceIT {

    private static final Integer DEFAULT_CAMPAIGN_ID = 1;
    private static final Integer UPDATED_CAMPAIGN_ID = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/campaigns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return campaign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createUpdatedEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .campaignId(UPDATED_CAMPAIGN_ID)
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return campaign;
    }

    @BeforeEach
    public void initTest() {
        campaign = createEntity(em);
    }

    @Test
    @Transactional
    void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();
        // Create the Campaign
        restCampaignMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testCampaign.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createCampaignWithExistingId() throws Exception {
        // Create the Campaign with an existing ID
        campaign.setId(1L);

        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList
        restCampaignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].campaignId").value(hasItem(DEFAULT_CAMPAIGN_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }

    @Test
    @Transactional
    void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get the campaign
        restCampaignMockMvc
            .perform(get(ENTITY_API_URL_ID, campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campaign.getId().intValue()))
            .andExpect(jsonPath("$.campaignId").value(DEFAULT_CAMPAIGN_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCampaign are not directly saved in db
        em.detach(updatedCampaign);
        updatedCampaign.campaignId(UPDATED_CAMPAIGN_ID).title(UPDATED_TITLE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restCampaignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCampaign.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCampaign))
            )
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testCampaign.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campaign.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign.endDate(UPDATED_END_DATE);

        restCampaignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            )
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testCampaign.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign.campaignId(UPDATED_CAMPAIGN_ID).title(UPDATED_TITLE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restCampaignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            )
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testCampaign.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, campaign.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();
        campaign.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaign))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Delete the campaign
        restCampaignMockMvc
            .perform(delete(ENTITY_API_URL_ID, campaign.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
