package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlActiso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlActisoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String key;

    @Size(max = 10485760)
    private String valueJason;

    private JohnLennonDTO application;

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
        if (!(o instanceof AlActisoDTO)) {
            return false;
        }

        AlActisoDTO alActisoDTO = (AlActisoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alActisoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlActisoDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", valueJason='" + getValueJason() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
