package com.tale.web.rest;

import com.tale.domain.Analyticss;
import com.tale.repository.AnalyticssRepository;
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
 * REST controller for managing {@link com.tale.domain.Analyticss}.
 */
@RestController
@RequestMapping("/api/analyticsses")
@Transactional
public class AnalyticssResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticssResource.class);

    private static final String ENTITY_NAME = "analyticss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyticssRepository analyticssRepository;

    public AnalyticssResource(AnalyticssRepository analyticssRepository) {
        this.analyticssRepository = analyticssRepository;
    }

    /**
     * {@code POST  /analyticsses} : Create a new analyticss.
     *
     * @param analyticss the analyticss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyticss, or with status {@code 400 (Bad Request)} if the analyticss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Analyticss> createAnalyticss(@RequestBody Analyticss analyticss) throws URISyntaxException {
        log.debug("REST request to save Analyticss : {}", analyticss);
        if (analyticss.getId() != null) {
            throw new BadRequestAlertException("A new analyticss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Analyticss result = analyticssRepository.save(analyticss);
        return ResponseEntity
            .created(new URI("/api/analyticsses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analyticsses/:id} : Updates an existing analyticss.
     *
     * @param id the id of the analyticss to save.
     * @param analyticss the analyticss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyticss,
     * or with status {@code 400 (Bad Request)} if the analyticss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyticss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Analyticss> updateAnalyticss(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Analyticss analyticss
    ) throws URISyntaxException {
        log.debug("REST request to update Analyticss : {}, {}", id, analyticss);
        if (analyticss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyticss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyticssRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Analyticss result = analyticssRepository.save(analyticss);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, analyticss.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /analyticsses/:id} : Partial updates given fields of an existing analyticss, field will ignore if it is null
     *
     * @param id the id of the analyticss to save.
     * @param analyticss the analyticss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyticss,
     * or with status {@code 400 (Bad Request)} if the analyticss is not valid,
     * or with status {@code 404 (Not Found)} if the analyticss is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyticss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Analyticss> partialUpdateAnalyticss(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Analyticss analyticss
    ) throws URISyntaxException {
        log.debug("REST request to partial update Analyticss partially : {}, {}", id, analyticss);
        if (analyticss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyticss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyticssRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Analyticss> result = analyticssRepository
            .findById(analyticss.getId())
            .map(existingAnalyticss -> {
                if (analyticss.getAnalyticsId() != null) {
                    existingAnalyticss.setAnalyticsId(analyticss.getAnalyticsId());
                }
                if (analyticss.getIsready() != null) {
                    existingAnalyticss.setIsready(analyticss.getIsready());
                }
                if (analyticss.getInsights() != null) {
                    existingAnalyticss.setInsights(analyticss.getInsights());
                }

                return existingAnalyticss;
            })
            .map(analyticssRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, analyticss.getId().toString())
        );
    }

    /**
     * {@code GET  /analyticsses} : get all the analyticsses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analyticsses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Analyticss>> getAllAnalyticsses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Analyticsses");
        Page<Analyticss> page = analyticssRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /analyticsses/:id} : get the "id" analyticss.
     *
     * @param id the id of the analyticss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyticss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Analyticss> getAnalyticss(@PathVariable("id") Long id) {
        log.debug("REST request to get Analyticss : {}", id);
        Optional<Analyticss> analyticss = analyticssRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analyticss);
    }

    /**
     * {@code DELETE  /analyticsses/:id} : delete the "id" analyticss.
     *
     * @param id the id of the analyticss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalyticss(@PathVariable("id") Long id) {
        log.debug("REST request to delete Analyticss : {}", id);
        analyticssRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
