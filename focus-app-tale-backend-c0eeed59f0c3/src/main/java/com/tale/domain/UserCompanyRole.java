package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserCompanyRole.
 */
@Entity
@Table(name = "user_company_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCompanyRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userRoles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userRoles", "company" }, allowSetters = true)
    private Set<CompanyUser> companyUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserCompanyRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public UserCompanyRole roleId(Integer roleId) {
        this.setRoleId(roleId);
        return this;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public UserCompanyRole roleName(String roleName) {
        this.setRoleName(roleName);
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public UserCompanyRole description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return this.status;
    }

    public UserCompanyRole status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<CompanyUser> getCompanyUsers() {
        return this.companyUsers;
    }

    public void setCompanyUsers(Set<CompanyUser> companyUsers) {
        if (this.companyUsers != null) {
            this.companyUsers.forEach(i -> i.removeUserRoles(this));
        }
        if (companyUsers != null) {
            companyUsers.forEach(i -> i.addUserRoles(this));
        }
        this.companyUsers = companyUsers;
    }

    public UserCompanyRole companyUsers(Set<CompanyUser> companyUsers) {
        this.setCompanyUsers(companyUsers);
        return this;
    }

    public UserCompanyRole addCompanyUsers(CompanyUser companyUser) {
        this.companyUsers.add(companyUser);
        companyUser.getUserRoles().add(this);
        return this;
    }

    public UserCompanyRole removeCompanyUsers(CompanyUser companyUser) {
        this.companyUsers.remove(companyUser);
        companyUser.getUserRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCompanyRole)) {
            return false;
        }
        return getId() != null && getId().equals(((UserCompanyRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCompanyRole{" +
            "id=" + getId() +
            ", roleId=" + getRoleId() +
            ", roleName='" + getRoleName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
