package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NormalUser.
 */
@Entity
@Table(name = "normal_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NormalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "job")
    private String job;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "earned_points")
    private Integer earnedPoints;

    @Column(name = "status")
    private Integer status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "normalUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "surveys", "normalUser" }, allowSetters = true)
    private Set<SurveyParticipant> participatedSurveys = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "normalUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "normalUser", "company" }, allowSetters = true)
    private Set<FocusGroup> focusGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "normalUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "normalUser" }, allowSetters = true)
    private Set<PointTransaction> pointTransactions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "normalUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "normalUser" }, allowSetters = true)
    private Set<QuizProgress> quizProgresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "normalUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "normalUser" }, allowSetters = true)
    private Set<PointSpendOption> redemptionOptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveys", "analytics", "users", "adminUser" }, allowSetters = true)
    private Campaign campaign;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NormalUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public NormalUser userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public NormalUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public NormalUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public NormalUser firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public NormalUser lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return this.age;
    }

    public NormalUser age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJob() {
        return this.job;
    }

    public NormalUser job(String job) {
        this.setJob(job);
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getGender() {
        return this.gender;
    }

    public NormalUser gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return this.address;
    }

    public NormalUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEarnedPoints() {
        return this.earnedPoints;
    }

    public NormalUser earnedPoints(Integer earnedPoints) {
        this.setEarnedPoints(earnedPoints);
        return this;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Integer getStatus() {
        return this.status;
    }

    public NormalUser status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<SurveyParticipant> getParticipatedSurveys() {
        return this.participatedSurveys;
    }

    public void setParticipatedSurveys(Set<SurveyParticipant> surveyParticipants) {
        if (this.participatedSurveys != null) {
            this.participatedSurveys.forEach(i -> i.setNormalUser(null));
        }
        if (surveyParticipants != null) {
            surveyParticipants.forEach(i -> i.setNormalUser(this));
        }
        this.participatedSurveys = surveyParticipants;
    }

    public NormalUser participatedSurveys(Set<SurveyParticipant> surveyParticipants) {
        this.setParticipatedSurveys(surveyParticipants);
        return this;
    }

    public NormalUser addParticipatedSurveys(SurveyParticipant surveyParticipant) {
        this.participatedSurveys.add(surveyParticipant);
        surveyParticipant.setNormalUser(this);
        return this;
    }

    public NormalUser removeParticipatedSurveys(SurveyParticipant surveyParticipant) {
        this.participatedSurveys.remove(surveyParticipant);
        surveyParticipant.setNormalUser(null);
        return this;
    }

    public Set<FocusGroup> getFocusGroups() {
        return this.focusGroups;
    }

    public void setFocusGroups(Set<FocusGroup> focusGroups) {
        if (this.focusGroups != null) {
            this.focusGroups.forEach(i -> i.setNormalUser(null));
        }
        if (focusGroups != null) {
            focusGroups.forEach(i -> i.setNormalUser(this));
        }
        this.focusGroups = focusGroups;
    }

    public NormalUser focusGroups(Set<FocusGroup> focusGroups) {
        this.setFocusGroups(focusGroups);
        return this;
    }

    public NormalUser addFocusGroups(FocusGroup focusGroup) {
        this.focusGroups.add(focusGroup);
        focusGroup.setNormalUser(this);
        return this;
    }

    public NormalUser removeFocusGroups(FocusGroup focusGroup) {
        this.focusGroups.remove(focusGroup);
        focusGroup.setNormalUser(null);
        return this;
    }

    public Set<PointTransaction> getPointTransactions() {
        return this.pointTransactions;
    }

    public void setPointTransactions(Set<PointTransaction> pointTransactions) {
        if (this.pointTransactions != null) {
            this.pointTransactions.forEach(i -> i.setNormalUser(null));
        }
        if (pointTransactions != null) {
            pointTransactions.forEach(i -> i.setNormalUser(this));
        }
        this.pointTransactions = pointTransactions;
    }

    public NormalUser pointTransactions(Set<PointTransaction> pointTransactions) {
        this.setPointTransactions(pointTransactions);
        return this;
    }

    public NormalUser addPointTransactions(PointTransaction pointTransaction) {
        this.pointTransactions.add(pointTransaction);
        pointTransaction.setNormalUser(this);
        return this;
    }

    public NormalUser removePointTransactions(PointTransaction pointTransaction) {
        this.pointTransactions.remove(pointTransaction);
        pointTransaction.setNormalUser(null);
        return this;
    }

    public Set<QuizProgress> getQuizProgresses() {
        return this.quizProgresses;
    }

    public void setQuizProgresses(Set<QuizProgress> quizProgresses) {
        if (this.quizProgresses != null) {
            this.quizProgresses.forEach(i -> i.setNormalUser(null));
        }
        if (quizProgresses != null) {
            quizProgresses.forEach(i -> i.setNormalUser(this));
        }
        this.quizProgresses = quizProgresses;
    }

    public NormalUser quizProgresses(Set<QuizProgress> quizProgresses) {
        this.setQuizProgresses(quizProgresses);
        return this;
    }

    public NormalUser addQuizProgresses(QuizProgress quizProgress) {
        this.quizProgresses.add(quizProgress);
        quizProgress.setNormalUser(this);
        return this;
    }

    public NormalUser removeQuizProgresses(QuizProgress quizProgress) {
        this.quizProgresses.remove(quizProgress);
        quizProgress.setNormalUser(null);
        return this;
    }

    public Set<PointSpendOption> getRedemptionOptions() {
        return this.redemptionOptions;
    }

    public void setRedemptionOptions(Set<PointSpendOption> pointSpendOptions) {
        if (this.redemptionOptions != null) {
            this.redemptionOptions.forEach(i -> i.setNormalUser(null));
        }
        if (pointSpendOptions != null) {
            pointSpendOptions.forEach(i -> i.setNormalUser(this));
        }
        this.redemptionOptions = pointSpendOptions;
    }

    public NormalUser redemptionOptions(Set<PointSpendOption> pointSpendOptions) {
        this.setRedemptionOptions(pointSpendOptions);
        return this;
    }

    public NormalUser addRedemptionOptions(PointSpendOption pointSpendOption) {
        this.redemptionOptions.add(pointSpendOption);
        pointSpendOption.setNormalUser(this);
        return this;
    }

    public NormalUser removeRedemptionOptions(PointSpendOption pointSpendOption) {
        this.redemptionOptions.remove(pointSpendOption);
        pointSpendOption.setNormalUser(null);
        return this;
    }

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public NormalUser campaign(Campaign campaign) {
        this.setCampaign(campaign);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NormalUser)) {
            return false;
        }
        return getId() != null && getId().equals(((NormalUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NormalUser{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", age=" + getAge() +
            ", job='" + getJob() + "'" +
            ", gender='" + getGender() + "'" +
            ", address='" + getAddress() + "'" +
            ", earnedPoints=" + getEarnedPoints() +
            ", status=" + getStatus() +
            "}";
    }
}
