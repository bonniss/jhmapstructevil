package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextSupplier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextSupplierDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String contactPerson;

    private String email;

    private String phoneNumber;

    private MasterTenantDTO tenant;

    private Set<NextProductDTO> products = new HashSet<>();

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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public Set<NextProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<NextProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextSupplierDTO)) {
            return false;
        }

        NextSupplierDTO nextSupplierDTO = (NextSupplierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextSupplierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextSupplierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", tenant=" + getTenant() +
            ", products=" + getProducts() +
            "}";
    }
}
