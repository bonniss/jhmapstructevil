package ai.realworld.domain;

import ai.realworld.domain.enumeration.FooGameAward;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlDesireVi.
 */
@Entity
@Table(name = "al_desire_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlDesireVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "probability_of_winning")
    private Float probabilityOfWinning;

    @Column(name = "maximum_winning_time")
    private Integer maximumWinningTime;

    @Column(name = "is_winning_time_limited")
    private Boolean isWinningTimeLimited;

    @Enumerated(EnumType.STRING)
    @Column(name = "award_result_type")
    private FooGameAward awardResultType;

    @Column(name = "award_reference")
    private String awardReference;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "programBackground", "wheelBackground", "application", "awards", "awardVis" }, allowSetters = true)
    private AlLeandro maggi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlDesireVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlDesireVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public AlDesireVi weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Float getProbabilityOfWinning() {
        return this.probabilityOfWinning;
    }

    public AlDesireVi probabilityOfWinning(Float probabilityOfWinning) {
        this.setProbabilityOfWinning(probabilityOfWinning);
        return this;
    }

    public void setProbabilityOfWinning(Float probabilityOfWinning) {
        this.probabilityOfWinning = probabilityOfWinning;
    }

    public Integer getMaximumWinningTime() {
        return this.maximumWinningTime;
    }

    public AlDesireVi maximumWinningTime(Integer maximumWinningTime) {
        this.setMaximumWinningTime(maximumWinningTime);
        return this;
    }

    public void setMaximumWinningTime(Integer maximumWinningTime) {
        this.maximumWinningTime = maximumWinningTime;
    }

    public Boolean getIsWinningTimeLimited() {
        return this.isWinningTimeLimited;
    }

    public AlDesireVi isWinningTimeLimited(Boolean isWinningTimeLimited) {
        this.setIsWinningTimeLimited(isWinningTimeLimited);
        return this;
    }

    public void setIsWinningTimeLimited(Boolean isWinningTimeLimited) {
        this.isWinningTimeLimited = isWinningTimeLimited;
    }

    public FooGameAward getAwardResultType() {
        return this.awardResultType;
    }

    public AlDesireVi awardResultType(FooGameAward awardResultType) {
        this.setAwardResultType(awardResultType);
        return this;
    }

    public void setAwardResultType(FooGameAward awardResultType) {
        this.awardResultType = awardResultType;
    }

    public String getAwardReference() {
        return this.awardReference;
    }

    public AlDesireVi awardReference(String awardReference) {
        this.setAwardReference(awardReference);
        return this;
    }

    public void setAwardReference(String awardReference) {
        this.awardReference = awardReference;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public AlDesireVi isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Metaverse getImage() {
        return this.image;
    }

    public void setImage(Metaverse metaverse) {
        this.image = metaverse;
    }

    public AlDesireVi image(Metaverse metaverse) {
        this.setImage(metaverse);
        return this;
    }

    public AlLeandro getMaggi() {
        return this.maggi;
    }

    public void setMaggi(AlLeandro alLeandro) {
        this.maggi = alLeandro;
    }

    public AlDesireVi maggi(AlLeandro alLeandro) {
        this.setMaggi(alLeandro);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlDesireVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlDesireVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlDesireVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", probabilityOfWinning=" + getProbabilityOfWinning() +
            ", maximumWinningTime=" + getMaximumWinningTime() +
            ", isWinningTimeLimited='" + getIsWinningTimeLimited() + "'" +
            ", awardResultType='" + getAwardResultType() + "'" +
            ", awardReference='" + getAwardReference() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            "}";
    }
}
