package ai.realworld.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlVueVueViUsage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueViUsageDTO implements Serializable {

    private UUID id;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        if (!(o instanceof AlVueVueViUsageDTO)) {
            return false;
        }

        AlVueVueViUsageDTO alVueVueViUsageDTO = (AlVueVueViUsageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alVueVueViUsageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueViUsageDTO{" +
            "id='" + getId() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}