package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OurDatabases.
 */
@Entity
@Table(name = "our_databases")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OurDatabases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "database_id")
    private Integer databaseId;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ourDatabases")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "responseSurveys", "participants", "campaign", "ourDatabases" }, allowSetters = true)
    private Set<Survey> surveyDatabases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OurDatabases id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDatabaseId() {
        return this.databaseId;
    }

    public OurDatabases databaseId(Integer databaseId) {
        this.setDatabaseId(databaseId);
        return this;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public String getName() {
        return this.name;
    }

    public OurDatabases name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Survey> getSurveyDatabases() {
        return this.surveyDatabases;
    }

    public void setSurveyDatabases(Set<Survey> surveys) {
        if (this.surveyDatabases != null) {
            this.surveyDatabases.forEach(i -> i.setOurDatabases(null));
        }
        if (surveys != null) {
            surveys.forEach(i -> i.setOurDatabases(this));
        }
        this.surveyDatabases = surveys;
    }

    public OurDatabases surveyDatabases(Set<Survey> surveys) {
        this.setSurveyDatabases(surveys);
        return this;
    }

    public OurDatabases addSurveyDatabase(Survey survey) {
        this.surveyDatabases.add(survey);
        survey.setOurDatabases(this);
        return this;
    }

    public OurDatabases removeSurveyDatabase(Survey survey) {
        this.surveyDatabases.remove(survey);
        survey.setOurDatabases(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OurDatabases)) {
            return false;
        }
        return getId() != null && getId().equals(((OurDatabases) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OurDatabases{" +
            "id=" + getId() +
            ", databaseId=" + getDatabaseId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
