package xyz.jhmapstruct.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.ProductViVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductViViDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @Lob
    private String description;

    private CategoryViViDTO category;

    private MasterTenantDTO tenant;

    private OrderViViDTO order;

    private Set<SupplierViViDTO> suppliers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryViViDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryViViDTO category) {
        this.category = category;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public OrderViViDTO getOrder() {
        return order;
    }

    public void setOrder(OrderViViDTO order) {
        this.order = order;
    }

    public Set<SupplierViViDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<SupplierViViDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductViViDTO)) {
            return false;
        }

        ProductViViDTO productViViDTO = (ProductViViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productViViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductViViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", category=" + getCategory() +
            ", tenant=" + getTenant() +
            ", order=" + getOrder() +
            ", suppliers=" + getSuppliers() +
            "}";
    }
}
