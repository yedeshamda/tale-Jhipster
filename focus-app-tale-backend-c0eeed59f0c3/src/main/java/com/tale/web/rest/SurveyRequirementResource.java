package com.tale.web.rest;

import com.tale.domain.SurveyRequirement;
import com.tale.repository.SurveyRequirementRepository;
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
 * REST controller for managing {@link com.tale.domain.SurveyRequirement}.
 */
@RestController
@RequestMapping("/api/survey-requirements")
@Transactional
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SurveyRequirementResource {

    private final Logger log = LoggerFactory.getLogger(SurveyRequirementResource.class);

    private static final String ENTITY_NAME = "surveyRequirement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyRequirementRepository surveyRequirementRepository;

    public SurveyRequirementResource(SurveyRequirementRepository surveyRequirementRepository) {
        this.surveyRequirementRepository = surveyRequirementRepository;
    }

    /**
     * {@code POST  /survey-requirements} : Create a new surveyRequirement.
     *
     * @param surveyRequirement the surveyRequirement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surveyRequirement, or with status {@code 400 (Bad Request)} if the surveyRequirement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SurveyRequirement> createSurveyRequirement(@RequestBody SurveyRequirement surveyRequirement)
        throws URISyntaxException {
        log.debug("REST request to save SurveyRequirement : {}", surveyRequirement);
        if (surveyRequirement.getId() != null) {
            throw new BadRequestAlertException("A new surveyRequirement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurveyRequirement result = surveyRequirementRepository.save(surveyRequirement);
        return ResponseEntity
            .created(new URI("/api/survey-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /survey-requirements/:id} : Updates an existing surveyRequirement.
     *
     * @param id the id of the surveyRequirement to save.
     * @param surveyRequirement the surveyRequirement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyRequirement,
     * or with status {@code 400 (Bad Request)} if the surveyRequirement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surveyRequirement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurveyRequirement> updateSurveyRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyRequirement surveyRequirement
    ) throws URISyntaxException {
        log.debug("REST request to update SurveyRequirement : {}, {}", id, surveyRequirement);
        if (surveyRequirement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyRequirement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SurveyRequirement result = surveyRequirementRepository.save(surveyRequirement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyRequirement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /survey-requirements/:id} : Partial updates given fields of an existing surveyRequirement, field will ignore if it is null
     *
     * @param id the id of the surveyRequirement to save.
     * @param surveyRequirement the surveyRequirement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyRequirement,
     * or with status {@code 400 (Bad Request)} if the surveyRequirement is not valid,
     * or with status {@code 404 (Not Found)} if the surveyRequirement is not found,
     * or with status {@code 500 (Internal Server Error)} if the surveyRequirement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurveyRequirement> partialUpdateSurveyRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyRequirement surveyRequirement
    ) throws URISyntaxException {
        log.debug("REST request to partial update SurveyRequirement partially : {}, {}", id, surveyRequirement);
        if (surveyRequirement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyRequirement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurveyRequirement> result = surveyRequirementRepository
            .findById(surveyRequirement.getId())
            .map(existingSurveyRequirement -> {
                if (surveyRequirement.getRequirementId() != null) {
                    existingSurveyRequirement.setRequirementId(surveyRequirement.getRequirementId());
                }
                if (surveyRequirement.getRequirementDescription() != null) {
                    existingSurveyRequirement.setRequirementDescription(surveyRequirement.getRequirementDescription());
                }
                if (surveyRequirement.getSurveyId() != null) {
                    existingSurveyRequirement.setSurveyId(surveyRequirement.getSurveyId());
                }
                if (surveyRequirement.getCreatedByUserCompanyId() != null) {
                    existingSurveyRequirement.setCreatedByUserCompanyId(surveyRequirement.getCreatedByUserCompanyId());
                }
                if (surveyRequirement.getCreatedDate() != null) {
                    existingSurveyRequirement.setCreatedDate(surveyRequirement.getCreatedDate());
                }

                return existingSurveyRequirement;
            })
            .map(surveyRequirementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyRequirement.getId().toString())
        );
    }

    /**
     * {@code GET  /survey-requirements} : get all the surveyRequirements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveyRequirements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SurveyRequirement>> getAllSurveyRequirements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SurveyRequirements");
        Page<SurveyRequirement> page = surveyRequirementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /survey-requirements/:id} : get the "id" surveyRequirement.
     *
     * @param id the id of the surveyRequirement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surveyRequirement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurveyRequirement> getSurveyRequirement(@PathVariable("id") Long id) {
        log.debug("REST request to get SurveyRequirement : {}", id);
        Optional<SurveyRequirement> surveyRequirement = surveyRequirementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(surveyRequirement);
    }

    /**
     * {@code DELETE  /survey-requirements/:id} : delete the "id" surveyRequirement.
     *
     * @param id the id of the surveyRequirement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurveyRequirement(@PathVariable("id") Long id) {
        log.debug("REST request to delete SurveyRequirement : {}", id);
        surveyRequirementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
