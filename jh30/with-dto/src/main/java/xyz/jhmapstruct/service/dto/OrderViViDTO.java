package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderViViStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderViVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderViViDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderViViStatus status;

    private PaymentViViDTO payment;

    private ShipmentViViDTO shipment;

    private CustomerViViDTO customer;

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

    public OrderViViStatus getStatus() {
        return status;
    }

    public void setStatus(OrderViViStatus status) {
        this.status = status;
    }

    public PaymentViViDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentViViDTO payment) {
        this.payment = payment;
    }

    public ShipmentViViDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentViViDTO shipment) {
        this.shipment = shipment;
    }

    public CustomerViViDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerViViDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderViViDTO)) {
            return false;
        }

        OrderViViDTO orderViViDTO = (OrderViViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderViViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderViViDTO{" +
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
