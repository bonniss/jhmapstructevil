package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderAlphaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderAlpha} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderAlphaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderAlphaStatus status;

    private PaymentAlphaDTO payment;

    private ShipmentAlphaDTO shipment;

    private MasterTenantDTO tenant;

    private CustomerAlphaDTO customer;

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

    public PaymentAlphaDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentAlphaDTO payment) {
        this.payment = payment;
    }

    public ShipmentAlphaDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentAlphaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerAlphaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerAlphaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderAlphaDTO)) {
            return false;
        }

        OrderAlphaDTO orderAlphaDTO = (OrderAlphaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderAlphaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderAlphaDTO{" +
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
