package xyz.jhmapstruct.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import xyz.jhmapstruct.domain.enumeration.OrderThetaStatus;

/**
 * A OrderTheta.
 */
@Entity
@Table(name = "order_theta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderTheta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @NotNull
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderThetaStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductTheta> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentTheta payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentTheta shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerTheta customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderTheta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderTheta orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderTheta totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderThetaStatus getStatus() {
        return this.status;
    }

    public OrderTheta status(OrderThetaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderThetaStatus status) {
        this.status = status;
    }

    public Set<ProductTheta> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductTheta> productThetas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productThetas != null) {
            productThetas.forEach(i -> i.setOrder(this));
        }
        this.products = productThetas;
    }

    public OrderTheta products(Set<ProductTheta> productThetas) {
        this.setProducts(productThetas);
        return this;
    }

    public OrderTheta addProducts(ProductTheta productTheta) {
        this.products.add(productTheta);
        productTheta.setOrder(this);
        return this;
    }

    public OrderTheta removeProducts(ProductTheta productTheta) {
        this.products.remove(productTheta);
        productTheta.setOrder(null);
        return this;
    }

    public PaymentTheta getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTheta paymentTheta) {
        this.payment = paymentTheta;
    }

    public OrderTheta payment(PaymentTheta paymentTheta) {
        this.setPayment(paymentTheta);
        return this;
    }

    public ShipmentTheta getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentTheta shipmentTheta) {
        this.shipment = shipmentTheta;
    }

    public OrderTheta shipment(ShipmentTheta shipmentTheta) {
        this.setShipment(shipmentTheta);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderTheta tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerTheta getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerTheta customerTheta) {
        this.customer = customerTheta;
    }

    public OrderTheta customer(CustomerTheta customerTheta) {
        this.setCustomer(customerTheta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTheta)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderTheta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderTheta{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
