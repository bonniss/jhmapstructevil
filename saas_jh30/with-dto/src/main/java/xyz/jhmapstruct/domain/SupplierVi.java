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
 * A SupplierVi.
 */
@Entity
@Table(name = "supplier_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierVi implements Serializable {

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
        name = "rel_supplier_vi__products",
        joinColumns = @JoinColumn(name = "supplier_vi_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductVi> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupplierVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SupplierVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public SupplierVi contactPerson(String contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return this.email;
    }

    public SupplierVi email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public SupplierVi phoneNumber(String phoneNumber) {
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

    public SupplierVi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public Set<ProductVi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductVi> productVis) {
        this.products = productVis;
    }

    public SupplierVi products(Set<ProductVi> productVis) {
        this.setProducts(productVis);
        return this;
    }

    public SupplierVi addProducts(ProductVi productVi) {
        this.products.add(productVi);
        return this;
    }

    public SupplierVi removeProducts(ProductVi productVi) {
        this.products.remove(productVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierVi)) {
            return false;
        }
        return getId() != null && getId().equals(((SupplierVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
