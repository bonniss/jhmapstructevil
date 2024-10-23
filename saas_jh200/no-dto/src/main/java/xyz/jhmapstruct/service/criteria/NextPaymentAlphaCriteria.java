package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import xyz.jhmapstruct.domain.enumeration.NextPaymentAlphaMethod;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.NextPaymentAlpha} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.NextPaymentAlphaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /next-payment-alphas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextPaymentAlphaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NextPaymentAlphaMethod
     */
    public static class NextPaymentAlphaMethodFilter extends Filter<NextPaymentAlphaMethod> {

        public NextPaymentAlphaMethodFilter() {}

        public NextPaymentAlphaMethodFilter(NextPaymentAlphaMethodFilter filter) {
            super(filter);
        }

        @Override
        public NextPaymentAlphaMethodFilter copy() {
            return new NextPaymentAlphaMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amount;

    private InstantFilter paymentDate;

    private NextPaymentAlphaMethodFilter paymentMethod;

    private LongFilter tenantId;

    private Boolean distinct;

    public NextPaymentAlphaCriteria() {}

    public NextPaymentAlphaCriteria(NextPaymentAlphaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(InstantFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(NextPaymentAlphaMethodFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NextPaymentAlphaCriteria copy() {
        return new NextPaymentAlphaCriteria(this);
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

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public InstantFilter getPaymentDate() {
        return paymentDate;
    }

    public Optional<InstantFilter> optionalPaymentDate() {
        return Optional.ofNullable(paymentDate);
    }

    public InstantFilter paymentDate() {
        if (paymentDate == null) {
            setPaymentDate(new InstantFilter());
        }
        return paymentDate;
    }

    public void setPaymentDate(InstantFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public NextPaymentAlphaMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<NextPaymentAlphaMethodFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public NextPaymentAlphaMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new NextPaymentAlphaMethodFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(NextPaymentAlphaMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        final NextPaymentAlphaCriteria that = (NextPaymentAlphaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, paymentDate, paymentMethod, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextPaymentAlphaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalPaymentDate().map(f -> "paymentDate=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
