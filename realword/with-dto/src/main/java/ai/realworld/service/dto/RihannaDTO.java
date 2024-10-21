package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.Rihanna} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RihannaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    @Size(max = 10485760)
    private String permissionGridJason;

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

    public String getPermissionGridJason() {
        return permissionGridJason;
    }

    public void setPermissionGridJason(String permissionGridJason) {
        this.permissionGridJason = permissionGridJason;
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
        if (!(o instanceof RihannaDTO)) {
            return false;
        }

        RihannaDTO rihannaDTO = (RihannaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rihannaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RihannaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", permissionGridJason='" + getPermissionGridJason() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
