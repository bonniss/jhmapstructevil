package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.FooGameAward;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlDesire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlDesireDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private Integer weight;

    private Float probabilityOfWinning;

    private Integer maximumWinningTime;

    private Boolean isWinningTimeLimited;

    private FooGameAward awardResultType;

    private String awardReference;

    private Boolean isDefault;

    private MetaverseDTO image;

    private AlLeandroDTO miniGame;

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

    public Float getProbabilityOfWinning() {
        return probabilityOfWinning;
    }

    public void setProbabilityOfWinning(Float probabilityOfWinning) {
        this.probabilityOfWinning = probabilityOfWinning;
    }

    public Integer getMaximumWinningTime() {
        return maximumWinningTime;
    }

    public void setMaximumWinningTime(Integer maximumWinningTime) {
        this.maximumWinningTime = maximumWinningTime;
    }

    public Boolean getIsWinningTimeLimited() {
        return isWinningTimeLimited;
    }

    public void setIsWinningTimeLimited(Boolean isWinningTimeLimited) {
        this.isWinningTimeLimited = isWinningTimeLimited;
    }

    public FooGameAward getAwardResultType() {
        return awardResultType;
    }

    public void setAwardResultType(FooGameAward awardResultType) {
        this.awardResultType = awardResultType;
    }

    public String getAwardReference() {
        return awardReference;
    }

    public void setAwardReference(String awardReference) {
        this.awardReference = awardReference;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public MetaverseDTO getImage() {
        return image;
    }

    public void setImage(MetaverseDTO image) {
        this.image = image;
    }

    public AlLeandroDTO getMiniGame() {
        return miniGame;
    }

    public void setMiniGame(AlLeandroDTO miniGame) {
        this.miniGame = miniGame;
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
        if (!(o instanceof AlDesireDTO)) {
            return false;
        }

        AlDesireDTO alDesireDTO = (AlDesireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alDesireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlDesireDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", probabilityOfWinning=" + getProbabilityOfWinning() +
            ", maximumWinningTime=" + getMaximumWinningTime() +
            ", isWinningTimeLimited='" + getIsWinningTimeLimited() + "'" +
            ", awardResultType='" + getAwardResultType() + "'" +
            ", awardReference='" + getAwardReference() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", image=" + getImage() +
            ", miniGame=" + getMiniGame() +
            ", application=" + getApplication() +
            "}";
    }
}
