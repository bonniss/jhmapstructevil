package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPounderVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPounderViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private Integer weight;

    @NotNull
    private AlPedroTaxViDTO attributeTaxonomy;

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public AlPedroTaxViDTO getAttributeTaxonomy() {
        return attributeTaxonomy;
    }

    public void setAttributeTaxonomy(AlPedroTaxViDTO attributeTaxonomy) {
        this.attributeTaxonomy = attributeTaxonomy;
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
        if (!(o instanceof AlPounderViDTO)) {
            return false;
        }

        AlPounderViDTO alPounderViDTO = (AlPounderViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPounderViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPounderViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", attributeTaxonomy=" + getAttributeTaxonomy() +
            ", application=" + getApplication() +
            "}";
    }
}
