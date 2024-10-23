package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import xyz.jhmapstruct.domain.enumeration.OrderGammaStatus;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.OrderGamma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderGammaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private OrderGammaStatus status;

    private PaymentGammaDTO payment;

    private ShipmentGammaDTO shipment;

    private MasterTenantDTO tenant;

    private CustomerGammaDTO customer;

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

    public PaymentGammaDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentGammaDTO payment) {
        this.payment = payment;
    }

    public ShipmentGammaDTO getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentGammaDTO shipment) {
        this.shipment = shipment;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerGammaDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerGammaDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderGammaDTO)) {
            return false;
        }

        OrderGammaDTO orderGammaDTO = (OrderGammaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderGammaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderGammaDTO{" +
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
