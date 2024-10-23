package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderStatus status;

    private NextPaymentDTO payment;

    private NextShipmentDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerDTO customer;

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public NextPaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentDTO payment) {
        this.payment = payment;
    }

    public NextShipmentDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderDTO)) {
            return false;
        }

        NextOrderDTO nextOrderDTO = (NextOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderDTO{" +
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
