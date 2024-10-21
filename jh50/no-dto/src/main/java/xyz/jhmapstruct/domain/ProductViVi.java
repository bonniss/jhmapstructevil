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
 * A ProductViVi.
 */
@Entity
@Table(name = "product_vi_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductViVi implements Serializable {

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
    private CategoryViVi category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products", "payment", "shipment", "customer" }, allowSetters = true)
    private OrderViVi order;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<SupplierViVi> suppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductViVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductViVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductViVi price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return this.stock;
    }

    public ProductViVi stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductViVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryViVi getCategory() {
        return this.category;
    }

    public void setCategory(CategoryViVi categoryViVi) {
        this.category = categoryViVi;
    }

    public ProductViVi category(CategoryViVi categoryViVi) {
        this.setCategory(categoryViVi);
        return this;
    }

    public OrderViVi getOrder() {
        return this.order;
    }

    public void setOrder(OrderViVi orderViVi) {
        this.order = orderViVi;
    }

    public ProductViVi order(OrderViVi orderViVi) {
        this.setOrder(orderViVi);
        return this;
    }

    public Set<SupplierViVi> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<SupplierViVi> supplierViVis) {
        if (this.suppliers != null) {
            this.suppliers.forEach(i -> i.removeProducts(this));
        }
        if (supplierViVis != null) {
            supplierViVis.forEach(i -> i.addProducts(this));
        }
        this.suppliers = supplierViVis;
    }

    public ProductViVi suppliers(Set<SupplierViVi> supplierViVis) {
        this.setSuppliers(supplierViVis);
        return this;
    }

    public ProductViVi addSuppliers(SupplierViVi supplierViVi) {
        this.suppliers.add(supplierViVi);
        supplierViVi.getProducts().add(this);
        return this;
    }

    public ProductViVi removeSuppliers(SupplierViVi supplierViVi) {
        this.suppliers.remove(supplierViVi);
        supplierViVi.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductViVi)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductViVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductViVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
