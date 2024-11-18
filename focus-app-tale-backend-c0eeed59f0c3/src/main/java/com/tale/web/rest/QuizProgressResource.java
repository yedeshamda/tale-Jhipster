package com.tale.web.rest;

import com.tale.domain.QuizProgress;
import com.tale.repository.QuizProgressRepository;
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
 * REST controller for managing {@link com.tale.domain.QuizProgress}.
 */
@RestController
@RequestMapping("/api/quiz-progresses")
@Transactional
public class QuizProgressResource {

    private final Logger log = LoggerFactory.getLogger(QuizProgressResource.class);

    private static final String ENTITY_NAME = "quizProgress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizProgressRepository quizProgressRepository;

    public QuizProgressResource(QuizProgressRepository quizProgressRepository) {
        this.quizProgressRepository = quizProgressRepository;
    }

    /**
     * {@code POST  /quiz-progresses} : Create a new quizProgress.
     *
     * @param quizProgress the quizProgress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizProgress, or with status {@code 400 (Bad Request)} if the quizProgress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizProgress> createQuizProgress(@RequestBody QuizProgress quizProgress) throws URISyntaxException {
        log.debug("REST request to save QuizProgress : {}", quizProgress);
        if (quizProgress.getId() != null) {
            throw new BadRequestAlertException("A new quizProgress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizProgress result = quizProgressRepository.save(quizProgress);
        return ResponseEntity
            .created(new URI("/api/quiz-progresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quiz-progresses/:id} : Updates an existing quizProgress.
     *
     * @param id the id of the quizProgress to save.
     * @param quizProgress the quizProgress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizProgress,
     * or with status {@code 400 (Bad Request)} if the quizProgress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizProgress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizProgress> updateQuizProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizProgress quizProgress
    ) throws URISyntaxException {
        log.debug("REST request to update QuizProgress : {}, {}", id, quizProgress);
        if (quizProgress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizProgress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizProgress result = quizProgressRepository.save(quizProgress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizProgress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quiz-progresses/:id} : Partial updates given fields of an existing quizProgress, field will ignore if it is null
     *
     * @param id the id of the quizProgress to save.
     * @param quizProgress the quizProgress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizProgress,
     * or with status {@code 400 (Bad Request)} if the quizProgress is not valid,
     * or with status {@code 404 (Not Found)} if the quizProgress is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizProgress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizProgress> partialUpdateQuizProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizProgress quizProgress
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizProgress partially : {}, {}", id, quizProgress);
        if (quizProgress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizProgress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizProgress> result = quizProgressRepository
            .findById(quizProgress.getId())
            .map(existingQuizProgress -> {
                if (quizProgress.getQuizProgressId() != null) {
                    existingQuizProgress.setQuizProgressId(quizProgress.getQuizProgressId());
                }
                if (quizProgress.getUserId() != null) {
                    existingQuizProgress.setUserId(quizProgress.getUserId());
                }
                if (quizProgress.getSurveyId() != null) {
                    existingQuizProgress.setSurveyId(quizProgress.getSurveyId());
                }
                if (quizProgress.getProgress() != null) {
                    existingQuizProgress.setProgress(quizProgress.getProgress());
                }
                if (quizProgress.getStartedDate() != null) {
                    existingQuizProgress.setStartedDate(quizProgress.getStartedDate());
                }
                if (quizProgress.getCompletedDate() != null) {
                    existingQuizProgress.setCompletedDate(quizProgress.getCompletedDate());
                }

                return existingQuizProgress;
            })
            .map(quizProgressRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizProgress.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-progresses} : get all the quizProgresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizProgresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizProgress>> getAllQuizProgresses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of QuizProgresses");
        Page<QuizProgress> page = quizProgressRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quiz-progresses/:id} : get the "id" quizProgress.
     *
     * @param id the id of the quizProgress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizProgress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizProgress> getQuizProgress(@PathVariable("id") Long id) {
        log.debug("REST request to get QuizProgress : {}", id);
        Optional<QuizProgress> quizProgress = quizProgressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quizProgress);
    }

    /**
     * {@code DELETE  /quiz-progresses/:id} : delete the "id" quizProgress.
     *
     * @param id the id of the quizProgress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizProgress(@PathVariable("id") Long id) {
        log.debug("REST request to delete QuizProgress : {}", id);
        quizProgressRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
