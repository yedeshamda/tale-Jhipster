package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SurveyInsight.
 */
@Entity
@Table(name = "survey_insight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurveyInsight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "insight_id")
    private Integer insightId;

    @Column(name = "insights")
    private String insights;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "created_by_user_id")
    private Integer createdByUserId;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "createdSurveys", "createdFocusGroups", "surveyInsights", "companyUsers", "surveyRequirements" },
        allowSetters = true
    )
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SurveyInsight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInsightId() {
        return this.insightId;
    }

    public SurveyInsight insightId(Integer insightId) {
        this.setInsightId(insightId);
        return this;
    }

    public void setInsightId(Integer insightId) {
        this.insightId = insightId;
    }

    public String getInsights() {
        return this.insights;
    }

    public SurveyInsight insights(String insights) {
        this.setInsights(insights);
        return this;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    public Integer getSurveyId() {
        return this.surveyId;
    }

    public SurveyInsight surveyId(Integer surveyId) {
        this.setSurveyId(surveyId);
        return this;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getCreatedByUserId() {
        return this.createdByUserId;
    }

    public SurveyInsight createdByUserId(Integer createdByUserId) {
        this.setCreatedByUserId(createdByUserId);
        return this;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public SurveyInsight createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public SurveyInsight company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurveyInsight)) {
            return false;
        }
        return getId() != null && getId().equals(((SurveyInsight) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurveyInsight{" +
            "id=" + getId() +
            ", insightId=" + getInsightId() +
            ", insights='" + getInsights() + "'" +
            ", surveyId=" + getSurveyId() +
            ", createdByUserId=" + getCreatedByUserId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
