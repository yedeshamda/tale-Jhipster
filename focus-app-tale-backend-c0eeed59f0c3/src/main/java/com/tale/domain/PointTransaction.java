package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PointTransaction.
 */
@Entity
@Table(name = "point_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PointTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "description")
    private String description;

    @Column(name = "points")
    private Integer points;

    @Column(name = "transaction_date")
    private ZonedDateTime transactionDate;

    @Column(name = "user_id")
    private Integer userId;

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

    public PointTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTransactionId() {
        return this.transactionId;
    }

    public PointTransaction transactionId(Integer transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return this.description;
    }

    public PointTransaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return this.points;
    }

    public PointTransaction points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ZonedDateTime getTransactionDate() {
        return this.transactionDate;
    }

    public PointTransaction transactionDate(ZonedDateTime transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public PointTransaction userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public NormalUser getNormalUser() {
        return this.normalUser;
    }

    public void setNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    public PointTransaction normalUser(NormalUser normalUser) {
        this.setNormalUser(normalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((PointTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointTransaction{" +
            "id=" + getId() +
            ", transactionId=" + getTransactionId() +
            ", description='" + getDescription() + "'" +
            ", points=" + getPoints() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
