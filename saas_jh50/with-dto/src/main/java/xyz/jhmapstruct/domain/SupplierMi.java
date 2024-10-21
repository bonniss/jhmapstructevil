package xyz.jhmapstruct.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupplierMi.
 */
@Entity
@Table(name = "supplier_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierMi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_supplier_mi__products",
        joinColumns = @JoinColumn(name = "supplier_mi_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductMi> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupplierMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SupplierMi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public SupplierMi contactPerson(String contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return this.email;
    }

    public SupplierMi email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public SupplierMi phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public SupplierMi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public Set<ProductMi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductMi> productMis) {
        this.products = productMis;
    }

    public SupplierMi products(Set<ProductMi> productMis) {
        this.setProducts(productMis);
        return this;
    }

    public SupplierMi addProducts(ProductMi productMi) {
        this.products.add(productMi);
        return this;
    }

    public SupplierMi removeProducts(ProductMi productMi) {
        this.products.remove(productMi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierMi)) {
            return false;
        }
        return getId() != null && getId().equals(((SupplierMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierMi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
