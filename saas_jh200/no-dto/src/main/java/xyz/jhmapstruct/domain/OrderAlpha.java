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
import xyz.jhmapstruct.domain.enumeration.OrderAlphaStatus;

/**
 * A OrderAlpha.
 */
@Entity
@Table(name = "order_alpha")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderAlpha implements Serializable {

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
    private OrderAlphaStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductAlpha> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentAlpha payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentAlpha shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerAlpha customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderAlpha id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderAlpha orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderAlpha totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderAlphaStatus getStatus() {
        return this.status;
    }

    public OrderAlpha status(OrderAlphaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderAlphaStatus status) {
        this.status = status;
    }

    public Set<ProductAlpha> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductAlpha> productAlphas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productAlphas != null) {
            productAlphas.forEach(i -> i.setOrder(this));
        }
        this.products = productAlphas;
    }

    public OrderAlpha products(Set<ProductAlpha> productAlphas) {
        this.setProducts(productAlphas);
        return this;
    }

    public OrderAlpha addProducts(ProductAlpha productAlpha) {
        this.products.add(productAlpha);
        productAlpha.setOrder(this);
        return this;
    }

    public OrderAlpha removeProducts(ProductAlpha productAlpha) {
        this.products.remove(productAlpha);
        productAlpha.setOrder(null);
        return this;
    }

    public PaymentAlpha getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentAlpha paymentAlpha) {
        this.payment = paymentAlpha;
    }

    public OrderAlpha payment(PaymentAlpha paymentAlpha) {
        this.setPayment(paymentAlpha);
        return this;
    }

    public ShipmentAlpha getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentAlpha shipmentAlpha) {
        this.shipment = shipmentAlpha;
    }

    public OrderAlpha shipment(ShipmentAlpha shipmentAlpha) {
        this.setShipment(shipmentAlpha);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderAlpha tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerAlpha getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerAlpha customerAlpha) {
        this.customer = customerAlpha;
    }

    public OrderAlpha customer(CustomerAlpha customerAlpha) {
        this.setCustomer(customerAlpha);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderAlpha)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderAlpha) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderAlpha{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
