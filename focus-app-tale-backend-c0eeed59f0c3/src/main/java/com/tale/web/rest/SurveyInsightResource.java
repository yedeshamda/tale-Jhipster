package com.tale.web.rest;

import com.tale.domain.SurveyInsight;
import com.tale.repository.SurveyInsightRepository;
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
 * REST controller for managing {@link com.tale.domain.SurveyInsight}.
 */
@RestController
@RequestMapping("/api/survey-insights")
@Transactional
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SurveyInsightResource {

    private final Logger log = LoggerFactory.getLogger(SurveyInsightResource.class);

    private static final String ENTITY_NAME = "surveyInsight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyInsightRepository surveyInsightRepository;

    public SurveyInsightResource(SurveyInsightRepository surveyInsightRepository) {
        this.surveyInsightRepository = surveyInsightRepository;
    }

    /**
     * {@code POST  /survey-insights} : Create a new surveyInsight.
     *
     * @param surveyInsight the surveyInsight to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surveyInsight, or with status {@code 400 (Bad Request)} if the surveyInsight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SurveyInsight> createSurveyInsight(@RequestBody SurveyInsight surveyInsight) throws URISyntaxException {
        log.debug("REST request to save SurveyInsight : {}", surveyInsight);
        if (surveyInsight.getId() != null) {
            throw new BadRequestAlertException("A new surveyInsight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurveyInsight result = surveyInsightRepository.save(surveyInsight);
        return ResponseEntity
            .created(new URI("/api/survey-insights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /survey-insights/:id} : Updates an existing surveyInsight.
     *
     * @param id the id of the surveyInsight to save.
     * @param surveyInsight the surveyInsight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyInsight,
     * or with status {@code 400 (Bad Request)} if the surveyInsight is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surveyInsight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurveyInsight> updateSurveyInsight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyInsight surveyInsight
    ) throws URISyntaxException {
        log.debug("REST request to update SurveyInsight : {}, {}", id, surveyInsight);
        if (surveyInsight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyInsight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyInsightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SurveyInsight result = surveyInsightRepository.save(surveyInsight);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyInsight.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /survey-insights/:id} : Partial updates given fields of an existing surveyInsight, field will ignore if it is null
     *
     * @param id the id of the surveyInsight to save.
     * @param surveyInsight the surveyInsight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyInsight,
     * or with status {@code 400 (Bad Request)} if the surveyInsight is not valid,
     * or with status {@code 404 (Not Found)} if the surveyInsight is not found,
     * or with status {@code 500 (Internal Server Error)} if the surveyInsight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurveyInsight> partialUpdateSurveyInsight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveyInsight surveyInsight
    ) throws URISyntaxException {
        log.debug("REST request to partial update SurveyInsight partially : {}, {}", id, surveyInsight);
        if (surveyInsight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyInsight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyInsightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurveyInsight> result = surveyInsightRepository
            .findById(surveyInsight.getId())
            .map(existingSurveyInsight -> {
                if (surveyInsight.getInsightId() != null) {
                    existingSurveyInsight.setInsightId(surveyInsight.getInsightId());
                }
                if (surveyInsight.getInsights() != null) {
                    existingSurveyInsight.setInsights(surveyInsight.getInsights());
                }
                if (surveyInsight.getSurveyId() != null) {
                    existingSurveyInsight.setSurveyId(surveyInsight.getSurveyId());
                }
                if (surveyInsight.getCreatedByUserId() != null) {
                    existingSurveyInsight.setCreatedByUserId(surveyInsight.getCreatedByUserId());
                }
                if (surveyInsight.getCreatedDate() != null) {
                    existingSurveyInsight.setCreatedDate(surveyInsight.getCreatedDate());
                }

                return existingSurveyInsight;
            })
            .map(surveyInsightRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, surveyInsight.getId().toString())
        );
    }

    /**
     * {@code GET  /survey-insights} : get all the surveyInsights.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveyInsights in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SurveyInsight>> getAllSurveyInsights(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SurveyInsights");
        Page<SurveyInsight> page = surveyInsightRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /survey-insights/:id} : get the "id" surveyInsight.
     *
     * @param id the id of the surveyInsight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surveyInsight, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurveyInsight> getSurveyInsight(@PathVariable("id") Long id) {
        log.debug("REST request to get SurveyInsight : {}", id);
        Optional<SurveyInsight> surveyInsight = surveyInsightRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(surveyInsight);
    }

    /**
     * {@code DELETE  /survey-insights/:id} : delete the "id" surveyInsight.
     *
     * @param id the id of the surveyInsight to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurveyInsight(@PathVariable("id") Long id) {
        log.debug("REST request to delete SurveyInsight : {}", id);
        surveyInsightRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
