package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Responses;
import com.tale.repository.ResponsesRepository;
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
 * Integration tests for the {@link ResponsesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResponsesResourceIT {

    private static final Integer DEFAULT_RESPONSE_ID = 1;
    private static final Integer UPDATED_RESPONSE_ID = 2;

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResponsesRepository responsesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponsesMockMvc;

    private Responses responses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responses createEntity(EntityManager em) {
        Responses responses = new Responses().responseId(DEFAULT_RESPONSE_ID).answer(DEFAULT_ANSWER);
        return responses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responses createUpdatedEntity(EntityManager em) {
        Responses responses = new Responses().responseId(UPDATED_RESPONSE_ID).answer(UPDATED_ANSWER);
        return responses;
    }

    @BeforeEach
    public void initTest() {
        responses = createEntity(em);
    }

    @Test
    @Transactional
    void createResponses() throws Exception {
        int databaseSizeBeforeCreate = responsesRepository.findAll().size();
        // Create the Responses
        restResponsesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isCreated());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeCreate + 1);
        Responses testResponses = responsesList.get(responsesList.size() - 1);
        assertThat(testResponses.getResponseId()).isEqualTo(DEFAULT_RESPONSE_ID);
        assertThat(testResponses.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    void createResponsesWithExistingId() throws Exception {
        // Create the Responses with an existing ID
        responses.setId(1L);

        int databaseSizeBeforeCreate = responsesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResponses() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        // Get all the responsesList
        restResponsesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responses.getId().intValue())))
            .andExpect(jsonPath("$.[*].responseId").value(hasItem(DEFAULT_RESPONSE_ID)))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)));
    }

    @Test
    @Transactional
    void getResponses() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        // Get the responses
        restResponsesMockMvc
            .perform(get(ENTITY_API_URL_ID, responses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responses.getId().intValue()))
            .andExpect(jsonPath("$.responseId").value(DEFAULT_RESPONSE_ID))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER));
    }

    @Test
    @Transactional
    void getNonExistingResponses() throws Exception {
        // Get the responses
        restResponsesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponses() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();

        // Update the responses
        Responses updatedResponses = responsesRepository.findById(responses.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResponses are not directly saved in db
        em.detach(updatedResponses);
        updatedResponses.responseId(UPDATED_RESPONSE_ID).answer(UPDATED_ANSWER);

        restResponsesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResponses.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResponses))
            )
            .andExpect(status().isOk());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
        Responses testResponses = responsesList.get(responsesList.size() - 1);
        assertThat(testResponses.getResponseId()).isEqualTo(UPDATED_RESPONSE_ID);
        assertThat(testResponses.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void putNonExistingResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responses.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponsesWithPatch() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();

        // Update the responses using partial update
        Responses partialUpdatedResponses = new Responses();
        partialUpdatedResponses.setId(responses.getId());

        partialUpdatedResponses.responseId(UPDATED_RESPONSE_ID).answer(UPDATED_ANSWER);

        restResponsesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponses.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponses))
            )
            .andExpect(status().isOk());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
        Responses testResponses = responsesList.get(responsesList.size() - 1);
        assertThat(testResponses.getResponseId()).isEqualTo(UPDATED_RESPONSE_ID);
        assertThat(testResponses.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void fullUpdateResponsesWithPatch() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();

        // Update the responses using partial update
        Responses partialUpdatedResponses = new Responses();
        partialUpdatedResponses.setId(responses.getId());

        partialUpdatedResponses.responseId(UPDATED_RESPONSE_ID).answer(UPDATED_ANSWER);

        restResponsesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponses.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponses))
            )
            .andExpect(status().isOk());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
        Responses testResponses = responsesList.get(responsesList.size() - 1);
        assertThat(testResponses.getResponseId()).isEqualTo(UPDATED_RESPONSE_ID);
        assertThat(testResponses.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void patchNonExistingResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responses.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponses() throws Exception {
        int databaseSizeBeforeUpdate = responsesRepository.findAll().size();
        responses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Responses in the database
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponses() throws Exception {
        // Initialize the database
        responsesRepository.saveAndFlush(responses);

        int databaseSizeBeforeDelete = responsesRepository.findAll().size();

        // Delete the responses
        restResponsesMockMvc
            .perform(delete(ENTITY_API_URL_ID, responses.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Responses> responsesList = responsesRepository.findAll();
        assertThat(responsesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
