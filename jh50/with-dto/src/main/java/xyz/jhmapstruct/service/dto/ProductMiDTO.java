package xyz.jhmapstruct.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.ProductMi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductMiDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @Lob
    private String description;

    private CategoryMiDTO category;

    private OrderMiDTO order;

    private Set<SupplierMiDTO> suppliers = new HashSet<>();

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

    public CategoryMiDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryMiDTO category) {
        this.category = category;
    }

    public OrderMiDTO getOrder() {
        return order;
    }

    public void setOrder(OrderMiDTO order) {
        this.order = order;
    }

    public Set<SupplierMiDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<SupplierMiDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductMiDTO)) {
            return false;
        }

        ProductMiDTO productMiDTO = (ProductMiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productMiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductMiDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", category=" + getCategory() +
            ", order=" + getOrder() +
            ", suppliers=" + getSuppliers() +
            "}";
    }
}
