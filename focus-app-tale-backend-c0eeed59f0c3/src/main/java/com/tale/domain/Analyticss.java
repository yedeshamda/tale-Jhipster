package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Analyticss.
 */
@Entity
@Table(name = "analyticss")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Analyticss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "analytics_id")
    private Integer analyticsId;

    @Column(name = "isready")
    private Boolean isready;

    @Column(name = "insights")
    private String insights;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveys", "analytics", "users", "adminUser" }, allowSetters = true)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questions", "analytics", "survey" }, allowSetters = true)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @JsonIgnoreProperties(value = { "analytics" }, allowSetters = true)
    private Survey survey;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Analyticss id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnalyticsId() {
        return this.analyticsId;
    }

    public Analyticss analyticsId(Integer analyticsId) {
        this.setAnalyticsId(analyticsId);
        return this;
    }

    public void setAnalyticsId(Integer analyticsId) {
        this.analyticsId = analyticsId;
    }

    public Boolean getIsready() {
        return this.isready;
    }

    public Analyticss isready(Boolean isready) {
        this.setIsready(isready);
        return this;
    }

    public void setIsready(Boolean isready) {
        this.isready = isready;
    }

    public String getInsights() {
        return this.insights;
    }

    public Analyticss insights(String insights) {
        this.setInsights(insights);
        return this;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Analyticss campaign(Campaign campaign) {
        this.setCampaign(campaign);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Analyticss section(Section section) {
        this.setSection(section);
        return this;
    }

//    public Responses getResponses() {
//        return this.responses;
//    }
//
//    public void setResponses(Responses responses) {
//        this.responses = responses;
//    }
//
//    public Analyticss responses(Responses responses) {
//        this.setResponses(responses);
//        return this;
//    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Analyticss)) {
            return false;
        }
        return getId() != null && getId().equals(((Analyticss) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Analyticss{" +
            "id=" + getId() +
            ", analyticsId=" + getAnalyticsId() +
            ", isready='" + getIsready() + "'" +
            ", insights='" + getInsights() + "'" +
            "}";
    }
}
