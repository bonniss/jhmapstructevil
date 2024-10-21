package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.PeteType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPedroTaxVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPedroTaxViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    private Integer weight;

    private PeteType propertyType;

    private JohnLennonDTO application;

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public PeteType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
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
        if (!(o instanceof AlPedroTaxViDTO)) {
            return false;
        }

        AlPedroTaxViDTO alPedroTaxViDTO = (AlPedroTaxViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPedroTaxViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPedroTaxViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", propertyType='" + getPropertyType() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
