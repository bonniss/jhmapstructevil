package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlGore.
 */
@Entity
@Table(name = "al_gore")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private AlcountTypo discountType;

    @Column(name = "discount_rate", precision = 21, scale = 2)
    private BigDecimal discountRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private AlcountScopy scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "supplier", "customer", "application", "discounts" }, allowSetters = true)
    private AlBetonamuRelation bizRelation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "application" }, allowSetters = true)
    private Set<AlGoreCondition> conditions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlGore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlGore name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlcountTypo getDiscountType() {
        return this.discountType;
    }

    public AlGore discountType(AlcountTypo discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(AlcountTypo discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountRate() {
        return this.discountRate;
    }

    public AlGore discountRate(BigDecimal discountRate) {
        this.setDiscountRate(discountRate);
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopy getScope() {
        return this.scope;
    }

    public AlGore scope(AlcountScopy scope) {
        this.setScope(scope);
        return this;
    }

    public void setScope(AlcountScopy scope) {
        this.scope = scope;
    }

    public AlBetonamuRelation getBizRelation() {
        return this.bizRelation;
    }

    public void setBizRelation(AlBetonamuRelation alBetonamuRelation) {
        this.bizRelation = alBetonamuRelation;
    }

    public AlGore bizRelation(AlBetonamuRelation alBetonamuRelation) {
        this.setBizRelation(alBetonamuRelation);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlGore application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlGoreCondition> getConditions() {
        return this.conditions;
    }

    public void setConditions(Set<AlGoreCondition> alGoreConditions) {
        if (this.conditions != null) {
            this.conditions.forEach(i -> i.setParent(null));
        }
        if (alGoreConditions != null) {
            alGoreConditions.forEach(i -> i.setParent(this));
        }
        this.conditions = alGoreConditions;
    }

    public AlGore conditions(Set<AlGoreCondition> alGoreConditions) {
        this.setConditions(alGoreConditions);
        return this;
    }

    public AlGore addConditions(AlGoreCondition alGoreCondition) {
        this.conditions.add(alGoreCondition);
        alGoreCondition.setParent(this);
        return this;
    }

    public AlGore removeConditions(AlGoreCondition alGoreCondition) {
        this.conditions.remove(alGoreCondition);
        alGoreCondition.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlGore)) {
            return false;
        }
        return getId() != null && getId().equals(((AlGore) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGore{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", scope='" + getScope() + "'" +
            "}";
    }
}
