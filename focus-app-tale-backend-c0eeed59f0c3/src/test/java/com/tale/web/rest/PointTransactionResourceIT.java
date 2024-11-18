package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.PointTransaction;
import com.tale.repository.PointTransactionRepository;
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
 * Integration tests for the {@link PointTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointTransactionResourceIT {

    private static final Integer DEFAULT_TRANSACTION_ID = 1;
    private static final Integer UPDATED_TRANSACTION_ID = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final ZonedDateTime DEFAULT_TRANSACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/point-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointTransactionMockMvc;

    private PointTransaction pointTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointTransaction createEntity(EntityManager em) {
        PointTransaction pointTransaction = new PointTransaction()
            .transactionId(DEFAULT_TRANSACTION_ID)
            .description(DEFAULT_DESCRIPTION)
            .points(DEFAULT_POINTS)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .userId(DEFAULT_USER_ID);
        return pointTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointTransaction createUpdatedEntity(EntityManager em) {
        PointTransaction pointTransaction = new PointTransaction()
            .transactionId(UPDATED_TRANSACTION_ID)
            .description(UPDATED_DESCRIPTION)
            .points(UPDATED_POINTS)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .userId(UPDATED_USER_ID);
        return pointTransaction;
    }

    @BeforeEach
    public void initTest() {
        pointTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createPointTransaction() throws Exception {
        int databaseSizeBeforeCreate = pointTransactionRepository.findAll().size();
        // Create the PointTransaction
        restPointTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        PointTransaction testPointTransaction = pointTransactionList.get(pointTransactionList.size() - 1);
        assertThat(testPointTransaction.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPointTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointTransaction.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testPointTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPointTransaction.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createPointTransactionWithExistingId() throws Exception {
        // Create the PointTransaction with an existing ID
        pointTransaction.setId(1L);

        int databaseSizeBeforeCreate = pointTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPointTransactions() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        // Get all the pointTransactionList
        restPointTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(sameInstant(DEFAULT_TRANSACTION_DATE))))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getPointTransaction() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        // Get the pointTransaction
        restPointTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, pointTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.transactionDate").value(sameInstant(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingPointTransaction() throws Exception {
        // Get the pointTransaction
        restPointTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPointTransaction() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();

        // Update the pointTransaction
        PointTransaction updatedPointTransaction = pointTransactionRepository.findById(pointTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPointTransaction are not directly saved in db
        em.detach(updatedPointTransaction);
        updatedPointTransaction
            .transactionId(UPDATED_TRANSACTION_ID)
            .description(UPDATED_DESCRIPTION)
            .points(UPDATED_POINTS)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .userId(UPDATED_USER_ID);

        restPointTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPointTransaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPointTransaction))
            )
            .andExpect(status().isOk());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
        PointTransaction testPointTransaction = pointTransactionList.get(pointTransactionList.size() - 1);
        assertThat(testPointTransaction.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPointTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointTransaction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPointTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPointTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointTransaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointTransactionWithPatch() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();

        // Update the pointTransaction using partial update
        PointTransaction partialUpdatedPointTransaction = new PointTransaction();
        partialUpdatedPointTransaction.setId(pointTransaction.getId());

        partialUpdatedPointTransaction.description(UPDATED_DESCRIPTION).points(UPDATED_POINTS).transactionDate(UPDATED_TRANSACTION_DATE);

        restPointTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointTransaction))
            )
            .andExpect(status().isOk());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
        PointTransaction testPointTransaction = pointTransactionList.get(pointTransactionList.size() - 1);
        assertThat(testPointTransaction.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPointTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointTransaction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPointTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPointTransaction.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdatePointTransactionWithPatch() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();

        // Update the pointTransaction using partial update
        PointTransaction partialUpdatedPointTransaction = new PointTransaction();
        partialUpdatedPointTransaction.setId(pointTransaction.getId());

        partialUpdatedPointTransaction
            .transactionId(UPDATED_TRANSACTION_ID)
            .description(UPDATED_DESCRIPTION)
            .points(UPDATED_POINTS)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .userId(UPDATED_USER_ID);

        restPointTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointTransaction))
            )
            .andExpect(status().isOk());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
        PointTransaction testPointTransaction = pointTransactionList.get(pointTransactionList.size() - 1);
        assertThat(testPointTransaction.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPointTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointTransaction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPointTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPointTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointTransaction() throws Exception {
        int databaseSizeBeforeUpdate = pointTransactionRepository.findAll().size();
        pointTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PointTransaction in the database
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointTransaction() throws Exception {
        // Initialize the database
        pointTransactionRepository.saveAndFlush(pointTransaction);

        int databaseSizeBeforeDelete = pointTransactionRepository.findAll().size();

        // Delete the pointTransaction
        restPointTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointTransaction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAll();
        assertThat(pointTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
