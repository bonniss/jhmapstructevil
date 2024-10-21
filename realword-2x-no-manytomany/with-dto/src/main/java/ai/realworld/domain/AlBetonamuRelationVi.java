package ai.realworld.domain;

import ai.realworld.domain.enumeration.ReductionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlBetonamuRelationVi.
 */
@Entity
@Table(name = "al_betonamu_relation_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBetonamuRelationVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReductionType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "asSuppliers", "asCustomers", "agencies" }, allowSetters = true)
    private AlAlexTypeVi supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "asSuppliers", "asCustomers", "agencies" }, allowSetters = true)
    private AlAlexTypeVi customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bizRelationVi")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bizRelation", "application", "bizRelationVi", "conditions", "conditionVis" }, allowSetters = true)
    private Set<AlGore> discounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlBetonamuRelationVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReductionType getType() {
        return this.type;
    }

    public AlBetonamuRelationVi type(ReductionType type) {
        this.setType(type);
        return this;
    }

    public void setType(ReductionType type) {
        this.type = type;
    }

    public AlAlexTypeVi getSupplier() {
        return this.supplier;
    }

    public void setSupplier(AlAlexTypeVi alAlexTypeVi) {
        this.supplier = alAlexTypeVi;
    }

    public AlBetonamuRelationVi supplier(AlAlexTypeVi alAlexTypeVi) {
        this.setSupplier(alAlexTypeVi);
        return this;
    }

    public AlAlexTypeVi getCustomer() {
        return this.customer;
    }

    public void setCustomer(AlAlexTypeVi alAlexTypeVi) {
        this.customer = alAlexTypeVi;
    }

    public AlBetonamuRelationVi customer(AlAlexTypeVi alAlexTypeVi) {
        this.setCustomer(alAlexTypeVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlBetonamuRelationVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlGore> getDiscounts() {
        return this.discounts;
    }

    public void setDiscounts(Set<AlGore> alGores) {
        if (this.discounts != null) {
            this.discounts.forEach(i -> i.setBizRelationVi(null));
        }
        if (alGores != null) {
            alGores.forEach(i -> i.setBizRelationVi(this));
        }
        this.discounts = alGores;
    }

    public AlBetonamuRelationVi discounts(Set<AlGore> alGores) {
        this.setDiscounts(alGores);
        return this;
    }

    public AlBetonamuRelationVi addDiscounts(AlGore alGore) {
        this.discounts.add(alGore);
        alGore.setBizRelationVi(this);
        return this;
    }

    public AlBetonamuRelationVi removeDiscounts(AlGore alGore) {
        this.discounts.remove(alGore);
        alGore.setBizRelationVi(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBetonamuRelationVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlBetonamuRelationVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBetonamuRelationVi{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
