package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.BenedictToy;
import ai.realworld.domain.enumeration.PeteType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlProPro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProProDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 10485760)
    private String descriptionHeitiga;

    private PeteType propertyType;

    private Float areaInSquareMeter;

    @Min(value = 1)
    private Integer numberOfAdults;

    @Min(value = 0)
    private Integer numberOfPreschoolers;

    @Min(value = 0)
    private Integer numberOfChildren;

    private Integer numberOfRooms;

    private Integer numberOfFloors;

    private BenedictToy bedSize;

    private Boolean isEnabled;

    private AlProProDTO parent;

    private AlLadyGagaDTO project;

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

    public PeteType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
    }

    public Float getAreaInSquareMeter() {
        return areaInSquareMeter;
    }

    public void setAreaInSquareMeter(Float areaInSquareMeter) {
        this.areaInSquareMeter = areaInSquareMeter;
    }

    public Integer getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfPreschoolers() {
        return numberOfPreschoolers;
    }

    public void setNumberOfPreschoolers(Integer numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public BenedictToy getBedSize() {
        return bedSize;
    }

    public void setBedSize(BenedictToy bedSize) {
        this.bedSize = bedSize;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProProDTO getParent() {
        return parent;
    }

    public void setParent(AlProProDTO parent) {
        this.parent = parent;
    }

    public AlLadyGagaDTO getProject() {
        return project;
    }

    public void setProject(AlLadyGagaDTO project) {
        this.project = project;
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
        if (!(o instanceof AlProProDTO)) {
            return false;
        }

        AlProProDTO alProProDTO = (AlProProDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alProProDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProProDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", areaInSquareMeter=" + getAreaInSquareMeter() +
            ", numberOfAdults=" + getNumberOfAdults() +
            ", numberOfPreschoolers=" + getNumberOfPreschoolers() +
            ", numberOfChildren=" + getNumberOfChildren() +
            ", numberOfRooms=" + getNumberOfRooms() +
            ", numberOfFloors=" + getNumberOfFloors() +
            ", bedSize='" + getBedSize() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", parent=" + getParent() +
            ", project=" + getProject() +
            ", avatar=" + getAvatar() +
            ", application=" + getApplication() +
            "}";
    }
}
