package com.tale.web.rest;

import com.tale.domain.OurDatabases;
import com.tale.repository.OurDatabasesRepository;
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
 * REST controller for managing {@link com.tale.domain.OurDatabases}.
 */
@RestController
@RequestMapping("/api/our-databases")
@Transactional
public class OurDatabasesResource {

    private final Logger log = LoggerFactory.getLogger(OurDatabasesResource.class);

    private static final String ENTITY_NAME = "ourDatabases";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OurDatabasesRepository ourDatabasesRepository;

    public OurDatabasesResource(OurDatabasesRepository ourDatabasesRepository) {
        this.ourDatabasesRepository = ourDatabasesRepository;
    }

    /**
     * {@code POST  /our-databases} : Create a new ourDatabases.
     *
     * @param ourDatabases the ourDatabases to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ourDatabases, or with status {@code 400 (Bad Request)} if the ourDatabases has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OurDatabases> createOurDatabases(@RequestBody OurDatabases ourDatabases) throws URISyntaxException {
        log.debug("REST request to save OurDatabases : {}", ourDatabases);
        if (ourDatabases.getId() != null) {
            throw new BadRequestAlertException("A new ourDatabases cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OurDatabases result = ourDatabasesRepository.save(ourDatabases);
        return ResponseEntity
            .created(new URI("/api/our-databases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /our-databases/:id} : Updates an existing ourDatabases.
     *
     * @param id the id of the ourDatabases to save.
     * @param ourDatabases the ourDatabases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ourDatabases,
     * or with status {@code 400 (Bad Request)} if the ourDatabases is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ourDatabases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OurDatabases> updateOurDatabases(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OurDatabases ourDatabases
    ) throws URISyntaxException {
        log.debug("REST request to update OurDatabases : {}, {}", id, ourDatabases);
        if (ourDatabases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ourDatabases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ourDatabasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OurDatabases result = ourDatabasesRepository.save(ourDatabases);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ourDatabases.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /our-databases/:id} : Partial updates given fields of an existing ourDatabases, field will ignore if it is null
     *
     * @param id the id of the ourDatabases to save.
     * @param ourDatabases the ourDatabases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ourDatabases,
     * or with status {@code 400 (Bad Request)} if the ourDatabases is not valid,
     * or with status {@code 404 (Not Found)} if the ourDatabases is not found,
     * or with status {@code 500 (Internal Server Error)} if the ourDatabases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OurDatabases> partialUpdateOurDatabases(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OurDatabases ourDatabases
    ) throws URISyntaxException {
        log.debug("REST request to partial update OurDatabases partially : {}, {}", id, ourDatabases);
        if (ourDatabases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ourDatabases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ourDatabasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OurDatabases> result = ourDatabasesRepository
            .findById(ourDatabases.getId())
            .map(existingOurDatabases -> {
                if (ourDatabases.getDatabaseId() != null) {
                    existingOurDatabases.setDatabaseId(ourDatabases.getDatabaseId());
                }
                if (ourDatabases.getName() != null) {
                    existingOurDatabases.setName(ourDatabases.getName());
                }

                return existingOurDatabases;
            })
            .map(ourDatabasesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ourDatabases.getId().toString())
        );
    }

    /**
     * {@code GET  /our-databases} : get all the ourDatabases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ourDatabases in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OurDatabases>> getAllOurDatabases(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OurDatabases");
        Page<OurDatabases> page = ourDatabasesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /our-databases/:id} : get the "id" ourDatabases.
     *
     * @param id the id of the ourDatabases to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ourDatabases, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OurDatabases> getOurDatabases(@PathVariable("id") Long id) {
        log.debug("REST request to get OurDatabases : {}", id);
        Optional<OurDatabases> ourDatabases = ourDatabasesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ourDatabases);
    }

    /**
     * {@code DELETE  /our-databases/:id} : delete the "id" ourDatabases.
     *
     * @param id the id of the ourDatabases to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOurDatabases(@PathVariable("id") Long id) {
        log.debug("REST request to delete OurDatabases : {}", id);
        ourDatabasesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
