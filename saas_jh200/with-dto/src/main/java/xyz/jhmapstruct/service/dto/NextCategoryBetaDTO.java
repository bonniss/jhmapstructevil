package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextCategoryBeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextCategoryBetaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private MasterTenantDTO tenant;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextCategoryBetaDTO)) {
            return false;
        }

        NextCategoryBetaDTO nextCategoryBetaDTO = (NextCategoryBetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextCategoryBetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextCategoryBetaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
