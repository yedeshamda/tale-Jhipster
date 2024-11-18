package com.tale.web.rest;

import static com.tale.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.FocusGroup;
import com.tale.repository.FocusGroupRepository;
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
 * Integration tests for the {@link FocusGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FocusGroupResourceIT {

    private static final Integer DEFAULT_FOCUS_GROUP_ID = 1;
    private static final Integer UPDATED_FOCUS_GROUP_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_USER_CATEGORY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/focus-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FocusGroupRepository focusGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFocusGroupMockMvc;

    private FocusGroup focusGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FocusGroup createEntity(EntityManager em) {
        FocusGroup focusGroup = new FocusGroup()
            .focusGroupId(DEFAULT_FOCUS_GROUP_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .userCategory(DEFAULT_USER_CATEGORY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS);
        return focusGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FocusGroup createUpdatedEntity(EntityManager em) {
        FocusGroup focusGroup = new FocusGroup()
            .focusGroupId(UPDATED_FOCUS_GROUP_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userCategory(UPDATED_USER_CATEGORY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);
        return focusGroup;
    }

    @BeforeEach
    public void initTest() {
        focusGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createFocusGroup() throws Exception {
        int databaseSizeBeforeCreate = focusGroupRepository.findAll().size();
        // Create the FocusGroup
        restFocusGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isCreated());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeCreate + 1);
        FocusGroup testFocusGroup = focusGroupList.get(focusGroupList.size() - 1);
        assertThat(testFocusGroup.getFocusGroupId()).isEqualTo(DEFAULT_FOCUS_GROUP_ID);
        assertThat(testFocusGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFocusGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFocusGroup.getUserCategory()).isEqualTo(DEFAULT_USER_CATEGORY);
        assertThat(testFocusGroup.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFocusGroup.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFocusGroup.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFocusGroupWithExistingId() throws Exception {
        // Create the FocusGroup with an existing ID
        focusGroup.setId(1L);

        int databaseSizeBeforeCreate = focusGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFocusGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFocusGroups() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        // Get all the focusGroupList
        restFocusGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(focusGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].focusGroupId").value(hasItem(DEFAULT_FOCUS_GROUP_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userCategory").value(hasItem(DEFAULT_USER_CATEGORY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getFocusGroup() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        // Get the focusGroup
        restFocusGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, focusGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(focusGroup.getId().intValue()))
            .andExpect(jsonPath("$.focusGroupId").value(DEFAULT_FOCUS_GROUP_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.userCategory").value(DEFAULT_USER_CATEGORY))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingFocusGroup() throws Exception {
        // Get the focusGroup
        restFocusGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFocusGroup() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();

        // Update the focusGroup
        FocusGroup updatedFocusGroup = focusGroupRepository.findById(focusGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFocusGroup are not directly saved in db
        em.detach(updatedFocusGroup);
        updatedFocusGroup
            .focusGroupId(UPDATED_FOCUS_GROUP_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userCategory(UPDATED_USER_CATEGORY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restFocusGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFocusGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFocusGroup))
            )
            .andExpect(status().isOk());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
        FocusGroup testFocusGroup = focusGroupList.get(focusGroupList.size() - 1);
        assertThat(testFocusGroup.getFocusGroupId()).isEqualTo(UPDATED_FOCUS_GROUP_ID);
        assertThat(testFocusGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFocusGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFocusGroup.getUserCategory()).isEqualTo(UPDATED_USER_CATEGORY);
        assertThat(testFocusGroup.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFocusGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFocusGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, focusGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFocusGroupWithPatch() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();

        // Update the focusGroup using partial update
        FocusGroup partialUpdatedFocusGroup = new FocusGroup();
        partialUpdatedFocusGroup.setId(focusGroup.getId());

        partialUpdatedFocusGroup
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userCategory(UPDATED_USER_CATEGORY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restFocusGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFocusGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFocusGroup))
            )
            .andExpect(status().isOk());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
        FocusGroup testFocusGroup = focusGroupList.get(focusGroupList.size() - 1);
        assertThat(testFocusGroup.getFocusGroupId()).isEqualTo(DEFAULT_FOCUS_GROUP_ID);
        assertThat(testFocusGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFocusGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFocusGroup.getUserCategory()).isEqualTo(UPDATED_USER_CATEGORY);
        assertThat(testFocusGroup.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFocusGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFocusGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFocusGroupWithPatch() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();

        // Update the focusGroup using partial update
        FocusGroup partialUpdatedFocusGroup = new FocusGroup();
        partialUpdatedFocusGroup.setId(focusGroup.getId());

        partialUpdatedFocusGroup
            .focusGroupId(UPDATED_FOCUS_GROUP_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userCategory(UPDATED_USER_CATEGORY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restFocusGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFocusGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFocusGroup))
            )
            .andExpect(status().isOk());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
        FocusGroup testFocusGroup = focusGroupList.get(focusGroupList.size() - 1);
        assertThat(testFocusGroup.getFocusGroupId()).isEqualTo(UPDATED_FOCUS_GROUP_ID);
        assertThat(testFocusGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFocusGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFocusGroup.getUserCategory()).isEqualTo(UPDATED_USER_CATEGORY);
        assertThat(testFocusGroup.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFocusGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFocusGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, focusGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFocusGroup() throws Exception {
        int databaseSizeBeforeUpdate = focusGroupRepository.findAll().size();
        focusGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFocusGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(focusGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FocusGroup in the database
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFocusGroup() throws Exception {
        // Initialize the database
        focusGroupRepository.saveAndFlush(focusGroup);

        int databaseSizeBeforeDelete = focusGroupRepository.findAll().size();

        // Delete the focusGroup
        restFocusGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, focusGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FocusGroup> focusGroupList = focusGroupRepository.findAll();
        assertThat(focusGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
