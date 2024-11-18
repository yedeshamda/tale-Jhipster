package com.tale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo") // New logo attribute
    private String logo; // Assuming logo will be stored as a URL or path to the image
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Survey> surveys = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }
    // Getter and setter for logo
    public String getLogo() {
        return logo;
    }

    public Product logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public Product setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
        return this;
    }

    public Product addSurvey(Survey survey) {
        this.surveys.add(survey);
        survey.setProduct(this); // Set the back-reference
        return this;
    }

    public Product removeSurvey(Survey survey) {
        this.surveys.remove(survey);
        survey.setProduct(null); // Remove the back-reference
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", logo='" + getLogo() + "'" +
            "}";
    }
}
