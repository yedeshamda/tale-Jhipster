package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Campaign.
 */
@Entity
@Table(name = "campaign")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "campaign_id")
    private Integer campaignId;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "responseSurveys", "participants", "campaign", "ourDatabases" }, allowSetters = true)
    private Set<Survey> surveys = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "campaign", "section", "responses" }, allowSetters = true)
    private Set<Analyticss> analytics = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responses", "notifications", "participatedSurveys", "campaign" }, allowSetters = true)
    private Set<NormalUser> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "campaigns" }, allowSetters = true)
    private AdminUser adminUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campaign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCampaignId() {
        return this.campaignId;
    }

    public Campaign campaignId(Integer campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getTitle() {
        return this.title;
    }

    public Campaign title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Campaign startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public Campaign endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<Survey> getSurveys() {
        return this.surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        if (this.surveys != null) {
            this.surveys.forEach(i -> i.setCampaign(null));
        }
        if (surveys != null) {
            surveys.forEach(i -> i.setCampaign(this));
        }
        this.surveys = surveys;
    }

    public Campaign surveys(Set<Survey> surveys) {
        this.setSurveys(surveys);
        return this;
    }

    public Campaign addSurvey(Survey survey) {
        this.surveys.add(survey);
        survey.setCampaign(this);
        return this;
    }

    public Campaign removeSurvey(Survey survey) {
        this.surveys.remove(survey);
        survey.setCampaign(null);
        return this;
    }

    public Set<Analyticss> getAnalytics() {
        return this.analytics;
    }

    public void setAnalytics(Set<Analyticss> analyticsses) {
        if (this.analytics != null) {
            this.analytics.forEach(i -> i.setCampaign(null));
        }
        if (analyticsses != null) {
            analyticsses.forEach(i -> i.setCampaign(this));
        }
        this.analytics = analyticsses;
    }

    public Campaign analytics(Set<Analyticss> analyticsses) {
        this.setAnalytics(analyticsses);
        return this;
    }

    public Campaign addAnalytics(Analyticss analyticss) {
        this.analytics.add(analyticss);
        analyticss.setCampaign(this);
        return this;
    }

    public Campaign removeAnalytics(Analyticss analyticss) {
        this.analytics.remove(analyticss);
        analyticss.setCampaign(null);
        return this;
    }

    public Set<NormalUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<NormalUser> normalUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.setCampaign(null));
        }
        if (normalUsers != null) {
            normalUsers.forEach(i -> i.setCampaign(this));
        }
        this.users = normalUsers;
    }

    public Campaign users(Set<NormalUser> normalUsers) {
        this.setUsers(normalUsers);
        return this;
    }

    public Campaign addUser(NormalUser normalUser) {
        this.users.add(normalUser);
        normalUser.setCampaign(this);
        return this;
    }

    public Campaign removeUser(NormalUser normalUser) {
        this.users.remove(normalUser);
        normalUser.setCampaign(null);
        return this;
    }

    public AdminUser getAdminUser() {
        return this.adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Campaign adminUser(AdminUser adminUser) {
        this.setAdminUser(adminUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campaign)) {
            return false;
        }
        return getId() != null && getId().equals(((Campaign) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", campaignId=" + getCampaignId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
