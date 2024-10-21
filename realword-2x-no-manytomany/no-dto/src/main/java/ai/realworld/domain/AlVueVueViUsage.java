package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlVueVueViUsage.
 */
@Entity
@Table(name = "al_vue_vue_vi_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueViUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alVueVueViUsage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "image", "alVueVueViUsage", "application", "conditions" }, allowSetters = true)
    private Set<AlVueVueVi> vouchers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alVueVueViUsage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "application", "membershipTier", "alVueVueUsage", "membershipTierVi", "alVueVueViUsage" },
        allowSetters = true
    )
    private Set<AlPacino> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlVueVueViUsage id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlVueVueViUsage application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlVueVueVi> getVouchers() {
        return this.vouchers;
    }

    public void setVouchers(Set<AlVueVueVi> alVueVueVis) {
        if (this.vouchers != null) {
            this.vouchers.forEach(i -> i.setAlVueVueViUsage(null));
        }
        if (alVueVueVis != null) {
            alVueVueVis.forEach(i -> i.setAlVueVueViUsage(this));
        }
        this.vouchers = alVueVueVis;
    }

    public AlVueVueViUsage vouchers(Set<AlVueVueVi> alVueVueVis) {
        this.setVouchers(alVueVueVis);
        return this;
    }

    public AlVueVueViUsage addVoucher(AlVueVueVi alVueVueVi) {
        this.vouchers.add(alVueVueVi);
        alVueVueVi.setAlVueVueViUsage(this);
        return this;
    }

    public AlVueVueViUsage removeVoucher(AlVueVueVi alVueVueVi) {
        this.vouchers.remove(alVueVueVi);
        alVueVueVi.setAlVueVueViUsage(null);
        return this;
    }

    public Set<AlPacino> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<AlPacino> alPacinos) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setAlVueVueViUsage(null));
        }
        if (alPacinos != null) {
            alPacinos.forEach(i -> i.setAlVueVueViUsage(this));
        }
        this.customers = alPacinos;
    }

    public AlVueVueViUsage customers(Set<AlPacino> alPacinos) {
        this.setCustomers(alPacinos);
        return this;
    }

    public AlVueVueViUsage addCustomer(AlPacino alPacino) {
        this.customers.add(alPacino);
        alPacino.setAlVueVueViUsage(this);
        return this;
    }

    public AlVueVueViUsage removeCustomer(AlPacino alPacino) {
        this.customers.remove(alPacino);
        alPacino.setAlVueVueViUsage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueViUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((AlVueVueViUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueViUsage{" +
            "id=" + getId() +
            "}";
    }
}
