package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.PointSpendOption;
import com.tale.repository.PointSpendOptionRepository;
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
 * Integration tests for the {@link PointSpendOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointSpendOptionResourceIT {

    private static final Integer DEFAULT_REDEMPTION_OPTION_ID = 1;
    private static final Integer UPDATED_REDEMPTION_OPTION_ID = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS_REQUIRED = 1;
    private static final Integer UPDATED_POINTS_REQUIRED = 2;

    private static final Integer DEFAULT_AVAILABLE_QUANTITY = 1;
    private static final Integer UPDATED_AVAILABLE_QUANTITY = 2;

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/point-spend-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointSpendOptionRepository pointSpendOptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointSpendOptionMockMvc;

    private PointSpendOption pointSpendOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointSpendOption createEntity(EntityManager em) {
        PointSpendOption pointSpendOption = new PointSpendOption()
            .redemptionOptionId(DEFAULT_REDEMPTION_OPTION_ID)
            .description(DEFAULT_DESCRIPTION)
            .pointsRequired(DEFAULT_POINTS_REQUIRED)
            .availableQuantity(DEFAULT_AVAILABLE_QUANTITY)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
        return pointSpendOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointSpendOption createUpdatedEntity(EntityManager em) {
        PointSpendOption pointSpendOption = new PointSpendOption()
            .redemptionOptionId(UPDATED_REDEMPTION_OPTION_ID)
            .description(UPDATED_DESCRIPTION)
            .pointsRequired(UPDATED_POINTS_REQUIRED)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        return pointSpendOption;
    }

    @BeforeEach
    public void initTest() {
        pointSpendOption = createEntity(em);
    }

    @Test
    @Transactional
    void createPointSpendOption() throws Exception {
        int databaseSizeBeforeCreate = pointSpendOptionRepository.findAll().size();
        // Create the PointSpendOption
        restPointSpendOptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isCreated());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeCreate + 1);
        PointSpendOption testPointSpendOption = pointSpendOptionList.get(pointSpendOptionList.size() - 1);
        assertThat(testPointSpendOption.getRedemptionOptionId()).isEqualTo(DEFAULT_REDEMPTION_OPTION_ID);
        assertThat(testPointSpendOption.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointSpendOption.getPointsRequired()).isEqualTo(DEFAULT_POINTS_REQUIRED);
        assertThat(testPointSpendOption.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testPointSpendOption.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void createPointSpendOptionWithExistingId() throws Exception {
        // Create the PointSpendOption with an existing ID
        pointSpendOption.setId(1L);

        int databaseSizeBeforeCreate = pointSpendOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointSpendOptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPointSpendOptions() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        // Get all the pointSpendOptionList
        restPointSpendOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointSpendOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].redemptionOptionId").value(hasItem(DEFAULT_REDEMPTION_OPTION_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pointsRequired").value(hasItem(DEFAULT_POINTS_REQUIRED)))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))));
    }

    @Test
    @Transactional
    void getPointSpendOption() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        // Get the pointSpendOption
        restPointSpendOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, pointSpendOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointSpendOption.getId().intValue()))
            .andExpect(jsonPath("$.redemptionOptionId").value(DEFAULT_REDEMPTION_OPTION_ID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pointsRequired").value(DEFAULT_POINTS_REQUIRED))
            .andExpect(jsonPath("$.availableQuantity").value(DEFAULT_AVAILABLE_QUANTITY))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingPointSpendOption() throws Exception {
        // Get the pointSpendOption
        restPointSpendOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPointSpendOption() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();

        // Update the pointSpendOption
        PointSpendOption updatedPointSpendOption = pointSpendOptionRepository.findById(pointSpendOption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPointSpendOption are not directly saved in db
        em.detach(updatedPointSpendOption);
        updatedPointSpendOption
            .redemptionOptionId(UPDATED_REDEMPTION_OPTION_ID)
            .description(UPDATED_DESCRIPTION)
            .pointsRequired(UPDATED_POINTS_REQUIRED)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restPointSpendOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointSpendOption.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointSpendOption))
            )
            .andExpect(status().isOk());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
        PointSpendOption testPointSpendOption = pointSpendOptionList.get(pointSpendOptionList.size() - 1);
        assertThat(testPointSpendOption.getRedemptionOptionId()).isEqualTo(UPDATED_REDEMPTION_OPTION_ID);
        assertThat(testPointSpendOption.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointSpendOption.getPointsRequired()).isEqualTo(UPDATED_POINTS_REQUIRED);
        assertThat(testPointSpendOption.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testPointSpendOption.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointSpendOption.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointSpendOptionWithPatch() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();

        // Update the pointSpendOption using partial update
        PointSpendOption partialUpdatedPointSpendOption = new PointSpendOption();
        partialUpdatedPointSpendOption.setId(pointSpendOption.getId());

        partialUpdatedPointSpendOption.redemptionOptionId(UPDATED_REDEMPTION_OPTION_ID).availableQuantity(UPDATED_AVAILABLE_QUANTITY);

        restPointSpendOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointSpendOption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointSpendOption))
            )
            .andExpect(status().isOk());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
        PointSpendOption testPointSpendOption = pointSpendOptionList.get(pointSpendOptionList.size() - 1);
        assertThat(testPointSpendOption.getRedemptionOptionId()).isEqualTo(UPDATED_REDEMPTION_OPTION_ID);
        assertThat(testPointSpendOption.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointSpendOption.getPointsRequired()).isEqualTo(DEFAULT_POINTS_REQUIRED);
        assertThat(testPointSpendOption.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testPointSpendOption.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePointSpendOptionWithPatch() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();

        // Update the pointSpendOption using partial update
        PointSpendOption partialUpdatedPointSpendOption = new PointSpendOption();
        partialUpdatedPointSpendOption.setId(pointSpendOption.getId());

        partialUpdatedPointSpendOption
            .redemptionOptionId(UPDATED_REDEMPTION_OPTION_ID)
            .description(UPDATED_DESCRIPTION)
            .pointsRequired(UPDATED_POINTS_REQUIRED)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restPointSpendOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointSpendOption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointSpendOption))
            )
            .andExpect(status().isOk());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
        PointSpendOption testPointSpendOption = pointSpendOptionList.get(pointSpendOptionList.size() - 1);
        assertThat(testPointSpendOption.getRedemptionOptionId()).isEqualTo(UPDATED_REDEMPTION_OPTION_ID);
        assertThat(testPointSpendOption.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointSpendOption.getPointsRequired()).isEqualTo(UPDATED_POINTS_REQUIRED);
        assertThat(testPointSpendOption.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testPointSpendOption.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointSpendOption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointSpendOption() throws Exception {
        int databaseSizeBeforeUpdate = pointSpendOptionRepository.findAll().size();
        pointSpendOption.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointSpendOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointSpendOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointSpendOption in the database
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointSpendOption() throws Exception {
        // Initialize the database
        pointSpendOptionRepository.saveAndFlush(pointSpendOption);

        int databaseSizeBeforeDelete = pointSpendOptionRepository.findAll().size();

        // Delete the pointSpendOption
        restPointSpendOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointSpendOption.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointSpendOption> pointSpendOptionList = pointSpendOptionRepository.findAll();
        assertThat(pointSpendOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
