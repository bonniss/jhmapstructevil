package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.PeteType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.realworld.domain.AlMenity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlMenityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private String iconSvg;

    private PeteType propertyType;

    private JohnLennonDTO application;

    private Set<AlProProDTO> propertyProfiles = new HashSet<>();

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

    public String getIconSvg() {
        return iconSvg;
    }

    public void setIconSvg(String iconSvg) {
        this.iconSvg = iconSvg;
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

    public Set<AlProProDTO> getPropertyProfiles() {
        return propertyProfiles;
    }

    public void setPropertyProfiles(Set<AlProProDTO> propertyProfiles) {
        this.propertyProfiles = propertyProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlMenityDTO)) {
            return false;
        }

        AlMenityDTO alMenityDTO = (AlMenityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alMenityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlMenityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", iconSvg='" + getIconSvg() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", application=" + getApplication() +
            ", propertyProfiles=" + getPropertyProfiles() +
            "}";
    }
}
