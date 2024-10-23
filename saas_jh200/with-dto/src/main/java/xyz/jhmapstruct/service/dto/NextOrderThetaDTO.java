package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderThetaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderTheta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderThetaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderThetaStatus status;

    private NextPaymentThetaDTO payment;

    private NextShipmentThetaDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerThetaDTO customer;

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

    public OrderThetaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderThetaStatus status) {
        this.status = status;
    }

    public NextPaymentThetaDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentThetaDTO payment) {
        this.payment = payment;
    }

    public NextShipmentThetaDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentThetaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerThetaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerThetaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderThetaDTO)) {
            return false;
        }

        NextOrderThetaDTO nextOrderThetaDTO = (NextOrderThetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderThetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderThetaDTO{" +
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
