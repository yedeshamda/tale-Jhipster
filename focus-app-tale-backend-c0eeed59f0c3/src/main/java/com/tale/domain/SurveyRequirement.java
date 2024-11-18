package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SurveyRequirement.
 */
@Entity
@Table(name = "survey_requirement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurveyRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "requirement_id")
    private Integer requirementId;

    @Column(name = "requirement_description")
    private String requirementDescription;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "created_by_user_company_id")
    private Integer createdByUserCompanyId;

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

    public SurveyRequirement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRequirementId() {
        return this.requirementId;
    }

    public SurveyRequirement requirementId(Integer requirementId) {
        this.setRequirementId(requirementId);
        return this;
    }

    public void setRequirementId(Integer requirementId) {
        this.requirementId = requirementId;
    }

    public String getRequirementDescription() {
        return this.requirementDescription;
    }

    public SurveyRequirement requirementDescription(String requirementDescription) {
        this.setRequirementDescription(requirementDescription);
        return this;
    }

    public void setRequirementDescription(String requirementDescription) {
        this.requirementDescription = requirementDescription;
    }

    public Integer getSurveyId() {
        return this.surveyId;
    }

    public SurveyRequirement surveyId(Integer surveyId) {
        this.setSurveyId(surveyId);
        return this;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getCreatedByUserCompanyId() {
        return this.createdByUserCompanyId;
    }

    public SurveyRequirement createdByUserCompanyId(Integer createdByUserCompanyId) {
        this.setCreatedByUserCompanyId(createdByUserCompanyId);
        return this;
    }

    public void setCreatedByUserCompanyId(Integer createdByUserCompanyId) {
        this.createdByUserCompanyId = createdByUserCompanyId;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public SurveyRequirement createdDate(ZonedDateTime createdDate) {
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

    public SurveyRequirement company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurveyRequirement)) {
            return false;
        }
        return getId() != null && getId().equals(((SurveyRequirement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurveyRequirement{" +
            "id=" + getId() +
            ", requirementId=" + getRequirementId() +
            ", requirementDescription='" + getRequirementDescription() + "'" +
            ", surveyId=" + getSurveyId() +
            ", createdByUserCompanyId=" + getCreatedByUserCompanyId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
