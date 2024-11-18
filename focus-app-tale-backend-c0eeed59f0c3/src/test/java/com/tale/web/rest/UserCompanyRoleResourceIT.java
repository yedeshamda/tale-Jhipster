package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.UserCompanyRole;
import com.tale.repository.UserCompanyRoleRepository;
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
 * Integration tests for the {@link UserCompanyRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserCompanyRoleResourceIT {

    private static final Integer DEFAULT_ROLE_ID = 1;
    private static final Integer UPDATED_ROLE_ID = 2;

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/user-company-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCompanyRoleRepository userCompanyRoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCompanyRoleMockMvc;

    private UserCompanyRole userCompanyRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCompanyRole createEntity(EntityManager em) {
        UserCompanyRole userCompanyRole = new UserCompanyRole()
            .roleId(DEFAULT_ROLE_ID)
            .roleName(DEFAULT_ROLE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return userCompanyRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCompanyRole createUpdatedEntity(EntityManager em) {
        UserCompanyRole userCompanyRole = new UserCompanyRole()
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        return userCompanyRole;
    }

    @BeforeEach
    public void initTest() {
        userCompanyRole = createEntity(em);
    }

    @Test
    @Transactional
    void createUserCompanyRole() throws Exception {
        int databaseSizeBeforeCreate = userCompanyRoleRepository.findAll().size();
        // Create the UserCompanyRole
        restUserCompanyRoleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isCreated());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeCreate + 1);
        UserCompanyRole testUserCompanyRole = userCompanyRoleList.get(userCompanyRoleList.size() - 1);
        assertThat(testUserCompanyRole.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testUserCompanyRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testUserCompanyRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserCompanyRole.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUserCompanyRoleWithExistingId() throws Exception {
        // Create the UserCompanyRole with an existing ID
        userCompanyRole.setId(1L);

        int databaseSizeBeforeCreate = userCompanyRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCompanyRoleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserCompanyRoles() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        // Get all the userCompanyRoleList
        restUserCompanyRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCompanyRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getUserCompanyRole() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        // Get the userCompanyRole
        restUserCompanyRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, userCompanyRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCompanyRole.getId().intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingUserCompanyRole() throws Exception {
        // Get the userCompanyRole
        restUserCompanyRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserCompanyRole() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();

        // Update the userCompanyRole
        UserCompanyRole updatedUserCompanyRole = userCompanyRoleRepository.findById(userCompanyRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserCompanyRole are not directly saved in db
        em.detach(updatedUserCompanyRole);
        updatedUserCompanyRole.roleId(UPDATED_ROLE_ID).roleName(UPDATED_ROLE_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restUserCompanyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserCompanyRole.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserCompanyRole))
            )
            .andExpect(status().isOk());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
        UserCompanyRole testUserCompanyRole = userCompanyRoleList.get(userCompanyRoleList.size() - 1);
        assertThat(testUserCompanyRole.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testUserCompanyRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testUserCompanyRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserCompanyRole.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCompanyRole.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCompanyRoleWithPatch() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();

        // Update the userCompanyRole using partial update
        UserCompanyRole partialUpdatedUserCompanyRole = new UserCompanyRole();
        partialUpdatedUserCompanyRole.setId(userCompanyRole.getId());

        partialUpdatedUserCompanyRole.roleName(UPDATED_ROLE_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restUserCompanyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCompanyRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCompanyRole))
            )
            .andExpect(status().isOk());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
        UserCompanyRole testUserCompanyRole = userCompanyRoleList.get(userCompanyRoleList.size() - 1);
        assertThat(testUserCompanyRole.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testUserCompanyRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testUserCompanyRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserCompanyRole.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserCompanyRoleWithPatch() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();

        // Update the userCompanyRole using partial update
        UserCompanyRole partialUpdatedUserCompanyRole = new UserCompanyRole();
        partialUpdatedUserCompanyRole.setId(userCompanyRole.getId());

        partialUpdatedUserCompanyRole
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);

        restUserCompanyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCompanyRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCompanyRole))
            )
            .andExpect(status().isOk());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
        UserCompanyRole testUserCompanyRole = userCompanyRoleList.get(userCompanyRoleList.size() - 1);
        assertThat(testUserCompanyRole.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testUserCompanyRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testUserCompanyRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserCompanyRole.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userCompanyRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserCompanyRole() throws Exception {
        int databaseSizeBeforeUpdate = userCompanyRoleRepository.findAll().size();
        userCompanyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCompanyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCompanyRole))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCompanyRole in the database
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserCompanyRole() throws Exception {
        // Initialize the database
        userCompanyRoleRepository.saveAndFlush(userCompanyRole);

        int databaseSizeBeforeDelete = userCompanyRoleRepository.findAll().size();

        // Delete the userCompanyRole
        restUserCompanyRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, userCompanyRole.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserCompanyRole> userCompanyRoleList = userCompanyRoleRepository.findAll();
        assertThat(userCompanyRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
