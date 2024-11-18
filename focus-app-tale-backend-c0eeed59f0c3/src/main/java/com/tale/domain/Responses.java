package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Responses.
 */
@Entity
@Table(name = "responses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Responses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "response_id")
    private Integer responseId;

    @Column(name = "answer")
    private String answer;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responses")
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    @JsonIgnoreProperties(value = { "campaign", "section", "responses" }, allowSetters = true)
//    private Set<Analyticss> analytics = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responses", "notifications", "participatedSurveys", "campaign" }, allowSetters = true)
    private NormalUser normalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sections", "responseSurveys", "participants", "campaign", "ourDatabases" }, allowSetters = true)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responses", "section" }, allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Responses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getResponseId() {
        return this.responseId;
    }

    public Responses responseId(Integer responseId) {
        this.setResponseId(responseId);
        return this;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Responses answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

//    public Set<Analyticss> getAnalytics() {
//        return this.analytics;
//    }
//
//    public void setAnalytics(Set<Analyticss> analyticsses) {
//        if (this.analytics != null) {
//            this.analytics.forEach(i -> i.setResponses(null));
//        }
//        if (analyticsses != null) {
//            analyticsses.forEach(i -> i.setResponses(this));
//        }
//        this.analytics = analyticsses;
//    }
//
//    public Responses analytics(Set<Analyticss> analyticsses) {
//        this.setAnalytics(analyticsses);
//        return this;
//    }
//
//    public Responses addAnalytics(Analyticss analyticss) {
//        this.analytics.add(analyticss);
//        analyticss.setResponses(this);
//        return this;
//    }
//
//    public Responses removeAnalytics(Analyticss analyticss) {
//        this.analytics.remove(analyticss);
//        analyticss.setResponses(null);
//        return this;
//    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public Responses normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Responses survey(Survey survey) {
        this.setSurvey(survey);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Responses question(Question question) {
        this.setQuestion(question);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Responses)) {
            return false;
        }
        return getId() != null && getId().equals(((Responses) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Responses{" +
            "id=" + getId() +
            ", responseId=" + getResponseId() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
