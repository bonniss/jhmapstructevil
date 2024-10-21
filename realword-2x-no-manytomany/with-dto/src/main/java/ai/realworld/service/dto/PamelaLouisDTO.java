package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.PamelaLouis} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PamelaLouisDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 10485760)
    private String configJason;

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

    public String getConfigJason() {
        return configJason;
    }

    public void setConfigJason(String configJason) {
        this.configJason = configJason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PamelaLouisDTO)) {
            return false;
        }

        PamelaLouisDTO pamelaLouisDTO = (PamelaLouisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pamelaLouisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PamelaLouisDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", configJason='" + getConfigJason() + "'" +
            "}";
    }
}
