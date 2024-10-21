package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.FooGameAward;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlDesire} entity. This class is used
 * in {@link ai.realworld.web.rest.AlDesireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-desires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlDesireCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FooGameAward
     */
    public static class FooGameAwardFilter extends Filter<FooGameAward> {

        public FooGameAwardFilter() {}

        public FooGameAwardFilter(FooGameAwardFilter filter) {
            super(filter);
        }

        @Override
        public FooGameAwardFilter copy() {
            return new FooGameAwardFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private IntegerFilter weight;

    private FloatFilter probabilityOfWinning;

    private IntegerFilter maximumWinningTime;

    private BooleanFilter isWinningTimeLimited;

    private FooGameAwardFilter awardResultType;

    private StringFilter awardReference;

    private BooleanFilter isDefault;

    private LongFilter imageId;

    private UUIDFilter miniGameId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlDesireCriteria() {}

    public AlDesireCriteria(AlDesireCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(IntegerFilter::copy).orElse(null);
        this.probabilityOfWinning = other.optionalProbabilityOfWinning().map(FloatFilter::copy).orElse(null);
        this.maximumWinningTime = other.optionalMaximumWinningTime().map(IntegerFilter::copy).orElse(null);
        this.isWinningTimeLimited = other.optionalIsWinningTimeLimited().map(BooleanFilter::copy).orElse(null);
        this.awardResultType = other.optionalAwardResultType().map(FooGameAwardFilter::copy).orElse(null);
        this.awardReference = other.optionalAwardReference().map(StringFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.imageId = other.optionalImageId().map(LongFilter::copy).orElse(null);
        this.miniGameId = other.optionalMiniGameId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlDesireCriteria copy() {
        return new AlDesireCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getWeight() {
        return weight;
    }

    public Optional<IntegerFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public IntegerFilter weight() {
        if (weight == null) {
            setWeight(new IntegerFilter());
        }
        return weight;
    }

    public void setWeight(IntegerFilter weight) {
        this.weight = weight;
    }

    public FloatFilter getProbabilityOfWinning() {
        return probabilityOfWinning;
    }

    public Optional<FloatFilter> optionalProbabilityOfWinning() {
        return Optional.ofNullable(probabilityOfWinning);
    }

    public FloatFilter probabilityOfWinning() {
        if (probabilityOfWinning == null) {
            setProbabilityOfWinning(new FloatFilter());
        }
        return probabilityOfWinning;
    }

    public void setProbabilityOfWinning(FloatFilter probabilityOfWinning) {
        this.probabilityOfWinning = probabilityOfWinning;
    }

    public IntegerFilter getMaximumWinningTime() {
        return maximumWinningTime;
    }

    public Optional<IntegerFilter> optionalMaximumWinningTime() {
        return Optional.ofNullable(maximumWinningTime);
    }

    public IntegerFilter maximumWinningTime() {
        if (maximumWinningTime == null) {
            setMaximumWinningTime(new IntegerFilter());
        }
        return maximumWinningTime;
    }

    public void setMaximumWinningTime(IntegerFilter maximumWinningTime) {
        this.maximumWinningTime = maximumWinningTime;
    }

    public BooleanFilter getIsWinningTimeLimited() {
        return isWinningTimeLimited;
    }

    public Optional<BooleanFilter> optionalIsWinningTimeLimited() {
        return Optional.ofNullable(isWinningTimeLimited);
    }

    public BooleanFilter isWinningTimeLimited() {
        if (isWinningTimeLimited == null) {
            setIsWinningTimeLimited(new BooleanFilter());
        }
        return isWinningTimeLimited;
    }

    public void setIsWinningTimeLimited(BooleanFilter isWinningTimeLimited) {
        this.isWinningTimeLimited = isWinningTimeLimited;
    }

    public FooGameAwardFilter getAwardResultType() {
        return awardResultType;
    }

    public Optional<FooGameAwardFilter> optionalAwardResultType() {
        return Optional.ofNullable(awardResultType);
    }

    public FooGameAwardFilter awardResultType() {
        if (awardResultType == null) {
            setAwardResultType(new FooGameAwardFilter());
        }
        return awardResultType;
    }

    public void setAwardResultType(FooGameAwardFilter awardResultType) {
        this.awardResultType = awardResultType;
    }

    public StringFilter getAwardReference() {
        return awardReference;
    }

    public Optional<StringFilter> optionalAwardReference() {
        return Optional.ofNullable(awardReference);
    }

    public StringFilter awardReference() {
        if (awardReference == null) {
            setAwardReference(new StringFilter());
        }
        return awardReference;
    }

    public void setAwardReference(StringFilter awardReference) {
        this.awardReference = awardReference;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public Optional<LongFilter> optionalImageId() {
        return Optional.ofNullable(imageId);
    }

    public LongFilter imageId() {
        if (imageId == null) {
            setImageId(new LongFilter());
        }
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public UUIDFilter getMiniGameId() {
        return miniGameId;
    }

    public Optional<UUIDFilter> optionalMiniGameId() {
        return Optional.ofNullable(miniGameId);
    }

    public UUIDFilter miniGameId() {
        if (miniGameId == null) {
            setMiniGameId(new UUIDFilter());
        }
        return miniGameId;
    }

    public void setMiniGameId(UUIDFilter miniGameId) {
        this.miniGameId = miniGameId;
    }

    public UUIDFilter getApplicationId() {
        return applicationId;
    }

    public Optional<UUIDFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public UUIDFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new UUIDFilter());
        }
        return applicationId;
    }

    public void setApplicationId(UUIDFilter applicationId) {
        this.applicationId = applicationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlDesireCriteria that = (AlDesireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(probabilityOfWinning, that.probabilityOfWinning) &&
            Objects.equals(maximumWinningTime, that.maximumWinningTime) &&
            Objects.equals(isWinningTimeLimited, that.isWinningTimeLimited) &&
            Objects.equals(awardResultType, that.awardResultType) &&
            Objects.equals(awardReference, that.awardReference) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(miniGameId, that.miniGameId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            weight,
            probabilityOfWinning,
            maximumWinningTime,
            isWinningTimeLimited,
            awardResultType,
            awardReference,
            isDefault,
            imageId,
            miniGameId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlDesireCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalProbabilityOfWinning().map(f -> "probabilityOfWinning=" + f + ", ").orElse("") +
            optionalMaximumWinningTime().map(f -> "maximumWinningTime=" + f + ", ").orElse("") +
            optionalIsWinningTimeLimited().map(f -> "isWinningTimeLimited=" + f + ", ").orElse("") +
            optionalAwardResultType().map(f -> "awardResultType=" + f + ", ").orElse("") +
            optionalAwardReference().map(f -> "awardReference=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalImageId().map(f -> "imageId=" + f + ", ").orElse("") +
            optionalMiniGameId().map(f -> "miniGameId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
