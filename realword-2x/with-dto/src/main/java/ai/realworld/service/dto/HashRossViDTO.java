package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.HashRossVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HashRossViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    private String slug;

    @Size(max = 65535)
    private String description;

    @Size(max = 10485760)
    private String permissionGridJason;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashRossViDTO)) {
            return false;
        }

        HashRossViDTO hashRossViDTO = (HashRossViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hashRossViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HashRossViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", description='" + getDescription() + "'" +
            ", permissionGridJason='" + getPermissionGridJason() + "'" +
            "}";
    }
}
