package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderMiStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderMi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderMiDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderMiStatus status;

    private NextPaymentMiDTO payment;

    private NextShipmentMiDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerMiDTO customer;

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

    public OrderMiStatus getStatus() {
        return status;
    }

    public void setStatus(OrderMiStatus status) {
        this.status = status;
    }

    public NextPaymentMiDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentMiDTO payment) {
        this.payment = payment;
    }

    public NextShipmentMiDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentMiDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerMiDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerMiDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMiDTO)) {
            return false;
        }

        OrderMiDTO orderMiDTO = (OrderMiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderMiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderMiDTO{" +
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
