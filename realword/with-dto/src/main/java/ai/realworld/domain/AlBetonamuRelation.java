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
 * A AlBetonamuRelation.
 */
@Entity
@Table(name = "al_betonamu_relation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBetonamuRelation implements Serializable {

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
    private AlAlexType supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "asSuppliers", "asCustomers", "agencies" }, allowSetters = true)
    private AlAlexType customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bizRelation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bizRelation", "application", "conditions" }, allowSetters = true)
    private Set<AlGore> discounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlBetonamuRelation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReductionType getType() {
        return this.type;
    }

    public AlBetonamuRelation type(ReductionType type) {
        this.setType(type);
        return this;
    }

    public void setType(ReductionType type) {
        this.type = type;
    }

    public AlAlexType getSupplier() {
        return this.supplier;
    }

    public void setSupplier(AlAlexType alAlexType) {
        this.supplier = alAlexType;
    }

    public AlBetonamuRelation supplier(AlAlexType alAlexType) {
        this.setSupplier(alAlexType);
        return this;
    }

    public AlAlexType getCustomer() {
        return this.customer;
    }

    public void setCustomer(AlAlexType alAlexType) {
        this.customer = alAlexType;
    }

    public AlBetonamuRelation customer(AlAlexType alAlexType) {
        this.setCustomer(alAlexType);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlBetonamuRelation application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlGore> getDiscounts() {
        return this.discounts;
    }

    public void setDiscounts(Set<AlGore> alGores) {
        if (this.discounts != null) {
            this.discounts.forEach(i -> i.setBizRelation(null));
        }
        if (alGores != null) {
            alGores.forEach(i -> i.setBizRelation(this));
        }
        this.discounts = alGores;
    }

    public AlBetonamuRelation discounts(Set<AlGore> alGores) {
        this.setDiscounts(alGores);
        return this;
    }

    public AlBetonamuRelation addDiscounts(AlGore alGore) {
        this.discounts.add(alGore);
        alGore.setBizRelation(this);
        return this;
    }

    public AlBetonamuRelation removeDiscounts(AlGore alGore) {
        this.discounts.remove(alGore);
        alGore.setBizRelation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBetonamuRelation)) {
            return false;
        }
        return getId() != null && getId().equals(((AlBetonamuRelation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBetonamuRelation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
