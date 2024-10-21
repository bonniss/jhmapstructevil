package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlLeandro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandroDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private Integer weight;

    @Size(max = 65535)
    private String description;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Boolean isEnabled;

    private Boolean separateWinningByPeriods;

    private MetaverseDTO programBackground;

    private MetaverseDTO wheelBackground;

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getSeparateWinningByPeriods() {
        return separateWinningByPeriods;
    }

    public void setSeparateWinningByPeriods(Boolean separateWinningByPeriods) {
        this.separateWinningByPeriods = separateWinningByPeriods;
    }

    public MetaverseDTO getProgramBackground() {
        return programBackground;
    }

    public void setProgramBackground(MetaverseDTO programBackground) {
        this.programBackground = programBackground;
    }

    public MetaverseDTO getWheelBackground() {
        return wheelBackground;
    }

    public void setWheelBackground(MetaverseDTO wheelBackground) {
        this.wheelBackground = wheelBackground;
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
        if (!(o instanceof AlLeandroDTO)) {
            return false;
        }

        AlLeandroDTO alLeandroDTO = (AlLeandroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alLeandroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandroDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", description='" + getDescription() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", separateWinningByPeriods='" + getSeparateWinningByPeriods() + "'" +
            ", programBackground=" + getProgramBackground() +
            ", wheelBackground=" + getWheelBackground() +
            ", application=" + getApplication() +
            "}";
    }
}
