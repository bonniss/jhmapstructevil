package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.EeriePointSource;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPacinoPointHistory} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPacinoPointHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pacino-point-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoPointHistoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EeriePointSource
     */
    public static class EeriePointSourceFilter extends Filter<EeriePointSource> {

        public EeriePointSourceFilter() {}

        public EeriePointSourceFilter(EeriePointSourceFilter filter) {
            super(filter);
        }

        @Override
        public EeriePointSourceFilter copy() {
            return new EeriePointSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EeriePointSourceFilter source;

    private StringFilter associatedId;

    private IntegerFilter pointAmount;

    private UUIDFilter customerId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlPacinoPointHistoryCriteria() {}

    public AlPacinoPointHistoryCriteria(AlPacinoPointHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.source = other.optionalSource().map(EeriePointSourceFilter::copy).orElse(null);
        this.associatedId = other.optionalAssociatedId().map(StringFilter::copy).orElse(null);
        this.pointAmount = other.optionalPointAmount().map(IntegerFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPacinoPointHistoryCriteria copy() {
        return new AlPacinoPointHistoryCriteria(this);
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

    public EeriePointSourceFilter getSource() {
        return source;
    }

    public Optional<EeriePointSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public EeriePointSourceFilter source() {
        if (source == null) {
            setSource(new EeriePointSourceFilter());
        }
        return source;
    }

    public void setSource(EeriePointSourceFilter source) {
        this.source = source;
    }

    public StringFilter getAssociatedId() {
        return associatedId;
    }

    public Optional<StringFilter> optionalAssociatedId() {
        return Optional.ofNullable(associatedId);
    }

    public StringFilter associatedId() {
        if (associatedId == null) {
            setAssociatedId(new StringFilter());
        }
        return associatedId;
    }

    public void setAssociatedId(StringFilter associatedId) {
        this.associatedId = associatedId;
    }

    public IntegerFilter getPointAmount() {
        return pointAmount;
    }

    public Optional<IntegerFilter> optionalPointAmount() {
        return Optional.ofNullable(pointAmount);
    }

    public IntegerFilter pointAmount() {
        if (pointAmount == null) {
            setPointAmount(new IntegerFilter());
        }
        return pointAmount;
    }

    public void setPointAmount(IntegerFilter pointAmount) {
        this.pointAmount = pointAmount;
    }

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
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
        final AlPacinoPointHistoryCriteria that = (AlPacinoPointHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(source, that.source) &&
            Objects.equals(associatedId, that.associatedId) &&
            Objects.equals(pointAmount, that.pointAmount) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, associatedId, pointAmount, customerId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoPointHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalAssociatedId().map(f -> "associatedId=" + f + ", ").orElse("") +
            optionalPointAmount().map(f -> "pointAmount=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
