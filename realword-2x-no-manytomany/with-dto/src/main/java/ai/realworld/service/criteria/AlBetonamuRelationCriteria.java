package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.ReductionType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlBetonamuRelation} entity. This class is used
 * in {@link ai.realworld.web.rest.AlBetonamuRelationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-betonamu-relations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBetonamuRelationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReductionType
     */
    public static class ReductionTypeFilter extends Filter<ReductionType> {

        public ReductionTypeFilter() {}

        public ReductionTypeFilter(ReductionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReductionTypeFilter copy() {
            return new ReductionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ReductionTypeFilter type;

    private UUIDFilter supplierId;

    private UUIDFilter customerId;

    private UUIDFilter applicationId;

    private LongFilter discountsId;

    private Boolean distinct;

    public AlBetonamuRelationCriteria() {}

    public AlBetonamuRelationCriteria(AlBetonamuRelationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(ReductionTypeFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.discountsId = other.optionalDiscountsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlBetonamuRelationCriteria copy() {
        return new AlBetonamuRelationCriteria(this);
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

    public ReductionTypeFilter getType() {
        return type;
    }

    public Optional<ReductionTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public ReductionTypeFilter type() {
        if (type == null) {
            setType(new ReductionTypeFilter());
        }
        return type;
    }

    public void setType(ReductionTypeFilter type) {
        this.type = type;
    }

    public UUIDFilter getSupplierId() {
        return supplierId;
    }

    public Optional<UUIDFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public UUIDFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new UUIDFilter());
        }
        return supplierId;
    }

    public void setSupplierId(UUIDFilter supplierId) {
        this.supplierId = supplierId;
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

    public LongFilter getDiscountsId() {
        return discountsId;
    }

    public Optional<LongFilter> optionalDiscountsId() {
        return Optional.ofNullable(discountsId);
    }

    public LongFilter discountsId() {
        if (discountsId == null) {
            setDiscountsId(new LongFilter());
        }
        return discountsId;
    }

    public void setDiscountsId(LongFilter discountsId) {
        this.discountsId = discountsId;
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
        final AlBetonamuRelationCriteria that = (AlBetonamuRelationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(discountsId, that.discountsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, supplierId, customerId, applicationId, discountsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBetonamuRelationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDiscountsId().map(f -> "discountsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
