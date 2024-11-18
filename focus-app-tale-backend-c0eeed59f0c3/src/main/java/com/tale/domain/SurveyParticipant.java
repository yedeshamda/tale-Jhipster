package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SurveyParticipant.
 */
@Entity
@Table(name = "survey_participant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurveyParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "participant_id")
    private Integer participantId;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "surveyRequirements", "surveyInsights", "participants", "campaign", "company", "ourDatabases" },
        allowSetters = true
    )
    private Set<Survey> surveys = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "participatedSurveys", "focusGroups", "pointTransactions", "quizProgresses", "redemptionOptions", "campaign" },
        allowSetters = true
    )
    private NormalUser normalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SurveyParticipant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParticipantId() {
        return this.participantId;
    }

    public SurveyParticipant participantId(Integer participantId) {
        this.setParticipantId(participantId);
        return this;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Set<Survey> getSurveys() {
        return this.surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        if (this.surveys != null) {
            this.surveys.forEach(i -> i.removeParticipants(this));
        }
        if (surveys != null) {
            surveys.forEach(i -> i.addParticipants(this));
        }
        this.surveys = surveys;
    }

    public SurveyParticipant surveys(Set<Survey> surveys) {
        this.setSurveys(surveys);
        return this;
    }

    public SurveyParticipant addSurveys(Survey survey) {
        this.surveys.add(survey);
        survey.getParticipants().add(this);
        return this;
    }

    public SurveyParticipant removeSurveys(Survey survey) {
        this.surveys.remove(survey);
        survey.getParticipants().remove(this);
        return this;
    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public SurveyParticipant normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurveyParticipant)) {
            return false;
        }
        return getId() != null && getId().equals(((SurveyParticipant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurveyParticipant{" +
            "id=" + getId() +
            ", participantId=" + getParticipantId() +
            "}";
    }
}
