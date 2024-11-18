package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "company_id")
    private Integer companyId;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "industry")
    private String industry;

    @Column(name = "revenue")
    private Float revenue;

    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "status")
    private Integer status;

    @Column(name = "logo") // New logo attribute
    private String logo; // Assuming logo will be stored as a URL or path to the image

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "surveyRequirements", "surveyInsights", "participants", "campaign", "company", "ourDatabases" },
        allowSetters = true
    )
    private Set<Survey> createdSurveys = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "normalUser", "company" }, allowSetters = true)
    private Set<FocusGroup> createdFocusGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<SurveyInsight> surveyInsights = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userRoles", "company" }, allowSetters = true)
    private Set<CompanyUser> companyUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<SurveyRequirement> surveyRequirements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public Company companyId(Integer companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Company companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return this.industry;
    }

    public Company industry(String industry) {
        this.setIndustry(industry);
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Float getRevenue() {
        return this.revenue;
    }

    public Company revenue(Float revenue) {
        this.setRevenue(revenue);
        return this;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public Integer getNumberOfEmployees() {
        return this.numberOfEmployees;
    }

    public Company numberOfEmployees(Integer numberOfEmployees) {
        this.setNumberOfEmployees(numberOfEmployees);
        return this;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getAddress() {
        return this.address;
    }

    public Company address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return this.website;
    }

    public Company website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Company status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // Getter and setter for logo
    public String getLogo() {
        return logo;
    }

    public Company logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Survey> getCreatedSurveys() {
        return this.createdSurveys;
    }

    public void setCreatedSurveys(Set<Survey> surveys) {
        if (this.createdSurveys != null) {
            this.createdSurveys.forEach(i -> i.setCompany(null));
        }
        if (surveys != null) {
            surveys.forEach(i -> i.setCompany(this));
        }
        this.createdSurveys = surveys;
    }

    public Company createdSurveys(Set<Survey> surveys) {
        this.setCreatedSurveys(surveys);
        return this;
    }

    public Company addCreatedSurveys(Survey survey) {
        this.createdSurveys.add(survey);
        survey.setCompany(this);
        return this;
    }

    public Company removeCreatedSurveys(Survey survey) {
        this.createdSurveys.remove(survey);
        survey.setCompany(null);
        return this;
    }

    public Set<FocusGroup> getCreatedFocusGroups() {
        return this.createdFocusGroups;
    }

    public void setCreatedFocusGroups(Set<FocusGroup> focusGroups) {
        if (this.createdFocusGroups != null) {
            this.createdFocusGroups.forEach(i -> i.setCompany(null));
        }
        if (focusGroups != null) {
            focusGroups.forEach(i -> i.setCompany(this));
        }
        this.createdFocusGroups = focusGroups;
    }

    public Company createdFocusGroups(Set<FocusGroup> focusGroups) {
        this.setCreatedFocusGroups(focusGroups);
        return this;
    }

    public Company addCreatedFocusGroups(FocusGroup focusGroup) {
        this.createdFocusGroups.add(focusGroup);
        focusGroup.setCompany(this);
        return this;
    }

    public Company removeCreatedFocusGroups(FocusGroup focusGroup) {
        this.createdFocusGroups.remove(focusGroup);
        focusGroup.setCompany(null);
        return this;
    }

    public Set<SurveyInsight> getSurveyInsights() {
        return this.surveyInsights;
    }

    public void setSurveyInsights(Set<SurveyInsight> surveyInsights) {
        if (this.surveyInsights != null) {
            this.surveyInsights.forEach(i -> i.setCompany(null));
        }
        if (surveyInsights != null) {
            surveyInsights.forEach(i -> i.setCompany(this));
        }
        this.surveyInsights = surveyInsights;
    }

    public Company surveyInsights(Set<SurveyInsight> surveyInsights) {
        this.setSurveyInsights(surveyInsights);
        return this;
    }

    public Company addSurveyInsights(SurveyInsight surveyInsight) {
        this.surveyInsights.add(surveyInsight);
        surveyInsight.setCompany(this);
        return this;
    }

    public Company removeSurveyInsights(SurveyInsight surveyInsight) {
        this.surveyInsights.remove(surveyInsight);
        surveyInsight.setCompany(null);
        return this;
    }

    public Set<CompanyUser> getCompanyUsers() {
        return this.companyUsers;
    }

    public void setCompanyUsers(Set<CompanyUser> companyUsers) {
        if (this.companyUsers != null) {
            this.companyUsers.forEach(i -> i.setCompany(null));
        }
        if (companyUsers != null) {
            companyUsers.forEach(i -> i.setCompany(this));
        }
        this.companyUsers = companyUsers;
    }

    public Company companyUsers(Set<CompanyUser> companyUsers) {
        this.setCompanyUsers(companyUsers);
        return this;
    }

    public Company addCompanyUsers(CompanyUser companyUser) {
        this.companyUsers.add(companyUser);
        companyUser.setCompany(this);
        return this;
    }

    public Company removeCompanyUsers(CompanyUser companyUser) {
        this.companyUsers.remove(companyUser);
        companyUser.setCompany(null);
        return this;
    }

    public Set<SurveyRequirement> getSurveyRequirements() {
        return this.surveyRequirements;
    }

    public void setSurveyRequirements(Set<SurveyRequirement> surveyRequirements) {
        if (this.surveyRequirements != null) {
            this.surveyRequirements.forEach(i -> i.setCompany(null));
        }
        if (surveyRequirements != null) {
            surveyRequirements.forEach(i -> i.setCompany(this));
        }
        this.surveyRequirements = surveyRequirements;
    }

    public Company surveyRequirements(Set<SurveyRequirement> surveyRequirements) {
        this.setSurveyRequirements(surveyRequirements);
        return this;
    }

    public Company addSurveyRequirements(SurveyRequirement surveyRequirement) {
        this.surveyRequirements.add(surveyRequirement);
        surveyRequirement.setCompany(this);
        return this;
    }

    public Company removeSurveyRequirements(SurveyRequirement surveyRequirement) {
        this.surveyRequirements.remove(surveyRequirement);
        surveyRequirement.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return getId() != null && getId().equals(((Company) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyId=" + getCompanyId() +
            ", companyName='" + getCompanyName() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", revenue=" + getRevenue() +
            ", numberOfEmployees=" + getNumberOfEmployees() +
            ", address='" + getAddress() + "'" +
            ", website='" + getWebsite() + "'" +
            ", logo='" + getLogo() + "'" + // Include logo in toString
            ", status=" + getStatus() +
            "}";
    }
}
