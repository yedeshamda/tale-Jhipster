package com.tale.web.rest;

import com.tale.domain.AdminUser;
import com.tale.repository.AdminUserRepository;
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
 * REST controller for managing {@link com.tale.domain.AdminUser}.
 */
@RestController
@RequestMapping("/api/admin-users")
@Transactional
public class AdminUserResource {

    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);

    private static final String ENTITY_NAME = "adminUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminUserRepository adminUserRepository;

    public AdminUserResource(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    /**
     * {@code POST  /admin-users} : Create a new adminUser.
     *
     * @param adminUser the adminUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminUser, or with status {@code 400 (Bad Request)} if the adminUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdminUser> createAdminUser(@RequestBody AdminUser adminUser) throws URISyntaxException {
        log.debug("REST request to save AdminUser : {}", adminUser);
        if (adminUser.getId() != null) {
            throw new BadRequestAlertException("A new adminUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminUser result = adminUserRepository.save(adminUser);
        return ResponseEntity
            .created(new URI("/api/admin-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /admin-users/:id} : Updates an existing adminUser.
     *
     * @param id the id of the adminUser to save.
     * @param adminUser the adminUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminUser,
     * or with status {@code 400 (Bad Request)} if the adminUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdminUser adminUser
    ) throws URISyntaxException {
        log.debug("REST request to update AdminUser : {}, {}", id, adminUser);
        if (adminUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdminUser result = adminUserRepository.save(adminUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adminUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /admin-users/:id} : Partial updates given fields of an existing adminUser, field will ignore if it is null
     *
     * @param id the id of the adminUser to save.
     * @param adminUser the adminUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminUser,
     * or with status {@code 400 (Bad Request)} if the adminUser is not valid,
     * or with status {@code 404 (Not Found)} if the adminUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdminUser> partialUpdateAdminUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdminUser adminUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminUser partially : {}, {}", id, adminUser);
        if (adminUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminUser> result = adminUserRepository
            .findById(adminUser.getId())
            .map(existingAdminUser -> {
                if (adminUser.getAdminId() != null) {
                    existingAdminUser.setAdminId(adminUser.getAdminId());
                }

                return existingAdminUser;
            })
            .map(adminUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adminUser.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-users} : get all the adminUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdminUser>> getAllAdminUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AdminUsers");
        Page<AdminUser> page = adminUserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-users/:id} : get the "id" adminUser.
     *
     * @param id the id of the adminUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUser(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminUser : {}", id);
        Optional<AdminUser> adminUser = adminUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adminUser);
    }

    /**
     * {@code DELETE  /admin-users/:id} : delete the "id" adminUser.
     *
     * @param id the id of the adminUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminUser : {}", id);
        adminUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
