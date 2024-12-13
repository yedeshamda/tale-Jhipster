package com.tale.web.rest;

import com.tale.domain.Survey;
import com.tale.repository.SurveyRepository;
import com.tale.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tale.domain.Survey}.
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/surveys")
@Transactional
//@CrossOrigin(origins = "http://localhost:3000")

//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SurveyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyRepository surveyRepository;

    public SurveyResource(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @PostMapping("")
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) throws URISyntaxException {
        log.debug("REST request to save Survey : {}", survey);
        if (survey.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // If image is Base64 encoded, decode it
        if (survey.getImage() != null && survey.getImage().length == 0) {
            byte[] decodedImage = Base64.getDecoder().decode(survey.getImage());
            survey.setImage(decodedImage);
        }

        Survey result = surveyRepository.save(survey);
        return ResponseEntity
            .created(new URI("/api/surveys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable(value = "id", required = false) final Long id, @RequestBody Survey survey)
        throws URISyntaxException {
        log.debug("REST request to update Survey : {}, {}", id, survey);
        if (survey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, survey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Survey result = surveyRepository.save(survey);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, survey.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Survey> partialUpdateSurvey(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Survey survey
    ) throws URISyntaxException {
        log.debug("REST request to partial update Survey partially : {}, {}", id, survey);
        if (survey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, survey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Survey> result = surveyRepository
            .findById(survey.getId())
            .map(existingSurvey -> {
                if (survey.getSurveyId() != null) {
                    existingSurvey.setSurveyId(survey.getSurveyId());
                }
                // Update other fields similarly
                return existingSurvey;
            })
            .map(surveyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, survey.getId().toString())
        );
    }

    @CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend origin
    @GetMapping("")
    public ResponseEntity<List<Survey>> getAllSurveys(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Surveys");
        Page<Survey> page;
        if (eagerload) {
            page = surveyRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = surveyRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable("id") Long id) {
        log.debug("REST request to get Survey : {}", id);
        Optional<Survey> survey = surveyRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(survey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") Long id) {
        log.debug("REST request to delete Survey : {}", id);
        surveyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
