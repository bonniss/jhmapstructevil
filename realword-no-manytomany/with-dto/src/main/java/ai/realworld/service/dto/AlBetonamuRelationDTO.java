package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.ReductionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlBetonamuRelation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBetonamuRelationDTO implements Serializable {

    private Long id;

    @NotNull
    private ReductionType type;

    @NotNull
    private AlAlexTypeDTO supplier;

    @NotNull
    private AlAlexTypeDTO customer;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReductionType getType() {
        return type;
    }

    public void setType(ReductionType type) {
        this.type = type;
    }

    public AlAlexTypeDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(AlAlexTypeDTO supplier) {
        this.supplier = supplier;
    }

    public AlAlexTypeDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlAlexTypeDTO customer) {
        this.customer = customer;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBetonamuRelationDTO)) {
            return false;
        }

        AlBetonamuRelationDTO alBetonamuRelationDTO = (AlBetonamuRelationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alBetonamuRelationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBetonamuRelationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", supplier=" + getSupplier() +
            ", customer=" + getCustomer() +
            ", application=" + getApplication() +
            "}";
    }
}
