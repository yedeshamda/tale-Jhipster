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
 * A CompanyUser.
 */
@Entity
@Table(name = "company_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "company_user_id")
    private Integer companyUserId;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_company_user__user_roles",
        joinColumns = @JoinColumn(name = "company_user_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "companyUsers" }, allowSetters = true)
    private Set<UserCompanyRole> userRoles = new HashSet<>();

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

    public CompanyUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyUserId() {
        return this.companyUserId;
    }

    public CompanyUser companyUserId(Integer companyUserId) {
        this.setCompanyUserId(companyUserId);
        return this;
    }

    public void setCompanyUserId(Integer companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getUsername() {
        return this.username;
    }

    public CompanyUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public CompanyUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public CompanyUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public CompanyUser phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public CompanyUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public CompanyUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public CompanyUser jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getStatus() {
        return this.status;
    }

    public CompanyUser status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<UserCompanyRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserCompanyRole> userCompanyRoles) {
        this.userRoles = userCompanyRoles;
    }

    public CompanyUser userRoles(Set<UserCompanyRole> userCompanyRoles) {
        this.setUserRoles(userCompanyRoles);
        return this;
    }

    public CompanyUser addUserRoles(UserCompanyRole userCompanyRole) {
        this.userRoles.add(userCompanyRole);
        return this;
    }

    public CompanyUser removeUserRoles(UserCompanyRole userCompanyRole) {
        this.userRoles.remove(userCompanyRole);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyUser company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUser)) {
            return false;
        }
        return getId() != null && getId().equals(((CompanyUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyUser{" +
            "id=" + getId() +
            ", companyUserId=" + getCompanyUserId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
