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
import xyz.jhmapstruct.domain.enumeration.OrderMiStatus;

/**
 * A OrderMi.
 */
@Entity
@Table(name = "order_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderMi implements Serializable {

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
    private OrderMiStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "order", "suppliers" }, allowSetters = true)
    private Set<ProductMi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShipmentMi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private CustomerMi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderMi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderMi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderMiStatus getStatus() {
        return this.status;
    }

    public OrderMi status(OrderMiStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderMiStatus status) {
        this.status = status;
    }

    public Set<ProductMi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductMi> productMis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productMis != null) {
            productMis.forEach(i -> i.setOrder(this));
        }
        this.products = productMis;
    }

    public OrderMi products(Set<ProductMi> productMis) {
        this.setProducts(productMis);
        return this;
    }

    public OrderMi addProducts(ProductMi productMi) {
        this.products.add(productMi);
        productMi.setOrder(this);
        return this;
    }

    public OrderMi removeProducts(ProductMi productMi) {
        this.products.remove(productMi);
        productMi.setOrder(null);
        return this;
    }

    public PaymentMi getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentMi paymentMi) {
        this.payment = paymentMi;
    }

    public OrderMi payment(PaymentMi paymentMi) {
        this.setPayment(paymentMi);
        return this;
    }

    public ShipmentMi getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentMi shipmentMi) {
        this.shipment = shipmentMi;
    }

    public OrderMi shipment(ShipmentMi shipmentMi) {
        this.setShipment(shipmentMi);
        return this;
    }

    public CustomerMi getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerMi customerMi) {
        this.customer = customerMi;
    }

    public OrderMi customer(CustomerMi customerMi) {
        this.setCustomer(customerMi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMi)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderMi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
