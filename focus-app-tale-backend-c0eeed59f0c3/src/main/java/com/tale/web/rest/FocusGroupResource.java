package com.tale.web.rest;

import com.tale.domain.FocusGroup;
import com.tale.repository.FocusGroupRepository;
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
 * REST controller for managing {@link com.tale.domain.FocusGroup}.
 */
@RestController
@RequestMapping("/api/focus-groups")
@Transactional
public class FocusGroupResource {

    private final Logger log = LoggerFactory.getLogger(FocusGroupResource.class);

    private static final String ENTITY_NAME = "focusGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FocusGroupRepository focusGroupRepository;

    public FocusGroupResource(FocusGroupRepository focusGroupRepository) {
        this.focusGroupRepository = focusGroupRepository;
    }

    /**
     * {@code POST  /focus-groups} : Create a new focusGroup.
     *
     * @param focusGroup the focusGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new focusGroup, or with status {@code 400 (Bad Request)} if the focusGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FocusGroup> createFocusGroup(@RequestBody FocusGroup focusGroup) throws URISyntaxException {
        log.debug("REST request to save FocusGroup : {}", focusGroup);
        if (focusGroup.getId() != null) {
            throw new BadRequestAlertException("A new focusGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FocusGroup result = focusGroupRepository.save(focusGroup);
        return ResponseEntity
            .created(new URI("/api/focus-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /focus-groups/:id} : Updates an existing focusGroup.
     *
     * @param id the id of the focusGroup to save.
     * @param focusGroup the focusGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated focusGroup,
     * or with status {@code 400 (Bad Request)} if the focusGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the focusGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FocusGroup> updateFocusGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FocusGroup focusGroup
    ) throws URISyntaxException {
        log.debug("REST request to update FocusGroup : {}, {}", id, focusGroup);
        if (focusGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, focusGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!focusGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FocusGroup result = focusGroupRepository.save(focusGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, focusGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /focus-groups/:id} : Partial updates given fields of an existing focusGroup, field will ignore if it is null
     *
     * @param id the id of the focusGroup to save.
     * @param focusGroup the focusGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated focusGroup,
     * or with status {@code 400 (Bad Request)} if the focusGroup is not valid,
     * or with status {@code 404 (Not Found)} if the focusGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the focusGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FocusGroup> partialUpdateFocusGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FocusGroup focusGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update FocusGroup partially : {}, {}", id, focusGroup);
        if (focusGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, focusGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!focusGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FocusGroup> result = focusGroupRepository
            .findById(focusGroup.getId())
            .map(existingFocusGroup -> {
                if (focusGroup.getFocusGroupId() != null) {
                    existingFocusGroup.setFocusGroupId(focusGroup.getFocusGroupId());
                }
                if (focusGroup.getName() != null) {
                    existingFocusGroup.setName(focusGroup.getName());
                }
                if (focusGroup.getDescription() != null) {
                    existingFocusGroup.setDescription(focusGroup.getDescription());
                }
                if (focusGroup.getUserCategory() != null) {
                    existingFocusGroup.setUserCategory(focusGroup.getUserCategory());
                }
                if (focusGroup.getStartDate() != null) {
                    existingFocusGroup.setStartDate(focusGroup.getStartDate());
                }
                if (focusGroup.getEndDate() != null) {
                    existingFocusGroup.setEndDate(focusGroup.getEndDate());
                }
                if (focusGroup.getStatus() != null) {
                    existingFocusGroup.setStatus(focusGroup.getStatus());
                }

                return existingFocusGroup;
            })
            .map(focusGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, focusGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /focus-groups} : get all the focusGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of focusGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FocusGroup>> getAllFocusGroups(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FocusGroups");
        Page<FocusGroup> page = focusGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /focus-groups/:id} : get the "id" focusGroup.
     *
     * @param id the id of the focusGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the focusGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FocusGroup> getFocusGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get FocusGroup : {}", id);
        Optional<FocusGroup> focusGroup = focusGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(focusGroup);
    }

    /**
     * {@code DELETE  /focus-groups/:id} : delete the "id" focusGroup.
     *
     * @param id the id of the focusGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFocusGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete FocusGroup : {}", id);
        focusGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
