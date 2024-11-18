package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.CompanyUser;
import com.tale.repository.CompanyUserRepository;
import jakarta.persistence.EntityManager;
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

/**
 * Integration tests for the {@link CompanyUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompanyUserResourceIT {

    private static final Integer DEFAULT_COMPANY_USER_ID = 1;
    private static final Integer UPDATED_COMPANY_USER_ID = 2;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/company-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Mock
    private CompanyUserRepository companyUserRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyUserMockMvc;

    private CompanyUser companyUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyUser createEntity(EntityManager em) {
        CompanyUser companyUser = new CompanyUser()
            .companyUserId(DEFAULT_COMPANY_USER_ID)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .jobTitle(DEFAULT_JOB_TITLE)
            .status(DEFAULT_STATUS);
        return companyUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyUser createUpdatedEntity(EntityManager em) {
        CompanyUser companyUser = new CompanyUser()
            .companyUserId(UPDATED_COMPANY_USER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .status(UPDATED_STATUS);
        return companyUser;
    }

    @BeforeEach
    public void initTest() {
        companyUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyUser() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();
        // Create the CompanyUser
        restCompanyUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompanyUserId()).isEqualTo(DEFAULT_COMPANY_USER_ID);
        assertThat(testCompanyUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testCompanyUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCompanyUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCompanyUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCompanyUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCompanyUser.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCompanyUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCompanyUserWithExistingId() throws Exception {
        // Create the CompanyUser with an existing ID
        companyUser.setId(1L);

        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyUserRepository.findAll().size();
        // set the field null
        companyUser.setUsername(null);

        // Create the CompanyUser, which fails.

        restCompanyUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanyUsers() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get all the companyUserList
        restCompanyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyUserId").value(hasItem(DEFAULT_COMPANY_USER_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(companyUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompanyUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(companyUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(companyUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompanyUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(companyUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get the companyUser
        restCompanyUserMockMvc
            .perform(get(ENTITY_API_URL_ID, companyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyUser.getId().intValue()))
            .andExpect(jsonPath("$.companyUserId").value(DEFAULT_COMPANY_USER_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingCompanyUser() throws Exception {
        // Get the companyUser
        restCompanyUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser
        CompanyUser updatedCompanyUser = companyUserRepository.findById(companyUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompanyUser are not directly saved in db
        em.detach(updatedCompanyUser);
        updatedCompanyUser
            .companyUserId(UPDATED_COMPANY_USER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .status(UPDATED_STATUS);

        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompanyUserId()).isEqualTo(UPDATED_COMPANY_USER_ID);
        assertThat(testCompanyUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testCompanyUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCompanyUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCompanyUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompanyUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompanyUser.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCompanyUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyUserWithPatch() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser using partial update
        CompanyUser partialUpdatedCompanyUser = new CompanyUser();
        partialUpdatedCompanyUser.setId(companyUser.getId());

        partialUpdatedCompanyUser
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompanyUserId()).isEqualTo(DEFAULT_COMPANY_USER_ID);
        assertThat(testCompanyUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testCompanyUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCompanyUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCompanyUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompanyUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompanyUser.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCompanyUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCompanyUserWithPatch() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser using partial update
        CompanyUser partialUpdatedCompanyUser = new CompanyUser();
        partialUpdatedCompanyUser.setId(companyUser.getId());

        partialUpdatedCompanyUser
            .companyUserId(UPDATED_COMPANY_USER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .status(UPDATED_STATUS);

        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompanyUserId()).isEqualTo(UPDATED_COMPANY_USER_ID);
        assertThat(testCompanyUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testCompanyUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCompanyUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCompanyUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompanyUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompanyUser.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCompanyUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeDelete = companyUserRepository.findAll().size();

        // Delete the companyUser
        restCompanyUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
