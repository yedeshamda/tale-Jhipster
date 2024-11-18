package com.tale.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A SurveyColl represents a collection of surveys, associated with a single survey.
 */
@Entity
@Table(name = "survey_coll")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SurveyColl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "status")
    private Integer status;

    @Column(name = "responses")
    private Integer responses;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    private ZonedDateTime dateModified;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "surveyColls" }, allowSetters = true)
    private Survey survey;  // Many SurveyColls can be associated with one Survey

    // Getters and setters
    public Long getId() {
        return id;
    }

    public SurveyColl id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public SurveyColl codeName(String codeName) {
        this.codeName = codeName;
        return this;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Integer getStatus() {
        return status;
    }

    public SurveyColl status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getResponses() {
        return responses;
    }

    public SurveyColl responses(Integer responses) {
        this.responses = responses;
        return this;
    }

    public void setResponses(Integer responses) {
        this.responses = responses;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public SurveyColl dateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateModified() {
        return dateModified;
    }

    public SurveyColl dateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public SurveyColl survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurveyColl)) {
            return false;
        }
        return id != null && id.equals(((SurveyColl) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SurveyColl{" +
            "id=" + getId() +
            ", codeName='" + getCodeName() + "'" +
            ", status=" + getStatus() +
            ", responses=" + getResponses() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
