package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextOrderBeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderBetaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderBetaStatus status;

    private NextPaymentBetaDTO payment;

    private NextShipmentBetaDTO shipment;

    private MasterTenantDTO tenant;

    private NextCustomerBetaDTO customer;

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

    public OrderBetaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderBetaStatus status) {
        this.status = status;
    }

    public NextPaymentBetaDTO getPayment() {
        return payment;
    }

    public void setPayment(NextPaymentBetaDTO payment) {
        this.payment = payment;
    }

    public NextShipmentBetaDTO getShipment() {
        return shipment;
    }

    public void setShipment(NextShipmentBetaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public NextCustomerBetaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(NextCustomerBetaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderBetaDTO)) {
            return false;
        }

        NextOrderBetaDTO nextOrderBetaDTO = (NextOrderBetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOrderBetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderBetaDTO{" +
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
