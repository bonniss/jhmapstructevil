package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderMiMiStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderMiMi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderMiMiDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderMiMiStatus status;

    private NextPaymentMiMiDTO payment;

    private NextShipmentMiMiDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerMiMiDTO customer;

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

    public OrderMiMiStatus getStatus() {
        return status;
    }

    public void setStatus(OrderMiMiStatus status) {
        this.status = status;
    }

    public NextPaymentMiMiDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentMiMiDTO payment) {
        this.payment = payment;
    }

    public NextShipmentMiMiDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentMiMiDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerMiMiDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerMiMiDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderMiMiDTO)) {
            return false;
        }

        NextOrderMiMiDTO nextOrderMiMiDTO = (NextOrderMiMiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderMiMiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderMiMiDTO{" +
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
