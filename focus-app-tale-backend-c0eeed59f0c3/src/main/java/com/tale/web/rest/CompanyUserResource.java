package com.tale.web.rest;

import com.tale.domain.CompanyUser;
import com.tale.repository.CompanyUserRepository;
import com.tale.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.tale.domain.CompanyUser}.
 */
@RestController
@RequestMapping("/api/company-users")
@Transactional
public class CompanyUserResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUserResource.class);

    private static final String ENTITY_NAME = "companyUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyUserRepository companyUserRepository;

    public CompanyUserResource(CompanyUserRepository companyUserRepository) {
        this.companyUserRepository = companyUserRepository;
    }

    /**
     * {@code POST  /company-users} : Create a new companyUser.
     *
     * @param companyUser the companyUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyUser, or with status {@code 400 (Bad Request)} if the companyUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompanyUser> createCompanyUser(@Valid @RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to save CompanyUser : {}", companyUser);
        if (companyUser.getId() != null) {
            throw new BadRequestAlertException("A new companyUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyUser result = companyUserRepository.save(companyUser);
        return ResponseEntity
            .created(new URI("/api/company-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-users/:id} : Updates an existing companyUser.
     *
     * @param id the id of the companyUser to save.
     * @param companyUser the companyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyUser,
     * or with status {@code 400 (Bad Request)} if the companyUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyUser> updateCompanyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyUser companyUser
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyUser : {}, {}", id, companyUser);
        if (companyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyUser result = companyUserRepository.save(companyUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-users/:id} : Partial updates given fields of an existing companyUser, field will ignore if it is null
     *
     * @param id the id of the companyUser to save.
     * @param companyUser the companyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyUser,
     * or with status {@code 400 (Bad Request)} if the companyUser is not valid,
     * or with status {@code 404 (Not Found)} if the companyUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyUser> partialUpdateCompanyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyUser companyUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyUser partially : {}, {}", id, companyUser);
        if (companyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyUser> result = companyUserRepository
            .findById(companyUser.getId())
            .map(existingCompanyUser -> {
                if (companyUser.getCompanyUserId() != null) {
                    existingCompanyUser.setCompanyUserId(companyUser.getCompanyUserId());
                }
                if (companyUser.getUsername() != null) {
                    existingCompanyUser.setUsername(companyUser.getUsername());
                }
                if (companyUser.getPassword() != null) {
                    existingCompanyUser.setPassword(companyUser.getPassword());
                }
                if (companyUser.getEmail() != null) {
                    existingCompanyUser.setEmail(companyUser.getEmail());
                }
                if (companyUser.getPhoneNumber() != null) {
                    existingCompanyUser.setPhoneNumber(companyUser.getPhoneNumber());
                }
                if (companyUser.getFirstName() != null) {
                    existingCompanyUser.setFirstName(companyUser.getFirstName());
                }
                if (companyUser.getLastName() != null) {
                    existingCompanyUser.setLastName(companyUser.getLastName());
                }
                if (companyUser.getJobTitle() != null) {
                    existingCompanyUser.setJobTitle(companyUser.getJobTitle());
                }
                if (companyUser.getStatus() != null) {
                    existingCompanyUser.setStatus(companyUser.getStatus());
                }

                return existingCompanyUser;
            })
            .map(companyUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyUser.getId().toString())
        );
    }

    /**
     * {@code GET  /company-users} : get all the companyUsers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompanyUser>> getAllCompanyUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CompanyUsers");
        Page<CompanyUser> page;
        if (eagerload) {
            page = companyUserRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = companyUserRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-users/:id} : get the "id" companyUser.
     *
     * @param id the id of the companyUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyUser> getCompanyUser(@PathVariable("id") Long id) {
        log.debug("REST request to get CompanyUser : {}", id);
        Optional<CompanyUser> companyUser = companyUserRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(companyUser);
    }

    /**
     * {@code DELETE  /company-users/:id} : delete the "id" companyUser.
     *
     * @param id the id of the companyUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete CompanyUser : {}", id);
        companyUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
