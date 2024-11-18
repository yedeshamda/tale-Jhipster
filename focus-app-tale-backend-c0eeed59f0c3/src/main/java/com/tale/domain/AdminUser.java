package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdminUser.
 */
@Entity
@Table(name = "admin_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "admin_id")
    private Integer adminId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adminUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "surveys", "analytics", "users", "adminUser" }, allowSetters = true)
    private Set<Campaign> campaigns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdminUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public AdminUser adminId(Integer adminId) {
        this.setAdminId(adminId);
        return this;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Set<Campaign> getCampaigns() {
        return this.campaigns;
    }

    public void setCampaigns(Set<Campaign> campaigns) {
        if (this.campaigns != null) {
            this.campaigns.forEach(i -> i.setAdminUser(null));
        }
        if (campaigns != null) {
            campaigns.forEach(i -> i.setAdminUser(this));
        }
        this.campaigns = campaigns;
    }

    public AdminUser campaigns(Set<Campaign> campaigns) {
        this.setCampaigns(campaigns);
        return this;
    }

    public AdminUser addCampaign(Campaign campaign) {
        this.campaigns.add(campaign);
        campaign.setAdminUser(this);
        return this;
    }

    public AdminUser removeCampaign(Campaign campaign) {
        this.campaigns.remove(campaign);
        campaign.setAdminUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AdminUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUser{" +
            "id=" + getId() +
            ", adminId=" + getAdminId() +
            "}";
    }
}
