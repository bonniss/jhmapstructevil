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
 * A AlPedroTaxVi.
 */
@Entity
@Table(name = "al_pedro_tax_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPedroTaxVi implements Serializable {

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
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeTaxonomy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attributeTaxonomy", "application" }, allowSetters = true)
    private Set<AlPounderVi> attributeTerms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPedroTaxVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlPedroTaxVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlPedroTaxVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public AlPedroTaxVi weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public PeteType getPropertyType() {
        return this.propertyType;
    }

    public AlPedroTaxVi propertyType(PeteType propertyType) {
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

    public AlPedroTaxVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlPounderVi> getAttributeTerms() {
        return this.attributeTerms;
    }

    public void setAttributeTerms(Set<AlPounderVi> alPounderVis) {
        if (this.attributeTerms != null) {
            this.attributeTerms.forEach(i -> i.setAttributeTaxonomy(null));
        }
        if (alPounderVis != null) {
            alPounderVis.forEach(i -> i.setAttributeTaxonomy(this));
        }
        this.attributeTerms = alPounderVis;
    }

    public AlPedroTaxVi attributeTerms(Set<AlPounderVi> alPounderVis) {
        this.setAttributeTerms(alPounderVis);
        return this;
    }

    public AlPedroTaxVi addAttributeTerms(AlPounderVi alPounderVi) {
        this.attributeTerms.add(alPounderVi);
        alPounderVi.setAttributeTaxonomy(this);
        return this;
    }

    public AlPedroTaxVi removeAttributeTerms(AlPounderVi alPounderVi) {
        this.attributeTerms.remove(alPounderVi);
        alPounderVi.setAttributeTaxonomy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPedroTaxVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPedroTaxVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPedroTaxVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", propertyType='" + getPropertyType() + "'" +
            "}";
    }
}
