package ai.realworld.domain;

import ai.realworld.domain.enumeration.PeteType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPedroTax.
 */
@Entity
@Table(name = "al_pedro_tax")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPedroTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "weight")
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PeteType propertyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeTaxonomy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attributeTaxonomy", "application" }, allowSetters = true)
    private Set<AlPounder> attributeTerms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPedroTax id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlPedroTax name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlPedroTax description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public AlPedroTax weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public PeteType getPropertyType() {
        return this.propertyType;
    }

    public AlPedroTax propertyType(PeteType propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPedroTax application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlPounder> getAttributeTerms() {
        return this.attributeTerms;
    }

    public void setAttributeTerms(Set<AlPounder> alPounders) {
        if (this.attributeTerms != null) {
            this.attributeTerms.forEach(i -> i.setAttributeTaxonomy(null));
        }
        if (alPounders != null) {
            alPounders.forEach(i -> i.setAttributeTaxonomy(this));
        }
        this.attributeTerms = alPounders;
    }

    public AlPedroTax attributeTerms(Set<AlPounder> alPounders) {
        this.setAttributeTerms(alPounders);
        return this;
    }

    public AlPedroTax addAttributeTerms(AlPounder alPounder) {
        this.attributeTerms.add(alPounder);
        alPounder.setAttributeTaxonomy(this);
        return this;
    }

    public AlPedroTax removeAttributeTerms(AlPounder alPounder) {
        this.attributeTerms.remove(alPounder);
        alPounder.setAttributeTaxonomy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPedroTax)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPedroTax) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPedroTax{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", propertyType='" + getPropertyType() + "'" +
            "}";
    }
}
