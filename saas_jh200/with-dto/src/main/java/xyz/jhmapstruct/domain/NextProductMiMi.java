package xyz.jhmapstruct.domain;

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
 * A NextProductMiMi.
 */
@Entity
@Table(name = "next_product_mi_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextProductMiMi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextCategoryMiMi category;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products", "payment", "shipment", "tenant", "customer" }, allowSetters = true)
    private NextOrderMiMi order;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tenant", "products" }, allowSetters = true)
    private Set<NextSupplierMiMi> suppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextProductMiMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public NextProductMiMi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public NextProductMiMi price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return this.stock;
    }

    public NextProductMiMi stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return this.description;
    }

    public NextProductMiMi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NextCategoryMiMi getCategory() {
        return this.category;
    }

    public void setCategory(NextCategoryMiMi nextCategoryMiMi) {
        this.category = nextCategoryMiMi;
    }

    public NextProductMiMi category(NextCategoryMiMi nextCategoryMiMi) {
        this.setCategory(nextCategoryMiMi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextProductMiMi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextOrderMiMi getOrder() {
        return this.order;
    }

    public void setOrder(NextOrderMiMi nextOrderMiMi) {
        this.order = nextOrderMiMi;
    }

    public NextProductMiMi order(NextOrderMiMi nextOrderMiMi) {
        this.setOrder(nextOrderMiMi);
        return this;
    }

    public Set<NextSupplierMiMi> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<NextSupplierMiMi> nextSupplierMiMis) {
        if (this.suppliers != null) {
            this.suppliers.forEach(i -> i.removeProducts(this));
        }
        if (nextSupplierMiMis != null) {
            nextSupplierMiMis.forEach(i -> i.addProducts(this));
        }
        this.suppliers = nextSupplierMiMis;
    }

    public NextProductMiMi suppliers(Set<NextSupplierMiMi> nextSupplierMiMis) {
        this.setSuppliers(nextSupplierMiMis);
        return this;
    }

    public NextProductMiMi addSuppliers(NextSupplierMiMi nextSupplierMiMi) {
        this.suppliers.add(nextSupplierMiMi);
        nextSupplierMiMi.getProducts().add(this);
        return this;
    }

    public NextProductMiMi removeSuppliers(NextSupplierMiMi nextSupplierMiMi) {
        this.suppliers.remove(nextSupplierMiMi);
        nextSupplierMiMi.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextProductMiMi)) {
            return false;
        }
        return getId() != null && getId().equals(((NextProductMiMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextProductMiMi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
