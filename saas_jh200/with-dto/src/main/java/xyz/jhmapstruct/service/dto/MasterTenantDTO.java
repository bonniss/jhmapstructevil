package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.MasterTenant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterTenantDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterTenantDTO)) {
            return false;
        }

        MasterTenantDTO masterTenantDTO = (MasterTenantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, masterTenantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterTenantDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
