package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Section;
import com.tale.repository.SectionRepository;
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
 * Integration tests for the {@link SectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SectionResourceIT {

    private static final Integer DEFAULT_SECTION_ID = 1;
    private static final Integer UPDATED_SECTION_ID = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectionMockMvc;

    private Section section;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section().sectionId(DEFAULT_SECTION_ID).title(DEFAULT_TITLE);
        return section;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createUpdatedEntity(EntityManager em) {
        Section section = new Section().sectionId(UPDATED_SECTION_ID).title(UPDATED_TITLE);
        return section;
    }

    @BeforeEach
    public void initTest() {
        section = createEntity(em);
    }

    @Test
    @Transactional
    void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();
        // Create the Section
        restSectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testSection.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createSectionWithExistingId() throws Exception {
        // Create the Section with an existing ID
        section.setId(1L);

        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        Section updatedSection = sectionRepository.findById(section.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSection are not directly saved in db
        em.detach(updatedSection);
        updatedSection.sectionId(UPDATED_SECTION_ID).title(UPDATED_TITLE);

        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSection.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testSection.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, section.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.sectionId(UPDATED_SECTION_ID).title(UPDATED_TITLE);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testSection.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.sectionId(UPDATED_SECTION_ID).title(UPDATED_TITLE);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testSection.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, section.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Delete the section
        restSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, section.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
