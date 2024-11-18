package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.AdminUser;
import com.tale.repository.AdminUserRepository;
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
 * Integration tests for the {@link AdminUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminUserResourceIT {

    private static final Integer DEFAULT_ADMIN_ID = 1;
    private static final Integer UPDATED_ADMIN_ID = 2;

    private static final String ENTITY_API_URL = "/api/admin-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminUserMockMvc;

    private AdminUser adminUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminUser createEntity(EntityManager em) {
        AdminUser adminUser = new AdminUser().adminId(DEFAULT_ADMIN_ID);
        return adminUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminUser createUpdatedEntity(EntityManager em) {
        AdminUser adminUser = new AdminUser().adminId(UPDATED_ADMIN_ID);
        return adminUser;
    }

    @BeforeEach
    public void initTest() {
        adminUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAdminUser() throws Exception {
        int databaseSizeBeforeCreate = adminUserRepository.findAll().size();
        // Create the AdminUser
        restAdminUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isCreated());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeCreate + 1);
        AdminUser testAdminUser = adminUserList.get(adminUserList.size() - 1);
        assertThat(testAdminUser.getAdminId()).isEqualTo(DEFAULT_ADMIN_ID);
    }

    @Test
    @Transactional
    void createAdminUserWithExistingId() throws Exception {
        // Create the AdminUser with an existing ID
        adminUser.setId(1L);

        int databaseSizeBeforeCreate = adminUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdminUsers() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        // Get all the adminUserList
        restAdminUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].adminId").value(hasItem(DEFAULT_ADMIN_ID)));
    }

    @Test
    @Transactional
    void getAdminUser() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        // Get the adminUser
        restAdminUserMockMvc
            .perform(get(ENTITY_API_URL_ID, adminUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminUser.getId().intValue()))
            .andExpect(jsonPath("$.adminId").value(DEFAULT_ADMIN_ID));
    }

    @Test
    @Transactional
    void getNonExistingAdminUser() throws Exception {
        // Get the adminUser
        restAdminUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminUser() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();

        // Update the adminUser
        AdminUser updatedAdminUser = adminUserRepository.findById(adminUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminUser are not directly saved in db
        em.detach(updatedAdminUser);
        updatedAdminUser.adminId(UPDATED_ADMIN_ID);

        restAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdminUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdminUser))
            )
            .andExpect(status().isOk());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
        AdminUser testAdminUser = adminUserList.get(adminUserList.size() - 1);
        assertThat(testAdminUser.getAdminId()).isEqualTo(UPDATED_ADMIN_ID);
    }

    @Test
    @Transactional
    void putNonExistingAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminUserWithPatch() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();

        // Update the adminUser using partial update
        AdminUser partialUpdatedAdminUser = new AdminUser();
        partialUpdatedAdminUser.setId(adminUser.getId());

        partialUpdatedAdminUser.adminId(UPDATED_ADMIN_ID);

        restAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminUser))
            )
            .andExpect(status().isOk());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
        AdminUser testAdminUser = adminUserList.get(adminUserList.size() - 1);
        assertThat(testAdminUser.getAdminId()).isEqualTo(UPDATED_ADMIN_ID);
    }

    @Test
    @Transactional
    void fullUpdateAdminUserWithPatch() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();

        // Update the adminUser using partial update
        AdminUser partialUpdatedAdminUser = new AdminUser();
        partialUpdatedAdminUser.setId(adminUser.getId());

        partialUpdatedAdminUser.adminId(UPDATED_ADMIN_ID);

        restAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminUser))
            )
            .andExpect(status().isOk());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
        AdminUser testAdminUser = adminUserList.get(adminUserList.size() - 1);
        assertThat(testAdminUser.getAdminId()).isEqualTo(UPDATED_ADMIN_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminUser() throws Exception {
        int databaseSizeBeforeUpdate = adminUserRepository.findAll().size();
        adminUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminUser in the database
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminUser() throws Exception {
        // Initialize the database
        adminUserRepository.saveAndFlush(adminUser);

        int databaseSizeBeforeDelete = adminUserRepository.findAll().size();

        // Delete the adminUser
        restAdminUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        assertThat(adminUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
