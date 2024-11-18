package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.OurDatabases;
import com.tale.repository.OurDatabasesRepository;
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
 * Integration tests for the {@link OurDatabasesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OurDatabasesResourceIT {

    private static final Integer DEFAULT_DATABASE_ID = 1;
    private static final Integer UPDATED_DATABASE_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/our-databases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OurDatabasesRepository ourDatabasesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOurDatabasesMockMvc;

    private OurDatabases ourDatabases;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OurDatabases createEntity(EntityManager em) {
        OurDatabases ourDatabases = new OurDatabases().databaseId(DEFAULT_DATABASE_ID).name(DEFAULT_NAME);
        return ourDatabases;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OurDatabases createUpdatedEntity(EntityManager em) {
        OurDatabases ourDatabases = new OurDatabases().databaseId(UPDATED_DATABASE_ID).name(UPDATED_NAME);
        return ourDatabases;
    }

    @BeforeEach
    public void initTest() {
        ourDatabases = createEntity(em);
    }

    @Test
    @Transactional
    void createOurDatabases() throws Exception {
        int databaseSizeBeforeCreate = ourDatabasesRepository.findAll().size();
        // Create the OurDatabases
        restOurDatabasesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isCreated());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeCreate + 1);
        OurDatabases testOurDatabases = ourDatabasesList.get(ourDatabasesList.size() - 1);
        assertThat(testOurDatabases.getDatabaseId()).isEqualTo(DEFAULT_DATABASE_ID);
        assertThat(testOurDatabases.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOurDatabasesWithExistingId() throws Exception {
        // Create the OurDatabases with an existing ID
        ourDatabases.setId(1L);

        int databaseSizeBeforeCreate = ourDatabasesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOurDatabasesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isBadRequest());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOurDatabases() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        // Get all the ourDatabasesList
        restOurDatabasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ourDatabases.getId().intValue())))
            .andExpect(jsonPath("$.[*].databaseId").value(hasItem(DEFAULT_DATABASE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOurDatabases() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        // Get the ourDatabases
        restOurDatabasesMockMvc
            .perform(get(ENTITY_API_URL_ID, ourDatabases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ourDatabases.getId().intValue()))
            .andExpect(jsonPath("$.databaseId").value(DEFAULT_DATABASE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOurDatabases() throws Exception {
        // Get the ourDatabases
        restOurDatabasesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOurDatabases() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();

        // Update the ourDatabases
        OurDatabases updatedOurDatabases = ourDatabasesRepository.findById(ourDatabases.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOurDatabases are not directly saved in db
        em.detach(updatedOurDatabases);
        updatedOurDatabases.databaseId(UPDATED_DATABASE_ID).name(UPDATED_NAME);

        restOurDatabasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOurDatabases.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOurDatabases))
            )
            .andExpect(status().isOk());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
        OurDatabases testOurDatabases = ourDatabasesList.get(ourDatabasesList.size() - 1);
        assertThat(testOurDatabases.getDatabaseId()).isEqualTo(UPDATED_DATABASE_ID);
        assertThat(testOurDatabases.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ourDatabases.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isBadRequest());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isBadRequest());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOurDatabasesWithPatch() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();

        // Update the ourDatabases using partial update
        OurDatabases partialUpdatedOurDatabases = new OurDatabases();
        partialUpdatedOurDatabases.setId(ourDatabases.getId());

        partialUpdatedOurDatabases.name(UPDATED_NAME);

        restOurDatabasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOurDatabases.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOurDatabases))
            )
            .andExpect(status().isOk());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
        OurDatabases testOurDatabases = ourDatabasesList.get(ourDatabasesList.size() - 1);
        assertThat(testOurDatabases.getDatabaseId()).isEqualTo(DEFAULT_DATABASE_ID);
        assertThat(testOurDatabases.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOurDatabasesWithPatch() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();

        // Update the ourDatabases using partial update
        OurDatabases partialUpdatedOurDatabases = new OurDatabases();
        partialUpdatedOurDatabases.setId(ourDatabases.getId());

        partialUpdatedOurDatabases.databaseId(UPDATED_DATABASE_ID).name(UPDATED_NAME);

        restOurDatabasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOurDatabases.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOurDatabases))
            )
            .andExpect(status().isOk());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
        OurDatabases testOurDatabases = ourDatabasesList.get(ourDatabasesList.size() - 1);
        assertThat(testOurDatabases.getDatabaseId()).isEqualTo(UPDATED_DATABASE_ID);
        assertThat(testOurDatabases.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ourDatabases.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isBadRequest());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isBadRequest());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOurDatabases() throws Exception {
        int databaseSizeBeforeUpdate = ourDatabasesRepository.findAll().size();
        ourDatabases.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOurDatabasesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ourDatabases))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OurDatabases in the database
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOurDatabases() throws Exception {
        // Initialize the database
        ourDatabasesRepository.saveAndFlush(ourDatabases);

        int databaseSizeBeforeDelete = ourDatabasesRepository.findAll().size();

        // Delete the ourDatabases
        restOurDatabasesMockMvc
            .perform(delete(ENTITY_API_URL_ID, ourDatabases.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OurDatabases> ourDatabasesList = ourDatabasesRepository.findAll();
        assertThat(ourDatabasesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
