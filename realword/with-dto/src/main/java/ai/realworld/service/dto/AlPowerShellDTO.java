package ai.realworld.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPowerShell} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPowerShellDTO implements Serializable {

    private Long id;

    private String value;

    private AlProProDTO propertyProfile;

    private AlPounderDTO attributeTerm;

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

    public AlProProDTO getPropertyProfile() {
        return propertyProfile;
    }

    public void setPropertyProfile(AlProProDTO propertyProfile) {
        this.propertyProfile = propertyProfile;
    }

    public AlPounderDTO getAttributeTerm() {
        return attributeTerm;
    }

    public void setAttributeTerm(AlPounderDTO attributeTerm) {
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
        if (!(o instanceof AlPowerShellDTO)) {
            return false;
        }

        AlPowerShellDTO alPowerShellDTO = (AlPowerShellDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPowerShellDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPowerShellDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", propertyProfile=" + getPropertyProfile() +
            ", attributeTerm=" + getAttributeTerm() +
            ", application=" + getApplication() +
            "}";
    }
}
