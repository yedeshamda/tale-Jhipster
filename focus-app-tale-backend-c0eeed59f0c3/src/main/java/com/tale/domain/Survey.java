package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Survey.
 */
@Entity
@Table(name = "survey")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "created_by_user_id")
    private Integer createdByUserId;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private SurveyRequirement surveyRequirements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private SurveyInsight surveyInsights;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_survey__participants",
        joinColumns = @JoinColumn(name = "survey_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "surveys", "normalUser" }, allowSetters = true)
    private Set<SurveyParticipant> participants = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveys", "analytics", "users", "adminUser" }, allowSetters = true)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "createdSurveys", "createdFocusGroups", "surveyInsights", "companyUsers", "surveyRequirements" },
        allowSetters = true
    )
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveyDatabases" }, allowSetters = true)
    private OurDatabases ourDatabases;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "survey" }, allowSetters = true)
    private List<Question> questions;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "survey" }, allowSetters = true)
    private Set<Analyticss> analytics = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveys" }, allowSetters = true)
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "survey" }, allowSetters = true)
    private Set<SurveyColl> surveyColls = new HashSet<>();  // One Survey can have many SurveyColls

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Survey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSurveyId() {
        return this.surveyId;
    }

    public Survey surveyId(Integer surveyId) {
        this.setSurveyId(surveyId);
        return this;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getTitle() {
        return this.title;
    }

    public Survey title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Survey description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Survey startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public Survey endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getCreatedByUserId() {
        return this.createdByUserId;
    }

    public Survey createdByUserId(Integer createdByUserId) {
        this.setCreatedByUserId(createdByUserId);
        return this;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Survey createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Survey status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SurveyRequirement getSurveyRequirements() {
        return this.surveyRequirements;
    }

    public void setSurveyRequirements(SurveyRequirement surveyRequirement) {
        this.surveyRequirements = surveyRequirement;
    }

    public Survey surveyRequirements(SurveyRequirement surveyRequirement) {
        this.setSurveyRequirements(surveyRequirement);
        return this;
    }

    public SurveyInsight getSurveyInsights() {
        return this.surveyInsights;
    }

    public void setSurveyInsights(SurveyInsight surveyInsight) {
        this.surveyInsights = surveyInsight;
    }

    public Survey surveyInsights(SurveyInsight surveyInsight) {
        this.setSurveyInsights(surveyInsight);
        return this;
    }

    public Set<SurveyParticipant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<SurveyParticipant> surveyParticipants) {
        this.participants = surveyParticipants;
    }

    public Survey participants(Set<SurveyParticipant> surveyParticipants) {
        this.setParticipants(surveyParticipants);
        return this;
    }

    public Survey addParticipants(SurveyParticipant surveyParticipant) {
        this.participants.add(surveyParticipant);
        return this;
    }

    public Survey removeParticipants(SurveyParticipant surveyParticipant) {
        this.participants.remove(surveyParticipant);
        return this;
    }

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Survey campaign(Campaign campaign) {
        this.setCampaign(campaign);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Survey company(Company company) {
        this.setCompany(company);
        return this;
    }

    public OurDatabases getOurDatabases() {
        return this.ourDatabases;
    }

    public void setOurDatabases(OurDatabases ourDatabases) {
        this.ourDatabases = ourDatabases;
    }

    public Survey ourDatabases(OurDatabases ourDatabases) {
        this.setOurDatabases(ourDatabases);
        return this;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Survey questions(List<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Survey addQuestion(Question question) {
        this.questions.add(question);
        question.setSurvey(this);
        return this;
    }

    public Survey removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSurvey(null);
        return this;
    }

    public Set<Analyticss> getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Set<Analyticss> analytics) {
        this.analytics = analytics;
    }

    public Survey addAnalytics(Analyticss analytics) {
        this.analytics.add(analytics);
        analytics.setSurvey(this);
        return this;
    }

    public Survey removeAnalytics(Analyticss analytics) {
        this.analytics.remove(analytics);
        analytics.setSurvey(null);
        return this;
    }


    // Add the getter and setter for product
    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Update the constructor and other methods as needed
    public Survey product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Survey)) {
            return false;
        }
        return getId() != null && getId().equals(((Survey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Survey{" +
            "id=" + getId() +
            ", surveyId=" + getSurveyId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdByUserId=" + getCreatedByUserId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
