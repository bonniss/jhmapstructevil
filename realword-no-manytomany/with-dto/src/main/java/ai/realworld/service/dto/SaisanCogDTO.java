package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.SaisanCog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaisanCogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String key;

    @Size(max = 10485760)
    private String valueJason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValueJason() {
        return valueJason;
    }

    public void setValueJason(String valueJason) {
        this.valueJason = valueJason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaisanCogDTO)) {
            return false;
        }

        SaisanCogDTO saisanCogDTO = (SaisanCogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saisanCogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaisanCogDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", valueJason='" + getValueJason() + "'" +
            "}";
    }
}
