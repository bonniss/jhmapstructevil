package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlLadyGaga} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLadyGagaDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 10485760)
    private String descriptionHeitiga;

    private AndreiRightHandDTO address;

    private MetaverseDTO avatar;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeitiga() {
        return descriptionHeitiga;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public AndreiRightHandDTO getAddress() {
        return address;
    }

    public void setAddress(AndreiRightHandDTO address) {
        this.address = address;
    }

    public MetaverseDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(MetaverseDTO avatar) {
        this.avatar = avatar;
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
        if (!(o instanceof AlLadyGagaDTO)) {
            return false;
        }

        AlLadyGagaDTO alLadyGagaDTO = (AlLadyGagaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alLadyGagaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLadyGagaDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", address=" + getAddress() +
            ", avatar=" + getAvatar() +
            ", application=" + getApplication() +
            "}";
    }
}
