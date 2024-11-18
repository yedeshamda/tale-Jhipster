package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizProgress.
 */
@Entity
@Table(name = "quiz_progress")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quiz_progress_id")
    private Integer quizProgressId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "progress")
    private Float progress;

    @Column(name = "started_date")
    private ZonedDateTime startedDate;

    @Column(name = "completed_date")
    private ZonedDateTime completedDate;

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

    public QuizProgress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuizProgressId() {
        return this.quizProgressId;
    }

    public QuizProgress quizProgressId(Integer quizProgressId) {
        this.setQuizProgressId(quizProgressId);
        return this;
    }

    public void setQuizProgressId(Integer quizProgressId) {
        this.quizProgressId = quizProgressId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public QuizProgress userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSurveyId() {
        return this.surveyId;
    }

    public QuizProgress surveyId(Integer surveyId) {
        this.setSurveyId(surveyId);
        return this;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Float getProgress() {
        return this.progress;
    }

    public QuizProgress progress(Float progress) {
        this.setProgress(progress);
        return this;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public ZonedDateTime getStartedDate() {
        return this.startedDate;
    }

    public QuizProgress startedDate(ZonedDateTime startedDate) {
        this.setStartedDate(startedDate);
        return this;
    }

    public void setStartedDate(ZonedDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public ZonedDateTime getCompletedDate() {
        return this.completedDate;
    }

    public QuizProgress completedDate(ZonedDateTime completedDate) {
        this.setCompletedDate(completedDate);
        return this;
    }

    public void setCompletedDate(ZonedDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public QuizProgress normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizProgress)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizProgress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizProgress{" +
            "id=" + getId() +
            ", quizProgressId=" + getQuizProgressId() +
            ", userId=" + getUserId() +
            ", surveyId=" + getSurveyId() +
            ", progress=" + getProgress() +
            ", startedDate='" + getStartedDate() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            "}";
    }
}
