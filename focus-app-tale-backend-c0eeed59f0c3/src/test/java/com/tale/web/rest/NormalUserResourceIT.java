package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.NormalUser;
import com.tale.repository.NormalUserRepository;
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
 * Integration tests for the {@link NormalUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NormalUserResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_EARNED_POINTS = 1;
    private static final Integer UPDATED_EARNED_POINTS = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/normal-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNormalUserMockMvc;

    private NormalUser normalUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NormalUser createEntity(EntityManager em) {
        NormalUser normalUser = new NormalUser()
            .userId(DEFAULT_USER_ID)
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .age(DEFAULT_AGE)
            .job(DEFAULT_JOB)
            .gender(DEFAULT_GENDER)
            .address(DEFAULT_ADDRESS)
            .earnedPoints(DEFAULT_EARNED_POINTS)
            .status(DEFAULT_STATUS);
        return normalUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NormalUser createUpdatedEntity(EntityManager em) {
        NormalUser normalUser = new NormalUser()
            .userId(UPDATED_USER_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .age(UPDATED_AGE)
            .job(UPDATED_JOB)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .earnedPoints(UPDATED_EARNED_POINTS)
            .status(UPDATED_STATUS);
        return normalUser;
    }

    @BeforeEach
    public void initTest() {
        normalUser = createEntity(em);
    }

    @Test
    @Transactional
    void createNormalUser() throws Exception {
        int databaseSizeBeforeCreate = normalUserRepository.findAll().size();
        // Create the NormalUser
        restNormalUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isCreated());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeCreate + 1);
        NormalUser testNormalUser = normalUserList.get(normalUserList.size() - 1);
        assertThat(testNormalUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testNormalUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testNormalUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNormalUser.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testNormalUser.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testNormalUser.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testNormalUser.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testNormalUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testNormalUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testNormalUser.getEarnedPoints()).isEqualTo(DEFAULT_EARNED_POINTS);
        assertThat(testNormalUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createNormalUserWithExistingId() throws Exception {
        // Create the NormalUser with an existing ID
        normalUser.setId(1L);

        int databaseSizeBeforeCreate = normalUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNormalUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNormalUsers() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        // Get all the normalUserList
        restNormalUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(normalUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].earnedPoints").value(hasItem(DEFAULT_EARNED_POINTS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getNormalUser() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        // Get the normalUser
        restNormalUserMockMvc
            .perform(get(ENTITY_API_URL_ID, normalUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(normalUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.earnedPoints").value(DEFAULT_EARNED_POINTS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingNormalUser() throws Exception {
        // Get the normalUser
        restNormalUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNormalUser() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();

        // Update the normalUser
        NormalUser updatedNormalUser = normalUserRepository.findById(normalUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNormalUser are not directly saved in db
        em.detach(updatedNormalUser);
        updatedNormalUser
            .userId(UPDATED_USER_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .age(UPDATED_AGE)
            .job(UPDATED_JOB)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .earnedPoints(UPDATED_EARNED_POINTS)
            .status(UPDATED_STATUS);

        restNormalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNormalUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNormalUser))
            )
            .andExpect(status().isOk());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
        NormalUser testNormalUser = normalUserList.get(normalUserList.size() - 1);
        assertThat(testNormalUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testNormalUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testNormalUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNormalUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testNormalUser.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testNormalUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testNormalUser.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testNormalUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testNormalUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNormalUser.getEarnedPoints()).isEqualTo(UPDATED_EARNED_POINTS);
        assertThat(testNormalUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, normalUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNormalUserWithPatch() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();

        // Update the normalUser using partial update
        NormalUser partialUpdatedNormalUser = new NormalUser();
        partialUpdatedNormalUser.setId(normalUser.getId());

        partialUpdatedNormalUser.username(UPDATED_USERNAME).firstname(UPDATED_FIRSTNAME).job(UPDATED_JOB);

        restNormalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNormalUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNormalUser))
            )
            .andExpect(status().isOk());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
        NormalUser testNormalUser = normalUserList.get(normalUserList.size() - 1);
        assertThat(testNormalUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testNormalUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testNormalUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNormalUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testNormalUser.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testNormalUser.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testNormalUser.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testNormalUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testNormalUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testNormalUser.getEarnedPoints()).isEqualTo(DEFAULT_EARNED_POINTS);
        assertThat(testNormalUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateNormalUserWithPatch() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();

        // Update the normalUser using partial update
        NormalUser partialUpdatedNormalUser = new NormalUser();
        partialUpdatedNormalUser.setId(normalUser.getId());

        partialUpdatedNormalUser
            .userId(UPDATED_USER_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .age(UPDATED_AGE)
            .job(UPDATED_JOB)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .earnedPoints(UPDATED_EARNED_POINTS)
            .status(UPDATED_STATUS);

        restNormalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNormalUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNormalUser))
            )
            .andExpect(status().isOk());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
        NormalUser testNormalUser = normalUserList.get(normalUserList.size() - 1);
        assertThat(testNormalUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testNormalUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testNormalUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNormalUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testNormalUser.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testNormalUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testNormalUser.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testNormalUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testNormalUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNormalUser.getEarnedPoints()).isEqualTo(UPDATED_EARNED_POINTS);
        assertThat(testNormalUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, normalUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNormalUser() throws Exception {
        int databaseSizeBeforeUpdate = normalUserRepository.findAll().size();
        normalUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNormalUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(normalUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NormalUser in the database
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNormalUser() throws Exception {
        // Initialize the database
        normalUserRepository.saveAndFlush(normalUser);

        int databaseSizeBeforeDelete = normalUserRepository.findAll().size();

        // Delete the normalUser
        restNormalUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, normalUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NormalUser> normalUserList = normalUserRepository.findAll();
        assertThat(normalUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
