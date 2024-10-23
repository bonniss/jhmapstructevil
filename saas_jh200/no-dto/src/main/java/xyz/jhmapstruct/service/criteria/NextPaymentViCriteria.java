package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import xyz.jhmapstruct.domain.enumeration.NextPaymentViMethod;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.NextPaymentVi} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.NextPaymentViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /next-payment-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextPaymentViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NextPaymentViMethod
     */
    public static class NextPaymentViMethodFilter extends Filter<NextPaymentViMethod> {

        public NextPaymentViMethodFilter() {}

        public NextPaymentViMethodFilter(NextPaymentViMethodFilter filter) {
            super(filter);
        }

        @Override
        public NextPaymentViMethodFilter copy() {
            return new NextPaymentViMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amount;

    private InstantFilter paymentDate;

    private NextPaymentViMethodFilter paymentMethod;

    private LongFilter tenantId;

    private Boolean distinct;

    public NextPaymentViCriteria() {}

    public NextPaymentViCriteria(NextPaymentViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(InstantFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(NextPaymentViMethodFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NextPaymentViCriteria copy() {
        return new NextPaymentViCriteria(this);
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

    public NextPaymentViMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<NextPaymentViMethodFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public NextPaymentViMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new NextPaymentViMethodFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(NextPaymentViMethodFilter paymentMethod) {
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
        final NextPaymentViCriteria that = (NextPaymentViCriteria) o;
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
        return "NextPaymentViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalPaymentDate().map(f -> "paymentDate=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
