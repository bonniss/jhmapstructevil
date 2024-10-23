package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderSigmaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderSigma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderSigmaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderSigmaStatus status;

    private PaymentSigmaDTO payment;

    private ShipmentSigmaDTO shipment;

    private MasterTenantDTO tenant;

    private CustomerSigmaDTO customer;

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

    public OrderSigmaStatus getStatus() {
        return status;
    }

    public void setStatus(OrderSigmaStatus status) {
        this.status = status;
    }

    public PaymentSigmaDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentSigmaDTO payment) {
        this.payment = payment;
    }

    public ShipmentSigmaDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentSigmaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerSigmaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSigmaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderSigmaDTO)) {
            return false;
        }

        OrderSigmaDTO orderSigmaDTO = (OrderSigmaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderSigmaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderSigmaDTO{" +
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
