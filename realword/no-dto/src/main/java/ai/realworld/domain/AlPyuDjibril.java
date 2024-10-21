package ai.realworld.domain;

import ai.realworld.domain.enumeration.BenedictRiottaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPyuDjibril.
 */
@Entity
@Table(name = "al_pyu_djibril")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuDjibril implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type")
    private BenedictRiottaType rateType;

    @Column(name = "rate", precision = 21, scale = 2)
    private BigDecimal rate;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "parent", "operator", "propertyProfile", "avatar", "application", "images", "children", "bookings" },
        allowSetters = true
    )
    private AlProty property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPyuDjibril id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BenedictRiottaType getRateType() {
        return this.rateType;
    }

    public AlPyuDjibril rateType(BenedictRiottaType rateType) {
        this.setRateType(rateType);
        return this;
    }

    public void setRateType(BenedictRiottaType rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public AlPyuDjibril rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlPyuDjibril isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProty getProperty() {
        return this.property;
    }

    public void setProperty(AlProty alProty) {
        this.property = alProty;
    }

    public AlPyuDjibril property(AlProty alProty) {
        this.setProperty(alProty);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPyuDjibril application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPyuDjibril)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPyuDjibril) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuDjibril{" +
            "id=" + getId() +
            ", rateType='" + getRateType() + "'" +
            ", rate=" + getRate() +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
