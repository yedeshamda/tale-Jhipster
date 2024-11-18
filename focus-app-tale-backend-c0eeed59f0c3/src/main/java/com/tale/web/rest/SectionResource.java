package com.tale.web.rest;

import com.tale.domain.Section;
import com.tale.repository.SectionRepository;
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
 * REST controller for managing {@link com.tale.domain.Section}.
 */
@RestController
@RequestMapping("/api/sections")
@Transactional
public class SectionResource {

    private final Logger log = LoggerFactory.getLogger(SectionResource.class);

    private static final String ENTITY_NAME = "section";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SectionRepository sectionRepository;

    public SectionResource(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * {@code POST  /sections} : Create a new section.
     *
     * @param section the section to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new section, or with status {@code 400 (Bad Request)} if the section has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Section> createSection(@RequestBody Section section) throws URISyntaxException {
        log.debug("REST request to save Section : {}", section);
        if (section.getId() != null) {
            throw new BadRequestAlertException("A new section cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Section result = sectionRepository.save(section);
        return ResponseEntity
            .created(new URI("/api/sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sections/:id} : Updates an existing section.
     *
     * @param id the id of the section to save.
     * @param section the section to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated section,
     * or with status {@code 400 (Bad Request)} if the section is not valid,
     * or with status {@code 500 (Internal Server Error)} if the section couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable(value = "id", required = false) final Long id, @RequestBody Section section)
        throws URISyntaxException {
        log.debug("REST request to update Section : {}, {}", id, section);
        if (section.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, section.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Section result = sectionRepository.save(section);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, section.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sections/:id} : Partial updates given fields of an existing section, field will ignore if it is null
     *
     * @param id the id of the section to save.
     * @param section the section to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated section,
     * or with status {@code 400 (Bad Request)} if the section is not valid,
     * or with status {@code 404 (Not Found)} if the section is not found,
     * or with status {@code 500 (Internal Server Error)} if the section couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Section> partialUpdateSection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Section section
    ) throws URISyntaxException {
        log.debug("REST request to partial update Section partially : {}, {}", id, section);
        if (section.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, section.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Section> result = sectionRepository
            .findById(section.getId())
            .map(existingSection -> {
                if (section.getSectionId() != null) {
                    existingSection.setSectionId(section.getSectionId());
                }
                if (section.getTitle() != null) {
                    existingSection.setTitle(section.getTitle());
                }

                return existingSection;
            })
            .map(sectionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, section.getId().toString())
        );
    }

    /**
     * {@code GET  /sections} : get all the sections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Section>> getAllSections(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Sections");
        Page<Section> page = sectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sections/:id} : get the "id" section.
     *
     * @param id the id of the section to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the section, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Section> getSection(@PathVariable("id") Long id) {
        log.debug("REST request to get Section : {}", id);
        Optional<Section> section = sectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(section);
    }

    /**
     * {@code DELETE  /sections/:id} : delete the "id" section.
     *
     * @param id the id of the section to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable("id") Long id) {
        log.debug("REST request to delete Section : {}", id);
        sectionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
