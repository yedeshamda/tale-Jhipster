package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "text")
    private String text;

    @Column(name = "description")
    private String description;

    @Column(name = "mandatory")
    private Boolean mandatory;

    @Column(name = "options")
    private String optionsString; // Store options as a single string, comma-separated

    @Column(name = "question_type")
    private String questionType;

    @Column(name = "answer_type")
    private String answerType;

    @Column(name = "files")
    private String filesString;  // Store files as a comma-separated string of file paths or IDs

    @Column(name = "generate_responses")
    private Boolean generateResponses;

    @Column(name = "response_type")
    private String responseType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "analytics", "normalUser", "survey", "question" }, allowSetters = true)
    private Set<Responses> responses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questions", "analytics", "survey" }, allowSetters = true)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Survey survey;

    // Getter for description
    public String getDescription() {
        return description;
    }

    public Question description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // Getter for mandatory
    public Boolean getMandatory() {
        return mandatory;
    }

    public Question mandatory(Boolean mandatory) {
        this.setMandatory(mandatory);
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }
    // Getter for questionType
    public String getQuestionType() {
        return questionType;
    }

    public Question questionType(String questionType) {
        this.setQuestionType(questionType);
        return this;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    // Getter for answerType
    public String getAnswerType() {
        return answerType;
    }

    public Question answerType(String answerType) {
        this.setAnswerType(answerType);
        return this;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    // Getter for generateResponses
    public Boolean getGenerateResponses() {
        return generateResponses;
    }

    public Question generateResponses(Boolean generateResponses) {
        this.setGenerateResponses(generateResponses);
        return this;
    }

    public void setGenerateResponses(Boolean generateResponses) {
        this.generateResponses = generateResponses;
    }

    // Getter for responseType
    public String getResponseType() {
        return responseType;
    }

    public Question responseType(String responseType) {
        this.setResponseType(responseType);
        return this;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    // Getter and setter for options as List
    public List<String> getOptions() {
        return optionsString != null ? Arrays.asList(optionsString.split(",")) : List.of();
    }

    public void setOptions(List<String> options) {
        this.optionsString = options != null ? String.join(",", options) : null;
    }

//    public List<String> getFiles() {
//        return filesString != null ? Arrays.asList(filesString.split(",")) : List.of();
//    }
//
//    public void setFiles(List<String> files) {
//        this.filesString = files != null ? String.join(",", files) : null;
//    }

    public String getFile() {
        return filesString;  // Returns a single file as a string
    }

    public void setFile(String file) {
        this.filesString = file;  // Accept a single file string (file name, path, or YouTube ID)
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public Question questionId(Integer questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return this.text;
    }

    public Question text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Set<Responses> getResponses() {
        return this.responses;
    }

    public void setResponses(Set<Responses> responses) {
        if (this.responses != null) {
            this.responses.forEach(i -> i.setQuestion(null));
        }
        if (responses != null) {
            responses.forEach(i -> i.setQuestion(this));
        }
        this.responses = responses;
    }

    public Question responses(Set<Responses> responses) {
        this.setResponses(responses);
        return this;
    }

    public Question addResponse(Responses responses) {
        this.responses.add(responses);
        responses.setQuestion(this);
        return this;
    }

    public Question removeResponse(Responses responses) {
        this.responses.remove(responses);
        responses.setQuestion(null);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Question section(Section section) {
        this.setSection(section);
        return this;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question survey(Survey survey) {
        this.setSurvey(survey);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", questionId=" + getQuestionId() +
            ", text='" + getText() + "'" +
            ", description='" + getDescription() + "'" +
            ", mandatory=" + getMandatory() +
            ", questionType='" + getQuestionType() + "'" +
            ", answerType='" + getAnswerType() + "'" +
            ", generateResponses=" + getGenerateResponses() +
            ", responseType='" + getResponseType() + "'" +
            "}";
    }
}
