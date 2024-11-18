package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FocusGroup.
 */
@Entity
@Table(name = "focus_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FocusGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "focus_group_id")
    private Integer focusGroupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "user_category")
    private String userCategory;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "participatedSurveys", "focusGroups", "pointTransactions", "quizProgresses", "redemptionOptions", "campaign" },
        allowSetters = true
    )
    private NormalUser normalUser;

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

    public FocusGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFocusGroupId() {
        return this.focusGroupId;
    }

    public FocusGroup focusGroupId(Integer focusGroupId) {
        this.setFocusGroupId(focusGroupId);
        return this;
    }

    public void setFocusGroupId(Integer focusGroupId) {
        this.focusGroupId = focusGroupId;
    }

    public String getName() {
        return this.name;
    }

    public FocusGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public FocusGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserCategory() {
        return this.userCategory;
    }

    public FocusGroup userCategory(String userCategory) {
        this.setUserCategory(userCategory);
        return this;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public FocusGroup startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public FocusGroup endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public FocusGroup status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public FocusGroup normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public FocusGroup company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FocusGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((FocusGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FocusGroup{" +
            "id=" + getId() +
            ", focusGroupId=" + getFocusGroupId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", userCategory='" + getUserCategory() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
