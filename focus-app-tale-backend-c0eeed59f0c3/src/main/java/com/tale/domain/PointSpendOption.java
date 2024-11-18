package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PointSpendOption.
 */
@Entity
@Table(name = "point_spend_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PointSpendOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "redemption_option_id")
    private Integer redemptionOptionId;

    @Column(name = "description")
    private String description;

    @Column(name = "points_required")
    private Integer pointsRequired;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "participatedSurveys", "focusGroups", "pointTransactions", "quizProgresses", "redemptionOptions", "campaign" },
        allowSetters = true
    )
    private NormalUser normalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PointSpendOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRedemptionOptionId() {
        return this.redemptionOptionId;
    }

    public PointSpendOption redemptionOptionId(Integer redemptionOptionId) {
        this.setRedemptionOptionId(redemptionOptionId);
        return this;
    }

    public void setRedemptionOptionId(Integer redemptionOptionId) {
        this.redemptionOptionId = redemptionOptionId;
    }

    public String getDescription() {
        return this.description;
    }

    public PointSpendOption description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsRequired() {
        return this.pointsRequired;
    }

    public PointSpendOption pointsRequired(Integer pointsRequired) {
        this.setPointsRequired(pointsRequired);
        return this;
    }

    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public Integer getAvailableQuantity() {
        return this.availableQuantity;
    }

    public PointSpendOption availableQuantity(Integer availableQuantity) {
        this.setAvailableQuantity(availableQuantity);
        return this;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public ZonedDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public PointSpendOption expirationDate(ZonedDateTime expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public PointSpendOption normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointSpendOption)) {
            return false;
        }
        return getId() != null && getId().equals(((PointSpendOption) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointSpendOption{" +
            "id=" + getId() +
            ", redemptionOptionId=" + getRedemptionOptionId() +
            ", description='" + getDescription() + "'" +
            ", pointsRequired=" + getPointsRequired() +
            ", availableQuantity=" + getAvailableQuantity() +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
