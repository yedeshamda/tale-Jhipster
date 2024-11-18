package com.tale.web.rest;

import com.tale.domain.NormalUser;
import com.tale.repository.NormalUserRepository;
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
 * REST controller for managing {@link com.tale.domain.NormalUser}.
 */
@RestController
@RequestMapping("/api/normal-users")
@Transactional
public class NormalUserResource {

    private final Logger log = LoggerFactory.getLogger(NormalUserResource.class);

    private static final String ENTITY_NAME = "normalUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NormalUserRepository normalUserRepository;

    public NormalUserResource(NormalUserRepository normalUserRepository) {
        this.normalUserRepository = normalUserRepository;
    }

    /**
     * {@code POST  /normal-users} : Create a new normalUser.
     *
     * @param normalUser the normalUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new normalUser, or with status {@code 400 (Bad Request)} if the normalUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NormalUser> createNormalUser(@RequestBody NormalUser normalUser) throws URISyntaxException {
        log.debug("REST request to save NormalUser : {}", normalUser);
        if (normalUser.getId() != null) {
            throw new BadRequestAlertException("A new normalUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NormalUser result = normalUserRepository.save(normalUser);
        return ResponseEntity
            .created(new URI("/api/normal-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /normal-users/:id} : Updates an existing normalUser.
     *
     * @param id the id of the normalUser to save.
     * @param normalUser the normalUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated normalUser,
     * or with status {@code 400 (Bad Request)} if the normalUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the normalUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NormalUser> updateNormalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NormalUser normalUser
    ) throws URISyntaxException {
        log.debug("REST request to update NormalUser : {}, {}", id, normalUser);
        if (normalUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, normalUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!normalUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NormalUser result = normalUserRepository.save(normalUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, normalUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /normal-users/:id} : Partial updates given fields of an existing normalUser, field will ignore if it is null
     *
     * @param id the id of the normalUser to save.
     * @param normalUser the normalUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated normalUser,
     * or with status {@code 400 (Bad Request)} if the normalUser is not valid,
     * or with status {@code 404 (Not Found)} if the normalUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the normalUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NormalUser> partialUpdateNormalUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NormalUser normalUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update NormalUser partially : {}, {}", id, normalUser);
        if (normalUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, normalUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!normalUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NormalUser> result = normalUserRepository
            .findById(normalUser.getId())
            .map(existingNormalUser -> {
                if (normalUser.getUserId() != null) {
                    existingNormalUser.setUserId(normalUser.getUserId());
                }
                if (normalUser.getUsername() != null) {
                    existingNormalUser.setUsername(normalUser.getUsername());
                }
                if (normalUser.getEmail() != null) {
                    existingNormalUser.setEmail(normalUser.getEmail());
                }
                if (normalUser.getFirstname() != null) {
                    existingNormalUser.setFirstname(normalUser.getFirstname());
                }
                if (normalUser.getLastname() != null) {
                    existingNormalUser.setLastname(normalUser.getLastname());
                }
                if (normalUser.getAge() != null) {
                    existingNormalUser.setAge(normalUser.getAge());
                }
                if (normalUser.getJob() != null) {
                    existingNormalUser.setJob(normalUser.getJob());
                }
                if (normalUser.getGender() != null) {
                    existingNormalUser.setGender(normalUser.getGender());
                }
                if (normalUser.getAddress() != null) {
                    existingNormalUser.setAddress(normalUser.getAddress());
                }
                if (normalUser.getEarnedPoints() != null) {
                    existingNormalUser.setEarnedPoints(normalUser.getEarnedPoints());
                }
                if (normalUser.getStatus() != null) {
                    existingNormalUser.setStatus(normalUser.getStatus());
                }

                return existingNormalUser;
            })
            .map(normalUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, normalUser.getId().toString())
        );
    }

    /**
     * {@code GET  /normal-users} : get all the normalUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of normalUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NormalUser>> getAllNormalUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NormalUsers");
        Page<NormalUser> page = normalUserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /normal-users/:id} : get the "id" normalUser.
     *
     * @param id the id of the normalUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the normalUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NormalUser> getNormalUser(@PathVariable("id") Long id) {
        log.debug("REST request to get NormalUser : {}", id);
        Optional<NormalUser> normalUser = normalUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(normalUser);
    }

    /**
     * {@code DELETE  /normal-users/:id} : delete the "id" normalUser.
     *
     * @param id the id of the normalUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNormalUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete NormalUser : {}", id);
        normalUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
