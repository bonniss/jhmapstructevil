package xyz.jhmapstruct.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextProductAlpha} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextProductAlphaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @Lob
    private String description;

    private NextCategoryAlphaDTO category;

    private MasterTenantDTO tenant;

    private NextOrderAlphaDTO order;

    private Set<NextSupplierAlphaDTO> suppliers = new HashSet<>();

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

    public NextCategoryAlphaDTO getCategory() {
        return category;
    }

    public void setCategory(NextCategoryAlphaDTO category) {
        this.category = category;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextOrderAlphaDTO getOrder() {
        return order;
    }

    public void setOrder(NextOrderAlphaDTO order) {
        this.order = order;
    }

    public Set<NextSupplierAlphaDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<NextSupplierAlphaDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextProductAlphaDTO)) {
            return false;
        }

        NextProductAlphaDTO nextProductAlphaDTO = (NextProductAlphaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextProductAlphaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextProductAlphaDTO{" +
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
