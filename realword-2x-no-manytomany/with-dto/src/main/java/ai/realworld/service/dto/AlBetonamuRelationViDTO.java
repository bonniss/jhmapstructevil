package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.ReductionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlBetonamuRelationVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBetonamuRelationViDTO implements Serializable {

    private Long id;

    @NotNull
    private ReductionType type;

    @NotNull
    private AlAlexTypeViDTO supplier;

    @NotNull
    private AlAlexTypeViDTO customer;

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

    public AlAlexTypeViDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(AlAlexTypeViDTO supplier) {
        this.supplier = supplier;
    }

    public AlAlexTypeViDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlAlexTypeViDTO customer) {
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
        if (!(o instanceof AlBetonamuRelationViDTO)) {
            return false;
        }

        AlBetonamuRelationViDTO alBetonamuRelationViDTO = (AlBetonamuRelationViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alBetonamuRelationViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBetonamuRelationViDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", supplier=" + getSupplier() +
            ", customer=" + getCustomer() +
            ", application=" + getApplication() +
            "}";
    }
}
