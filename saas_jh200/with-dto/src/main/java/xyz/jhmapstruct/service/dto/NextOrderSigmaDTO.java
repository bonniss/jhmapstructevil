package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderSigmaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderSigma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderSigmaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderSigmaStatus status;

    private NextPaymentSigmaDTO payment;

    private NextShipmentSigmaDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerSigmaDTO customer;

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

    public OrderSigmaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderSigmaStatus status) {
        this.status = status;
    }

    public NextPaymentSigmaDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentSigmaDTO payment) {
        this.payment = payment;
    }

    public NextShipmentSigmaDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentSigmaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerSigmaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerSigmaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderSigmaDTO)) {
            return false;
        }

        NextOrderSigmaDTO nextOrderSigmaDTO = (NextOrderSigmaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderSigmaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderSigmaDTO{" +
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
