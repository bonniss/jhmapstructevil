package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderViStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderViDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderViStatus status;

    private NextPaymentViDTO payment;

    private NextShipmentViDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerViDTO customer;

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

    public OrderViStatus getStatus() {
        return status;
    }

    public void setStatus(OrderViStatus status) {
        this.status = status;
    }

    public NextPaymentViDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentViDTO payment) {
        this.payment = payment;
    }

    public NextShipmentViDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentViDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerViDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerViDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderViDTO)) {
            return false;
        }

        NextOrderViDTO nextOrderViDTO = (NextOrderViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderViDTO{" +
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
