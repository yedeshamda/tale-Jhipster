package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Section.
 */
@Entity
@Table(name = "section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "section_id")
    private Integer sectionId;

    @Column(name = "title")
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responses", "section" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "campaign", "section", "responses" }, allowSetters = true)
    private Set<Analyticss> analytics = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sections", "responseSurveys", "participants", "campaign", "ourDatabases" }, allowSetters = true)
    private Survey survey;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Section id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSectionId() {
        return this.sectionId;
    }

    public Section sectionId(Integer sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return this.title;
    }

    public Section title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setSection(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setSection(this));
        }
        this.questions = questions;
    }

    public Section questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Section addQuestion(Question question) {
        this.questions.add(question);
        question.setSection(this);
        return this;
    }

    public Section removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSection(null);
        return this;
    }

    public Set<Analyticss> getAnalytics() {
        return this.analytics;
    }

    public void setAnalytics(Set<Analyticss> analyticsses) {
        if (this.analytics != null) {
            this.analytics.forEach(i -> i.setSection(null));
        }
        if (analyticsses != null) {
            analyticsses.forEach(i -> i.setSection(this));
        }
        this.analytics = analyticsses;
    }

    public Section analytics(Set<Analyticss> analyticsses) {
        this.setAnalytics(analyticsses);
        return this;
    }

    public Section addAnalytics(Analyticss analyticss) {
        this.analytics.add(analyticss);
        analyticss.setSection(this);
        return this;
    }

    public Section removeAnalytics(Analyticss analyticss) {
        this.analytics.remove(analyticss);
        analyticss.setSection(null);
        return this;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Section survey(Survey survey) {
        this.setSurvey(survey);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return getId() != null && getId().equals(((Section) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", sectionId=" + getSectionId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
