package com.tale.web.rest;

import com.tale.domain.Question;
import com.tale.repository.QuestionRepository;
import com.tale.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
 * REST controller for managing {@link com.tale.domain.Question}.
 */
@RestController
@RequestMapping("/api/questions")
@Transactional
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param question the question to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new question, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to save Question : {}", question);

        // Check if the question has an ID, which should not happen for a new question
        if (question.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Assuming that the files field in the request body contains a base64 string
        if (question.getFile() != null) {
            try {
                // Check if the file data starts with "data:image" to ensure it's a base64-encoded image
                String base64String = new String(question.getFile(), StandardCharsets.UTF_8);

                if (base64String.startsWith("data:image/")) {
                    // Split the string at the comma (remove the "data:image/png;base64," part)
                    base64String = base64String.split(",")[1];

                    // Decode the base64 string into a byte array
                    byte[] decodedBytes = Base64.getDecoder().decode(base64String);

                    // Set the decoded byte array into the 'files' field
                    question.setFile(decodedBytes);
                }
            } catch (IllegalArgumentException e) {
                log.error("Invalid base64 string provided for file", e);
                throw new BadRequestAlertException("Invalid file format", ENTITY_NAME, "invalidfile");
            }
        }

        // Save the question in the database
        Question result = questionRepository.save(question);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the question to save.
     * @param question the question to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question,
     * or with status {@code 400 (Bad Request)} if the question is not valid,
     * or with status {@code 500 (Internal Server Error)} if the question couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Question question
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, question.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Question result = questionRepository.save(question);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, question.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the question to save.
     * @param question the question to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question,
     * or with status {@code 400 (Bad Request)} if the question is not valid,
     * or with status {@code 404 (Not Found)} if the question is not found,
     * or with status {@code 500 (Internal Server Error)} if the question couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Question> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Question question
    ) throws URISyntaxException {
        log.debug("REST request to partial update Question partially : {}, {}", id, question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, question.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Question> result = questionRepository
            .findById(question.getId())
            .map(existingQuestion -> {
                if (question.getQuestionId() != null) {
                    existingQuestion.setQuestionId(question.getQuestionId());
                }
                if (question.getText() != null) {
                    existingQuestion.setText(question.getText());
                }

                return existingQuestion;
            })
            .map(questionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, question.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Question>> getAllQuestions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        Page<Question> page = questionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the question to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the question, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<Question> question = questionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(question);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the question to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
