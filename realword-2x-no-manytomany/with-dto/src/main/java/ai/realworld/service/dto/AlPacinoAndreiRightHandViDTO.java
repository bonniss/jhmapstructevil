package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPacinoAndreiRightHandVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoAndreiRightHandViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private Boolean isDefault;

    private AlPacinoDTO user;

    private AndreiRightHandViDTO address;

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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public AlPacinoDTO getUser() {
        return user;
    }

    public void setUser(AlPacinoDTO user) {
        this.user = user;
    }

    public AndreiRightHandViDTO getAddress() {
        return address;
    }

    public void setAddress(AndreiRightHandViDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoAndreiRightHandViDTO)) {
            return false;
        }

        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = (AlPacinoAndreiRightHandViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPacinoAndreiRightHandViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoAndreiRightHandViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", user=" + getUser() +
            ", address=" + getAddress() +
            "}";
    }
}
