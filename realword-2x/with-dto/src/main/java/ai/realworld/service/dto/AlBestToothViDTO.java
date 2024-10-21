package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlBestToothVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBestToothViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBestToothViDTO)) {
            return false;
        }

        AlBestToothViDTO alBestToothViDTO = (AlBestToothViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alBestToothViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBestToothViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
