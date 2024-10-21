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
 * A AlVueVueUsage.
 */
@Entity
@Table(name = "al_vue_vue_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alVueVueUsage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "image", "alVueVueUsage", "application", "conditions" }, allowSetters = true)
    private Set<AlVueVue> vouchers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alVueVueUsage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private Set<AlPacino> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlVueVueUsage id(UUID id) {
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

    public AlVueVueUsage application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlVueVue> getVouchers() {
        return this.vouchers;
    }

    public void setVouchers(Set<AlVueVue> alVueVues) {
        if (this.vouchers != null) {
            this.vouchers.forEach(i -> i.setAlVueVueUsage(null));
        }
        if (alVueVues != null) {
            alVueVues.forEach(i -> i.setAlVueVueUsage(this));
        }
        this.vouchers = alVueVues;
    }

    public AlVueVueUsage vouchers(Set<AlVueVue> alVueVues) {
        this.setVouchers(alVueVues);
        return this;
    }

    public AlVueVueUsage addVoucher(AlVueVue alVueVue) {
        this.vouchers.add(alVueVue);
        alVueVue.setAlVueVueUsage(this);
        return this;
    }

    public AlVueVueUsage removeVoucher(AlVueVue alVueVue) {
        this.vouchers.remove(alVueVue);
        alVueVue.setAlVueVueUsage(null);
        return this;
    }

    public Set<AlPacino> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<AlPacino> alPacinos) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setAlVueVueUsage(null));
        }
        if (alPacinos != null) {
            alPacinos.forEach(i -> i.setAlVueVueUsage(this));
        }
        this.customers = alPacinos;
    }

    public AlVueVueUsage customers(Set<AlPacino> alPacinos) {
        this.setCustomers(alPacinos);
        return this;
    }

    public AlVueVueUsage addCustomer(AlPacino alPacino) {
        this.customers.add(alPacino);
        alPacino.setAlVueVueUsage(this);
        return this;
    }

    public AlVueVueUsage removeCustomer(AlPacino alPacino) {
        this.customers.remove(alPacino);
        alPacino.setAlVueVueUsage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((AlVueVueUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueUsage{" +
            "id=" + getId() +
            "}";
    }
}
