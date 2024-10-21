package xyz.jhmapstruct.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.ProductMiMi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductMiMiDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @Lob
    private String description;

    private CategoryMiMiDTO category;

    private OrderMiMiDTO order;

    private Set<SupplierMiMiDTO> suppliers = new HashSet<>();

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

    public CategoryMiMiDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryMiMiDTO category) {
        this.category = category;
    }

    public OrderMiMiDTO getOrder() {
        return order;
    }

    public void setOrder(OrderMiMiDTO order) {
        this.order = order;
    }

    public Set<SupplierMiMiDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<SupplierMiMiDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductMiMiDTO)) {
            return false;
        }

        ProductMiMiDTO productMiMiDTO = (ProductMiMiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productMiMiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductMiMiDTO{" +
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
