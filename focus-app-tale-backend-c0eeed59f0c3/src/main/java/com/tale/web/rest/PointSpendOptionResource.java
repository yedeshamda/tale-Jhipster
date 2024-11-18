package com.tale.web.rest;

import com.tale.domain.PointSpendOption;
import com.tale.repository.PointSpendOptionRepository;
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
 * REST controller for managing {@link com.tale.domain.PointSpendOption}.
 */
@RestController
@RequestMapping("/api/point-spend-options")
@Transactional
public class PointSpendOptionResource {

    private final Logger log = LoggerFactory.getLogger(PointSpendOptionResource.class);

    private static final String ENTITY_NAME = "pointSpendOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointSpendOptionRepository pointSpendOptionRepository;

    public PointSpendOptionResource(PointSpendOptionRepository pointSpendOptionRepository) {
        this.pointSpendOptionRepository = pointSpendOptionRepository;
    }

    /**
     * {@code POST  /point-spend-options} : Create a new pointSpendOption.
     *
     * @param pointSpendOption the pointSpendOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointSpendOption, or with status {@code 400 (Bad Request)} if the pointSpendOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PointSpendOption> createPointSpendOption(@RequestBody PointSpendOption pointSpendOption)
        throws URISyntaxException {
        log.debug("REST request to save PointSpendOption : {}", pointSpendOption);
        if (pointSpendOption.getId() != null) {
            throw new BadRequestAlertException("A new pointSpendOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointSpendOption result = pointSpendOptionRepository.save(pointSpendOption);
        return ResponseEntity
            .created(new URI("/api/point-spend-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-spend-options/:id} : Updates an existing pointSpendOption.
     *
     * @param id the id of the pointSpendOption to save.
     * @param pointSpendOption the pointSpendOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointSpendOption,
     * or with status {@code 400 (Bad Request)} if the pointSpendOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointSpendOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointSpendOption> updatePointSpendOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointSpendOption pointSpendOption
    ) throws URISyntaxException {
        log.debug("REST request to update PointSpendOption : {}, {}", id, pointSpendOption);
        if (pointSpendOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointSpendOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointSpendOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointSpendOption result = pointSpendOptionRepository.save(pointSpendOption);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointSpendOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-spend-options/:id} : Partial updates given fields of an existing pointSpendOption, field will ignore if it is null
     *
     * @param id the id of the pointSpendOption to save.
     * @param pointSpendOption the pointSpendOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointSpendOption,
     * or with status {@code 400 (Bad Request)} if the pointSpendOption is not valid,
     * or with status {@code 404 (Not Found)} if the pointSpendOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointSpendOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointSpendOption> partialUpdatePointSpendOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointSpendOption pointSpendOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointSpendOption partially : {}, {}", id, pointSpendOption);
        if (pointSpendOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointSpendOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointSpendOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointSpendOption> result = pointSpendOptionRepository
            .findById(pointSpendOption.getId())
            .map(existingPointSpendOption -> {
                if (pointSpendOption.getRedemptionOptionId() != null) {
                    existingPointSpendOption.setRedemptionOptionId(pointSpendOption.getRedemptionOptionId());
                }
                if (pointSpendOption.getDescription() != null) {
                    existingPointSpendOption.setDescription(pointSpendOption.getDescription());
                }
                if (pointSpendOption.getPointsRequired() != null) {
                    existingPointSpendOption.setPointsRequired(pointSpendOption.getPointsRequired());
                }
                if (pointSpendOption.getAvailableQuantity() != null) {
                    existingPointSpendOption.setAvailableQuantity(pointSpendOption.getAvailableQuantity());
                }
                if (pointSpendOption.getExpirationDate() != null) {
                    existingPointSpendOption.setExpirationDate(pointSpendOption.getExpirationDate());
                }

                return existingPointSpendOption;
            })
            .map(pointSpendOptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointSpendOption.getId().toString())
        );
    }

    /**
     * {@code GET  /point-spend-options} : get all the pointSpendOptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointSpendOptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PointSpendOption>> getAllPointSpendOptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PointSpendOptions");
        Page<PointSpendOption> page = pointSpendOptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /point-spend-options/:id} : get the "id" pointSpendOption.
     *
     * @param id the id of the pointSpendOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointSpendOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointSpendOption> getPointSpendOption(@PathVariable("id") Long id) {
        log.debug("REST request to get PointSpendOption : {}", id);
        Optional<PointSpendOption> pointSpendOption = pointSpendOptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pointSpendOption);
    }

    /**
     * {@code DELETE  /point-spend-options/:id} : delete the "id" pointSpendOption.
     *
     * @param id the id of the pointSpendOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointSpendOption(@PathVariable("id") Long id) {
        log.debug("REST request to delete PointSpendOption : {}", id);
        pointSpendOptionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
