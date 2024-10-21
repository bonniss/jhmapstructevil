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
 * Criteria class for the {@link ai.realworld.domain.AlGoreVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlGoreViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-gore-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreViCriteria implements Serializable, Criteria {

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

    private Boolean distinct;

    public AlGoreViCriteria() {}

    public AlGoreViCriteria(AlGoreViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(AlcountTypoFilter::copy).orElse(null);
        this.discountRate = other.optionalDiscountRate().map(BigDecimalFilter::copy).orElse(null);
        this.scope = other.optionalScope().map(AlcountScopyFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlGoreViCriteria copy() {
        return new AlGoreViCriteria(this);
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
        final AlGoreViCriteria that = (AlGoreViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(scope, that.scope) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, discountType, discountRate, scope, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountRate().map(f -> "discountRate=" + f + ", ").orElse("") +
            optionalScope().map(f -> "scope=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
