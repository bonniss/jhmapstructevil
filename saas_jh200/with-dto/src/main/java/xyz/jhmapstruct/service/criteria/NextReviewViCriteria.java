package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.NextReviewVi} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.NextReviewViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /next-review-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextReviewViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rating;

    private InstantFilter reviewDate;

    private LongFilter productId;

    private LongFilter tenantId;

    private Boolean distinct;

    public NextReviewViCriteria() {}

    public NextReviewViCriteria(NextReviewViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.rating = other.optionalRating().map(IntegerFilter::copy).orElse(null);
        this.reviewDate = other.optionalReviewDate().map(InstantFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NextReviewViCriteria copy() {
        return new NextReviewViCriteria(this);
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

    public IntegerFilter getRating() {
        return rating;
    }

    public Optional<IntegerFilter> optionalRating() {
        return Optional.ofNullable(rating);
    }

    public IntegerFilter rating() {
        if (rating == null) {
            setRating(new IntegerFilter());
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public InstantFilter getReviewDate() {
        return reviewDate;
    }

    public Optional<InstantFilter> optionalReviewDate() {
        return Optional.ofNullable(reviewDate);
    }

    public InstantFilter reviewDate() {
        if (reviewDate == null) {
            setReviewDate(new InstantFilter());
        }
        return reviewDate;
    }

    public void setReviewDate(InstantFilter reviewDate) {
        this.reviewDate = reviewDate;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
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
        final NextReviewViCriteria that = (NextReviewViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(reviewDate, that.reviewDate) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, reviewDate, productId, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextReviewViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRating().map(f -> "rating=" + f + ", ").orElse("") +
            optionalReviewDate().map(f -> "reviewDate=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
