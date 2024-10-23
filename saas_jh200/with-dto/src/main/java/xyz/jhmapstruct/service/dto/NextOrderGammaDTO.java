package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderGammaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderGamma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderGammaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderGammaStatus status;

    private NextPaymentGammaDTO payment;

    private NextShipmentGammaDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerGammaDTO customer;

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

    public OrderGammaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderGammaStatus status) {
        this.status = status;
    }

    public NextPaymentGammaDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentGammaDTO payment) {
        this.payment = payment;
    }

    public NextShipmentGammaDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentGammaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerGammaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerGammaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderGammaDTO)) {
            return false;
        }

        NextOrderGammaDTO nextOrderGammaDTO = (NextOrderGammaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderGammaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderGammaDTO{" +
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
