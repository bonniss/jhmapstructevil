package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderBeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderBetaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderBetaStatus status;

    private PaymentBetaDTO payment;

    private ShipmentBetaDTO shipment;

    private MasterTenantDTO tenant;

    private CustomerBetaDTO customer;

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

    public PaymentBetaDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentBetaDTO payment) {
        this.payment = payment;
    }

    public ShipmentBetaDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentBetaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerBetaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBetaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderBetaDTO)) {
            return false;
        }

        OrderBetaDTO orderBetaDTO = (OrderBetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderBetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderBetaDTO{" +
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
