package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.MissisipiType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.Magharetti} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MagharettiDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @NotNull
    @Size(min = 2, max = 256)
    private String label;

    private MissisipiType type;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MissisipiType getType() {
        return type;
    }

    public void setType(MissisipiType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MagharettiDTO)) {
            return false;
        }

        MagharettiDTO magharettiDTO = (MagharettiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, magharettiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MagharettiDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
