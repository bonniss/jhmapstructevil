package ai.realworld.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPowerShellVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPowerShellViDTO implements Serializable {

    private Long id;

    private String value;

    private AlProProViDTO propertyProfile;

    private AlPounderViDTO attributeTerm;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AlProProViDTO getPropertyProfile() {
        return propertyProfile;
    }

    public void setPropertyProfile(AlProProViDTO propertyProfile) {
        this.propertyProfile = propertyProfile;
    }

    public AlPounderViDTO getAttributeTerm() {
        return attributeTerm;
    }

    public void setAttributeTerm(AlPounderViDTO attributeTerm) {
        this.attributeTerm = attributeTerm;
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
        if (!(o instanceof AlPowerShellViDTO)) {
            return false;
        }

        AlPowerShellViDTO alPowerShellViDTO = (AlPowerShellViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPowerShellViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPowerShellViDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", propertyProfile=" + getPropertyProfile() +
            ", attributeTerm=" + getAttributeTerm() +
            ", application=" + getApplication() +
            "}";
    }
}
