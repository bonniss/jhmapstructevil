package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.BenedictRiottaType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPyuDjibrilVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPyuDjibrilViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pyu-djibril-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuDjibrilViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BenedictRiottaType
     */
    public static class BenedictRiottaTypeFilter extends Filter<BenedictRiottaType> {

        public BenedictRiottaTypeFilter() {}

        public BenedictRiottaTypeFilter(BenedictRiottaTypeFilter filter) {
            super(filter);
        }

        @Override
        public BenedictRiottaTypeFilter copy() {
            return new BenedictRiottaTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BenedictRiottaTypeFilter rateType;

    private BigDecimalFilter rate;

    private BooleanFilter isEnabled;

    private UUIDFilter propertyId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlPyuDjibrilViCriteria() {}

    public AlPyuDjibrilViCriteria(AlPyuDjibrilViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.rateType = other.optionalRateType().map(BenedictRiottaTypeFilter::copy).orElse(null);
        this.rate = other.optionalRate().map(BigDecimalFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPyuDjibrilViCriteria copy() {
        return new AlPyuDjibrilViCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BenedictRiottaTypeFilter getRateType() {
        return rateType;
    }

    public Optional<BenedictRiottaTypeFilter> optionalRateType() {
        return Optional.ofNullable(rateType);
    }

    public BenedictRiottaTypeFilter rateType() {
        if (rateType == null) {
            setRateType(new BenedictRiottaTypeFilter());
        }
        return rateType;
    }

    public void setRateType(BenedictRiottaTypeFilter rateType) {
        this.rateType = rateType;
    }

    public BigDecimalFilter getRate() {
        return rate;
    }

    public Optional<BigDecimalFilter> optionalRate() {
        return Optional.ofNullable(rate);
    }

    public BigDecimalFilter rate() {
        if (rate == null) {
            setRate(new BigDecimalFilter());
        }
        return rate;
    }

    public void setRate(BigDecimalFilter rate) {
        this.rate = rate;
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

    public UUIDFilter getPropertyId() {
        return propertyId;
    }

    public Optional<UUIDFilter> optionalPropertyId() {
        return Optional.ofNullable(propertyId);
    }

    public UUIDFilter propertyId() {
        if (propertyId == null) {
            setPropertyId(new UUIDFilter());
        }
        return propertyId;
    }

    public void setPropertyId(UUIDFilter propertyId) {
        this.propertyId = propertyId;
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
        final AlPyuDjibrilViCriteria that = (AlPyuDjibrilViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rateType, that.rateType) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rateType, rate, isEnabled, propertyId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuDjibrilViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRateType().map(f -> "rateType=" + f + ", ").orElse("") +
            optionalRate().map(f -> "rate=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
