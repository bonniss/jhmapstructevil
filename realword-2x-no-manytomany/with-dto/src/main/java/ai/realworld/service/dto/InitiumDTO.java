package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.Initium} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InitiumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    private String slug;

    @Size(max = 65535)
    private String description;

    private Boolean isJelloSupported;

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

    public Boolean getIsJelloSupported() {
        return isJelloSupported;
    }

    public void setIsJelloSupported(Boolean isJelloSupported) {
        this.isJelloSupported = isJelloSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InitiumDTO)) {
            return false;
        }

        InitiumDTO initiumDTO = (InitiumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, initiumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InitiumDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", description='" + getDescription() + "'" +
            ", isJelloSupported='" + getIsJelloSupported() + "'" +
            "}";
    }
}
