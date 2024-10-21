package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.SitomutaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.SicilyUmetoVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SicilyUmetoViDTO implements Serializable {

    private Long id;

    private SitomutaType type;

    @Size(max = 10485760)
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SitomutaType getType() {
        return type;
    }

    public void setType(SitomutaType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SicilyUmetoViDTO)) {
            return false;
        }

        SicilyUmetoViDTO sicilyUmetoViDTO = (SicilyUmetoViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sicilyUmetoViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SicilyUmetoViDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
