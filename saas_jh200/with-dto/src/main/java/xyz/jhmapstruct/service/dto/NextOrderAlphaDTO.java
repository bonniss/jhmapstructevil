package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderAlphaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderAlpha} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderAlphaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderAlphaStatus status;

    private NextPaymentAlphaDTO payment;

    private NextShipmentAlphaDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerAlphaDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderAlphaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderAlphaStatus status) {
        this.status = status;
    }

    public NextPaymentAlphaDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentAlphaDTO payment) {
        this.payment = payment;
    }

    public NextShipmentAlphaDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentAlphaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerAlphaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerAlphaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderAlphaDTO)) {
            return false;
        }

        NextOrderAlphaDTO nextOrderAlphaDTO = (NextOrderAlphaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderAlphaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderAlphaDTO{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            ", payment=" + getPayment() +
            ", shipment=" + getShipment() +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            "}";
    }
}
