package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlGoreVi.
 */
@Entity
@Table(name = "al_gore_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreVi implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlGoreVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlGoreVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlcountTypo getDiscountType() {
        return this.discountType;
    }

    public AlGoreVi discountType(AlcountTypo discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(AlcountTypo discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountRate() {
        return this.discountRate;
    }

    public AlGoreVi discountRate(BigDecimal discountRate) {
        this.setDiscountRate(discountRate);
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopy getScope() {
        return this.scope;
    }

    public AlGoreVi scope(AlcountScopy scope) {
        this.setScope(scope);
        return this;
    }

    public void setScope(AlcountScopy scope) {
        this.scope = scope;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlGoreVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlGoreVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", scope='" + getScope() + "'" +
            "}";
    }
}
