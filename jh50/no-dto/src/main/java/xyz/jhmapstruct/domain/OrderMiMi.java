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
import xyz.jhmapstruct.domain.enumeration.OrderMiMiStatus;

/**
 * A OrderMiMi.
 */
@Entity
@Table(name = "order_mi_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderMiMi implements Serializable {

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
    private OrderMiMiStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "order", "suppliers" }, allowSetters = true)
    private Set<ProductMiMi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMiMi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShipmentMiMi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private CustomerMiMi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderMiMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderMiMi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderMiMi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderMiMiStatus getStatus() {
        return this.status;
    }

    public OrderMiMi status(OrderMiMiStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderMiMiStatus status) {
        this.status = status;
    }

    public Set<ProductMiMi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductMiMi> productMiMis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productMiMis != null) {
            productMiMis.forEach(i -> i.setOrder(this));
        }
        this.products = productMiMis;
    }

    public OrderMiMi products(Set<ProductMiMi> productMiMis) {
        this.setProducts(productMiMis);
        return this;
    }

    public OrderMiMi addProducts(ProductMiMi productMiMi) {
        this.products.add(productMiMi);
        productMiMi.setOrder(this);
        return this;
    }

    public OrderMiMi removeProducts(ProductMiMi productMiMi) {
        this.products.remove(productMiMi);
        productMiMi.setOrder(null);
        return this;
    }

    public PaymentMiMi getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentMiMi paymentMiMi) {
        this.payment = paymentMiMi;
    }

    public OrderMiMi payment(PaymentMiMi paymentMiMi) {
        this.setPayment(paymentMiMi);
        return this;
    }

    public ShipmentMiMi getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentMiMi shipmentMiMi) {
        this.shipment = shipmentMiMi;
    }

    public OrderMiMi shipment(ShipmentMiMi shipmentMiMi) {
        this.setShipment(shipmentMiMi);
        return this;
    }

    public CustomerMiMi getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerMiMi customerMiMi) {
        this.customer = customerMiMi;
    }

    public OrderMiMi customer(CustomerMiMi customerMiMi) {
        this.setCustomer(customerMiMi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMiMi)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderMiMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderMiMi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
