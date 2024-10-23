package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.OrderBeta} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.OrderBetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-betas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderBetaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderBetaStatus
     */
    public static class OrderBetaStatusFilter extends Filter<OrderBetaStatus> {

        public OrderBetaStatusFilter() {}

        public OrderBetaStatusFilter(OrderBetaStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderBetaStatusFilter copy() {
            return new OrderBetaStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter orderDate;

    private BigDecimalFilter totalPrice;

    private OrderBetaStatusFilter status;

    private LongFilter productsId;

    private LongFilter paymentId;

    private LongFilter shipmentId;

    private LongFilter tenantId;

    private LongFilter customerId;

    private Boolean distinct;

    public OrderBetaCriteria() {}

    public OrderBetaCriteria(OrderBetaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderDate = other.optionalOrderDate().map(InstantFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OrderBetaStatusFilter::copy).orElse(null);
        this.productsId = other.optionalProductsId().map(LongFilter::copy).orElse(null);
        this.paymentId = other.optionalPaymentId().map(LongFilter::copy).orElse(null);
        this.shipmentId = other.optionalShipmentId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrderBetaCriteria copy() {
        return new OrderBetaCriteria(this);
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

    public InstantFilter getOrderDate() {
        return orderDate;
    }

    public Optional<InstantFilter> optionalOrderDate() {
        return Optional.ofNullable(orderDate);
    }

    public InstantFilter orderDate() {
        if (orderDate == null) {
            setOrderDate(new InstantFilter());
        }
        return orderDate;
    }

    public void setOrderDate(InstantFilter orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderBetaStatusFilter getStatus() {
        return status;
    }

    public Optional<OrderBetaStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OrderBetaStatusFilter status() {
        if (status == null) {
            setStatus(new OrderBetaStatusFilter());
        }
        return status;
    }

    public void setStatus(OrderBetaStatusFilter status) {
        this.status = status;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public Optional<LongFilter> optionalProductsId() {
        return Optional.ofNullable(productsId);
    }

    public LongFilter productsId() {
        if (productsId == null) {
            setProductsId(new LongFilter());
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public Optional<LongFilter> optionalPaymentId() {
        return Optional.ofNullable(paymentId);
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            setPaymentId(new LongFilter());
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getShipmentId() {
        return shipmentId;
    }

    public Optional<LongFilter> optionalShipmentId() {
        return Optional.ofNullable(shipmentId);
    }

    public LongFilter shipmentId() {
        if (shipmentId == null) {
            setShipmentId(new LongFilter());
        }
        return shipmentId;
    }

    public void setShipmentId(LongFilter shipmentId) {
        this.shipmentId = shipmentId;
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final OrderBetaCriteria that = (OrderBetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(status, that.status) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(shipmentId, that.shipmentId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, totalPrice, status, productsId, paymentId, shipmentId, tenantId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderBetaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderDate().map(f -> "orderDate=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalProductsId().map(f -> "productsId=" + f + ", ").orElse("") +
            optionalPaymentId().map(f -> "paymentId=" + f + ", ").orElse("") +
            optionalShipmentId().map(f -> "shipmentId=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
