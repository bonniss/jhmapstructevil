package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderMiMiStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderMiMi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderMiMiDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderMiMiStatus status;

    private PaymentMiMiDTO payment;

    private ShipmentMiMiDTO shipment;

    private CustomerMiMiDTO customer;

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

    public PaymentMiMiDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentMiMiDTO payment) {
        this.payment = payment;
    }

    public ShipmentMiMiDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentMiMiDTO shipment) {
        this.shipment = shipment;
    }

    public CustomerMiMiDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerMiMiDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMiMiDTO)) {
            return false;
        }

        OrderMiMiDTO orderMiMiDTO = (OrderMiMiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderMiMiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderMiMiDTO{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            ", payment=" + getPayment() +
            ", shipment=" + getShipment() +
            ", customer=" + getCustomer() +
            "}";
    }
}
