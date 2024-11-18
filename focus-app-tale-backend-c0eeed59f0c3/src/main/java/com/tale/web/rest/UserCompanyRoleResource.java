package com.tale.web.rest;

import com.tale.domain.UserCompanyRole;
import com.tale.repository.UserCompanyRoleRepository;
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
 * REST controller for managing {@link com.tale.domain.UserCompanyRole}.
 */
@RestController
@RequestMapping("/api/user-company-roles")
@Transactional
public class UserCompanyRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserCompanyRoleResource.class);

    private static final String ENTITY_NAME = "userCompanyRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCompanyRoleRepository userCompanyRoleRepository;

    public UserCompanyRoleResource(UserCompanyRoleRepository userCompanyRoleRepository) {
        this.userCompanyRoleRepository = userCompanyRoleRepository;
    }

    /**
     * {@code POST  /user-company-roles} : Create a new userCompanyRole.
     *
     * @param userCompanyRole the userCompanyRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCompanyRole, or with status {@code 400 (Bad Request)} if the userCompanyRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserCompanyRole> createUserCompanyRole(@RequestBody UserCompanyRole userCompanyRole) throws URISyntaxException {
        log.debug("REST request to save UserCompanyRole : {}", userCompanyRole);
        if (userCompanyRole.getId() != null) {
            throw new BadRequestAlertException("A new userCompanyRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserCompanyRole result = userCompanyRoleRepository.save(userCompanyRole);
        return ResponseEntity
            .created(new URI("/api/user-company-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-company-roles/:id} : Updates an existing userCompanyRole.
     *
     * @param id the id of the userCompanyRole to save.
     * @param userCompanyRole the userCompanyRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCompanyRole,
     * or with status {@code 400 (Bad Request)} if the userCompanyRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCompanyRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserCompanyRole> updateUserCompanyRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserCompanyRole userCompanyRole
    ) throws URISyntaxException {
        log.debug("REST request to update UserCompanyRole : {}, {}", id, userCompanyRole);
        if (userCompanyRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCompanyRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCompanyRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserCompanyRole result = userCompanyRoleRepository.save(userCompanyRole);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCompanyRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-company-roles/:id} : Partial updates given fields of an existing userCompanyRole, field will ignore if it is null
     *
     * @param id the id of the userCompanyRole to save.
     * @param userCompanyRole the userCompanyRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCompanyRole,
     * or with status {@code 400 (Bad Request)} if the userCompanyRole is not valid,
     * or with status {@code 404 (Not Found)} if the userCompanyRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCompanyRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserCompanyRole> partialUpdateUserCompanyRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserCompanyRole userCompanyRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserCompanyRole partially : {}, {}", id, userCompanyRole);
        if (userCompanyRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCompanyRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCompanyRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserCompanyRole> result = userCompanyRoleRepository
            .findById(userCompanyRole.getId())
            .map(existingUserCompanyRole -> {
                if (userCompanyRole.getRoleId() != null) {
                    existingUserCompanyRole.setRoleId(userCompanyRole.getRoleId());
                }
                if (userCompanyRole.getRoleName() != null) {
                    existingUserCompanyRole.setRoleName(userCompanyRole.getRoleName());
                }
                if (userCompanyRole.getDescription() != null) {
                    existingUserCompanyRole.setDescription(userCompanyRole.getDescription());
                }
                if (userCompanyRole.getStatus() != null) {
                    existingUserCompanyRole.setStatus(userCompanyRole.getStatus());
                }

                return existingUserCompanyRole;
            })
            .map(userCompanyRoleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCompanyRole.getId().toString())
        );
    }

    /**
     * {@code GET  /user-company-roles} : get all the userCompanyRoles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userCompanyRoles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserCompanyRole>> getAllUserCompanyRoles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserCompanyRoles");
        Page<UserCompanyRole> page = userCompanyRoleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-company-roles/:id} : get the "id" userCompanyRole.
     *
     * @param id the id of the userCompanyRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCompanyRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserCompanyRole> getUserCompanyRole(@PathVariable("id") Long id) {
        log.debug("REST request to get UserCompanyRole : {}", id);
        Optional<UserCompanyRole> userCompanyRole = userCompanyRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userCompanyRole);
    }

    /**
     * {@code DELETE  /user-company-roles/:id} : delete the "id" userCompanyRole.
     *
     * @param id the id of the userCompanyRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCompanyRole(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserCompanyRole : {}", id);
        userCompanyRoleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
