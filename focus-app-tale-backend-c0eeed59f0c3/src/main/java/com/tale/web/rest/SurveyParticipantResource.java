package com.tale.web.rest;

import com.tale.domain.SurveyParticipant;
import com.tale.repository.SurveyParticipantRepository;
import com.tale.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tale.domain.SurveyParticipant}.
 */
@RestController
@RequestMapping("/api/survey-participants")
@Transactional
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SurveyParticipantResource {

    private final Logger log = LoggerFactory.getLogger(SurveyParticipantResource.class);

    private static final String ENTITY_NAME = "surveyParticipant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyParticipantRepository surveyParticipantRepository;

    public SurveyParticipantResource(SurveyParticipantRepository surveyParticipantRepository) {
        this.surveyParticipantRepository = surveyParticipantRepository;
    }

    /**
     * {@code POST  /survey-participants} : Create a new surveyParticipant.
     *
     * @param surveyParticipant the surveyParticipant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surveyParticipant, or with status {@code 400 (Bad Request)} if the surveyParticipant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SurveyParticipant> createSurveyParticipant(@RequestBody SurveyParticipant surveyParticipant)
        throws URISyntaxException {
        log.debug("REST request to save SurveyParticipant : {}", surveyParticipant);
        if (surveyParticipant.getId() != null) {
            throw new BadRequestAlertException("A new surveyParticipant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurveyParticipant result = surveyParticipantRepository.save(surveyParticipant);
        return ResponseEntity
            .created(new URI("/api/survey-participants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /survey-participants/:id} : Updates an existing surveyParticipant.
     *
     * @param id the id of the surveyParticipant to save.
     * @param surveyParticipant the surveyParticipant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyParticipant,
     * or with status {@code 400 (Bad Request)} if the surveyParticipant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surveyParticipant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurveyParticipant> updateSurveyParticipant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyParticipant surveyParticipant
    ) throws URISyntaxException {
        log.debug("REST request to update SurveyParticipant : {}, {}", id, surveyParticipant);
        if (surveyParticipant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyParticipant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SurveyParticipant result = surveyParticipantRepository.save(surveyParticipant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyParticipant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /survey-participants/:id} : Partial updates given fields of an existing surveyParticipant, field will ignore if it is null
     *
     * @param id the id of the surveyParticipant to save.
     * @param surveyParticipant the surveyParticipant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyParticipant,
     * or with status {@code 400 (Bad Request)} if the surveyParticipant is not valid,
     * or with status {@code 404 (Not Found)} if the surveyParticipant is not found,
     * or with status {@code 500 (Internal Server Error)} if the surveyParticipant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurveyParticipant> partialUpdateSurveyParticipant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyParticipant surveyParticipant
    ) throws URISyntaxException {
        log.debug("REST request to partial update SurveyParticipant partially : {}, {}", id, surveyParticipant);
        if (surveyParticipant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyParticipant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurveyParticipant> result = surveyParticipantRepository
            .findById(surveyParticipant.getId())
            .map(existingSurveyParticipant -> {
                if (surveyParticipant.getParticipantId() != null) {
                    existingSurveyParticipant.setParticipantId(surveyParticipant.getParticipantId());
                }

                return existingSurveyParticipant;
            })
            .map(surveyParticipantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyParticipant.getId().toString())
        );
    }

    /**
     * {@code GET  /survey-participants} : get all the surveyParticipants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveyParticipants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SurveyParticipant>> getAllSurveyParticipants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SurveyParticipants");
        Page<SurveyParticipant> page = surveyParticipantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /survey-participants/:id} : get the "id" surveyParticipant.
     *
     * @param id the id of the surveyParticipant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surveyParticipant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurveyParticipant> getSurveyParticipant(@PathVariable("id") Long id) {
        log.debug("REST request to get SurveyParticipant : {}", id);
        Optional<SurveyParticipant> surveyParticipant = surveyParticipantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(surveyParticipant);
    }

    /**
     * {@code DELETE  /survey-participants/:id} : delete the "id" surveyParticipant.
     *
     * @param id the id of the surveyParticipant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurveyParticipant(@PathVariable("id") Long id) {
        log.debug("REST request to delete SurveyParticipant : {}", id);
        surveyParticipantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
