package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.PeteStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlProty} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProtyDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 10485760)
    private String descriptionHeitiga;

    private String coordinate;

    private String code;

    private PeteStatus status;

    private Boolean isEnabled;

    private AlProtyDTO parent;

    @NotNull
    private AlAppleDTO operator;

    private AlProProDTO propertyProfile;

    private MetaverseDTO avatar;

    private JohnLennonDTO application;

    private Set<MetaverseDTO> images = new HashSet<>();

    private Set<AlPyuJokerDTO> bookings = new HashSet<>();

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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PeteStatus getStatus() {
        return status;
    }

    public void setStatus(PeteStatus status) {
        this.status = status;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProtyDTO getParent() {
        return parent;
    }

    public void setParent(AlProtyDTO parent) {
        this.parent = parent;
    }

    public AlAppleDTO getOperator() {
        return operator;
    }

    public void setOperator(AlAppleDTO operator) {
        this.operator = operator;
    }

    public AlProProDTO getPropertyProfile() {
        return propertyProfile;
    }

    public void setPropertyProfile(AlProProDTO propertyProfile) {
        this.propertyProfile = propertyProfile;
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

    public Set<MetaverseDTO> getImages() {
        return images;
    }

    public void setImages(Set<MetaverseDTO> images) {
        this.images = images;
    }

    public Set<AlPyuJokerDTO> getBookings() {
        return bookings;
    }

    public void setBookings(Set<AlPyuJokerDTO> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlProtyDTO)) {
            return false;
        }

        AlProtyDTO alProtyDTO = (AlProtyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alProtyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProtyDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", coordinate='" + getCoordinate() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", parent=" + getParent() +
            ", operator=" + getOperator() +
            ", propertyProfile=" + getPropertyProfile() +
            ", avatar=" + getAvatar() +
            ", application=" + getApplication() +
            ", images=" + getImages() +
            ", bookings=" + getBookings() +
            "}";
    }
}
