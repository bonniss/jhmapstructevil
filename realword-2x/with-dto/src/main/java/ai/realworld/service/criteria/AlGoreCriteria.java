package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlGore} entity. This class is used
 * in {@link ai.realworld.web.rest.AlGoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-gores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlcountTypo
     */
    public static class AlcountTypoFilter extends Filter<AlcountTypo> {

        public AlcountTypoFilter() {}

        public AlcountTypoFilter(AlcountTypoFilter filter) {
            super(filter);
        }

        @Override
        public AlcountTypoFilter copy() {
            return new AlcountTypoFilter(this);
        }
    }

    /**
     * Class for filtering AlcountScopy
     */
    public static class AlcountScopyFilter extends Filter<AlcountScopy> {

        public AlcountScopyFilter() {}

        public AlcountScopyFilter(AlcountScopyFilter filter) {
            super(filter);
        }

        @Override
        public AlcountScopyFilter copy() {
            return new AlcountScopyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private AlcountTypoFilter discountType;

    private BigDecimalFilter discountRate;

    private AlcountScopyFilter scope;

    private LongFilter bizRelationId;

    private UUIDFilter applicationId;

    private LongFilter bizRelationViId;

    private LongFilter conditionsId;

    private LongFilter conditionVisId;

    private Boolean distinct;

    public AlGoreCriteria() {}

    public AlGoreCriteria(AlGoreCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(AlcountTypoFilter::copy).orElse(null);
        this.discountRate = other.optionalDiscountRate().map(BigDecimalFilter::copy).orElse(null);
        this.scope = other.optionalScope().map(AlcountScopyFilter::copy).orElse(null);
        this.bizRelationId = other.optionalBizRelationId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.bizRelationViId = other.optionalBizRelationViId().map(LongFilter::copy).orElse(null);
        this.conditionsId = other.optionalConditionsId().map(LongFilter::copy).orElse(null);
        this.conditionVisId = other.optionalConditionVisId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlGoreCriteria copy() {
        return new AlGoreCriteria(this);
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

    public AlcountTypoFilter getDiscountType() {
        return discountType;
    }

    public Optional<AlcountTypoFilter> optionalDiscountType() {
        return Optional.ofNullable(discountType);
    }

    public AlcountTypoFilter discountType() {
        if (discountType == null) {
            setDiscountType(new AlcountTypoFilter());
        }
        return discountType;
    }

    public void setDiscountType(AlcountTypoFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountRate() {
        return discountRate;
    }

    public Optional<BigDecimalFilter> optionalDiscountRate() {
        return Optional.ofNullable(discountRate);
    }

    public BigDecimalFilter discountRate() {
        if (discountRate == null) {
            setDiscountRate(new BigDecimalFilter());
        }
        return discountRate;
    }

    public void setDiscountRate(BigDecimalFilter discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopyFilter getScope() {
        return scope;
    }

    public Optional<AlcountScopyFilter> optionalScope() {
        return Optional.ofNullable(scope);
    }

    public AlcountScopyFilter scope() {
        if (scope == null) {
            setScope(new AlcountScopyFilter());
        }
        return scope;
    }

    public void setScope(AlcountScopyFilter scope) {
        this.scope = scope;
    }

    public LongFilter getBizRelationId() {
        return bizRelationId;
    }

    public Optional<LongFilter> optionalBizRelationId() {
        return Optional.ofNullable(bizRelationId);
    }

    public LongFilter bizRelationId() {
        if (bizRelationId == null) {
            setBizRelationId(new LongFilter());
        }
        return bizRelationId;
    }

    public void setBizRelationId(LongFilter bizRelationId) {
        this.bizRelationId = bizRelationId;
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

    public LongFilter getBizRelationViId() {
        return bizRelationViId;
    }

    public Optional<LongFilter> optionalBizRelationViId() {
        return Optional.ofNullable(bizRelationViId);
    }

    public LongFilter bizRelationViId() {
        if (bizRelationViId == null) {
            setBizRelationViId(new LongFilter());
        }
        return bizRelationViId;
    }

    public void setBizRelationViId(LongFilter bizRelationViId) {
        this.bizRelationViId = bizRelationViId;
    }

    public LongFilter getConditionsId() {
        return conditionsId;
    }

    public Optional<LongFilter> optionalConditionsId() {
        return Optional.ofNullable(conditionsId);
    }

    public LongFilter conditionsId() {
        if (conditionsId == null) {
            setConditionsId(new LongFilter());
        }
        return conditionsId;
    }

    public void setConditionsId(LongFilter conditionsId) {
        this.conditionsId = conditionsId;
    }

    public LongFilter getConditionVisId() {
        return conditionVisId;
    }

    public Optional<LongFilter> optionalConditionVisId() {
        return Optional.ofNullable(conditionVisId);
    }

    public LongFilter conditionVisId() {
        if (conditionVisId == null) {
            setConditionVisId(new LongFilter());
        }
        return conditionVisId;
    }

    public void setConditionVisId(LongFilter conditionVisId) {
        this.conditionVisId = conditionVisId;
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
        final AlGoreCriteria that = (AlGoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(scope, that.scope) &&
            Objects.equals(bizRelationId, that.bizRelationId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(bizRelationViId, that.bizRelationViId) &&
            Objects.equals(conditionsId, that.conditionsId) &&
            Objects.equals(conditionVisId, that.conditionVisId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            discountType,
            discountRate,
            scope,
            bizRelationId,
            applicationId,
            bizRelationViId,
            conditionsId,
            conditionVisId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountRate().map(f -> "discountRate=" + f + ", ").orElse("") +
            optionalScope().map(f -> "scope=" + f + ", ").orElse("") +
            optionalBizRelationId().map(f -> "bizRelationId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalBizRelationViId().map(f -> "bizRelationViId=" + f + ", ").orElse("") +
            optionalConditionsId().map(f -> "conditionsId=" + f + ", ").orElse("") +
            optionalConditionVisId().map(f -> "conditionVisId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
