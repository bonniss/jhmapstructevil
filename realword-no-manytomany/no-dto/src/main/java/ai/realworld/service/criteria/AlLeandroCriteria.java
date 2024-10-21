package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlLeandro} entity. This class is used
 * in {@link ai.realworld.web.rest.AlLeandroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-leandros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private IntegerFilter weight;

    private StringFilter description;

    private LocalDateFilter fromDate;

    private LocalDateFilter toDate;

    private BooleanFilter isEnabled;

    private BooleanFilter separateWinningByPeriods;

    private LongFilter programBackgroundId;

    private LongFilter wheelBackgroundId;

    private UUIDFilter applicationId;

    private UUIDFilter awardsId;

    private Boolean distinct;

    public AlLeandroCriteria() {}

    public AlLeandroCriteria(AlLeandroCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(LocalDateFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(LocalDateFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.separateWinningByPeriods = other.optionalSeparateWinningByPeriods().map(BooleanFilter::copy).orElse(null);
        this.programBackgroundId = other.optionalProgramBackgroundId().map(LongFilter::copy).orElse(null);
        this.wheelBackgroundId = other.optionalWheelBackgroundId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.awardsId = other.optionalAwardsId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlLeandroCriteria copy() {
        return new AlLeandroCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public Optional<LocalDateFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public LocalDateFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new LocalDateFilter());
        }
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getToDate() {
        return toDate;
    }

    public Optional<LocalDateFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public LocalDateFilter toDate() {
        if (toDate == null) {
            setToDate(new LocalDateFilter());
        }
        return toDate;
    }

    public void setToDate(LocalDateFilter toDate) {
        this.toDate = toDate;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public Optional<BooleanFilter> optionalIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

    public BooleanFilter isEnabled() {
        if (isEnabled == null) {
            setIsEnabled(new BooleanFilter());
        }
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
    }

    public BooleanFilter getSeparateWinningByPeriods() {
        return separateWinningByPeriods;
    }

    public Optional<BooleanFilter> optionalSeparateWinningByPeriods() {
        return Optional.ofNullable(separateWinningByPeriods);
    }

    public BooleanFilter separateWinningByPeriods() {
        if (separateWinningByPeriods == null) {
            setSeparateWinningByPeriods(new BooleanFilter());
        }
        return separateWinningByPeriods;
    }

    public void setSeparateWinningByPeriods(BooleanFilter separateWinningByPeriods) {
        this.separateWinningByPeriods = separateWinningByPeriods;
    }

    public LongFilter getProgramBackgroundId() {
        return programBackgroundId;
    }

    public Optional<LongFilter> optionalProgramBackgroundId() {
        return Optional.ofNullable(programBackgroundId);
    }

    public LongFilter programBackgroundId() {
        if (programBackgroundId == null) {
            setProgramBackgroundId(new LongFilter());
        }
        return programBackgroundId;
    }

    public void setProgramBackgroundId(LongFilter programBackgroundId) {
        this.programBackgroundId = programBackgroundId;
    }

    public LongFilter getWheelBackgroundId() {
        return wheelBackgroundId;
    }

    public Optional<LongFilter> optionalWheelBackgroundId() {
        return Optional.ofNullable(wheelBackgroundId);
    }

    public LongFilter wheelBackgroundId() {
        if (wheelBackgroundId == null) {
            setWheelBackgroundId(new LongFilter());
        }
        return wheelBackgroundId;
    }

    public void setWheelBackgroundId(LongFilter wheelBackgroundId) {
        this.wheelBackgroundId = wheelBackgroundId;
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

    public UUIDFilter getAwardsId() {
        return awardsId;
    }

    public Optional<UUIDFilter> optionalAwardsId() {
        return Optional.ofNullable(awardsId);
    }

    public UUIDFilter awardsId() {
        if (awardsId == null) {
            setAwardsId(new UUIDFilter());
        }
        return awardsId;
    }

    public void setAwardsId(UUIDFilter awardsId) {
        this.awardsId = awardsId;
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
        final AlLeandroCriteria that = (AlLeandroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(separateWinningByPeriods, that.separateWinningByPeriods) &&
            Objects.equals(programBackgroundId, that.programBackgroundId) &&
            Objects.equals(wheelBackgroundId, that.wheelBackgroundId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(awardsId, that.awardsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            weight,
            description,
            fromDate,
            toDate,
            isEnabled,
            separateWinningByPeriods,
            programBackgroundId,
            wheelBackgroundId,
            applicationId,
            awardsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandroCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalSeparateWinningByPeriods().map(f -> "separateWinningByPeriods=" + f + ", ").orElse("") +
            optionalProgramBackgroundId().map(f -> "programBackgroundId=" + f + ", ").orElse("") +
            optionalWheelBackgroundId().map(f -> "wheelBackgroundId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalAwardsId().map(f -> "awardsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
