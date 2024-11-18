package com.tale.web.rest;

import com.tale.domain.PointTransaction;
import com.tale.repository.PointTransactionRepository;
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
 * REST controller for managing {@link com.tale.domain.PointTransaction}.
 */
@RestController
@RequestMapping("/api/point-transactions")
@Transactional
public class PointTransactionResource {

    private final Logger log = LoggerFactory.getLogger(PointTransactionResource.class);

    private static final String ENTITY_NAME = "pointTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointTransactionRepository pointTransactionRepository;

    public PointTransactionResource(PointTransactionRepository pointTransactionRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
    }

    /**
     * {@code POST  /point-transactions} : Create a new pointTransaction.
     *
     * @param pointTransaction the pointTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointTransaction, or with status {@code 400 (Bad Request)} if the pointTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PointTransaction> createPointTransaction(@RequestBody PointTransaction pointTransaction)
        throws URISyntaxException {
        log.debug("REST request to save PointTransaction : {}", pointTransaction);
        if (pointTransaction.getId() != null) {
            throw new BadRequestAlertException("A new pointTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointTransaction result = pointTransactionRepository.save(pointTransaction);
        return ResponseEntity
            .created(new URI("/api/point-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-transactions/:id} : Updates an existing pointTransaction.
     *
     * @param id the id of the pointTransaction to save.
     * @param pointTransaction the pointTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointTransaction,
     * or with status {@code 400 (Bad Request)} if the pointTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointTransaction> updatePointTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointTransaction pointTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update PointTransaction : {}, {}", id, pointTransaction);
        if (pointTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointTransaction result = pointTransactionRepository.save(pointTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /point-transactions/:id} : Partial updates given fields of an existing pointTransaction, field will ignore if it is null
     *
     * @param id the id of the pointTransaction to save.
     * @param pointTransaction the pointTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointTransaction,
     * or with status {@code 400 (Bad Request)} if the pointTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the pointTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointTransaction> partialUpdatePointTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PointTransaction pointTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update PointTransaction partially : {}, {}", id, pointTransaction);
        if (pointTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointTransaction> result = pointTransactionRepository
            .findById(pointTransaction.getId())
            .map(existingPointTransaction -> {
                if (pointTransaction.getTransactionId() != null) {
                    existingPointTransaction.setTransactionId(pointTransaction.getTransactionId());
                }
                if (pointTransaction.getDescription() != null) {
                    existingPointTransaction.setDescription(pointTransaction.getDescription());
                }
                if (pointTransaction.getPoints() != null) {
                    existingPointTransaction.setPoints(pointTransaction.getPoints());
                }
                if (pointTransaction.getTransactionDate() != null) {
                    existingPointTransaction.setTransactionDate(pointTransaction.getTransactionDate());
                }
                if (pointTransaction.getUserId() != null) {
                    existingPointTransaction.setUserId(pointTransaction.getUserId());
                }

                return existingPointTransaction;
            })
            .map(pointTransactionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pointTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /point-transactions} : get all the pointTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PointTransaction>> getAllPointTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PointTransactions");
        Page<PointTransaction> page = pointTransactionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /point-transactions/:id} : get the "id" pointTransaction.
     *
     * @param id the id of the pointTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointTransaction> getPointTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to get PointTransaction : {}", id);
        Optional<PointTransaction> pointTransaction = pointTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pointTransaction);
    }

    /**
     * {@code DELETE  /point-transactions/:id} : delete the "id" pointTransaction.
     *
     * @param id the id of the pointTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to delete PointTransaction : {}", id);
        pointTransactionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
