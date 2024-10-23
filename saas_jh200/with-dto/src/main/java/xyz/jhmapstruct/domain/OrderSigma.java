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
import xyz.jhmapstruct.domain.enumeration.OrderSigmaStatus;

/**
 * A OrderSigma.
 */
@Entity
@Table(name = "order_sigma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderSigma implements Serializable {

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
    private OrderSigmaStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductSigma> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentSigma payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentSigma shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerSigma customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderSigma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderSigma orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderSigma totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderSigmaStatus getStatus() {
        return this.status;
    }

    public OrderSigma status(OrderSigmaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderSigmaStatus status) {
        this.status = status;
    }

    public Set<ProductSigma> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductSigma> productSigmas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productSigmas != null) {
            productSigmas.forEach(i -> i.setOrder(this));
        }
        this.products = productSigmas;
    }

    public OrderSigma products(Set<ProductSigma> productSigmas) {
        this.setProducts(productSigmas);
        return this;
    }

    public OrderSigma addProducts(ProductSigma productSigma) {
        this.products.add(productSigma);
        productSigma.setOrder(this);
        return this;
    }

    public OrderSigma removeProducts(ProductSigma productSigma) {
        this.products.remove(productSigma);
        productSigma.setOrder(null);
        return this;
    }

    public PaymentSigma getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentSigma paymentSigma) {
        this.payment = paymentSigma;
    }

    public OrderSigma payment(PaymentSigma paymentSigma) {
        this.setPayment(paymentSigma);
        return this;
    }

    public ShipmentSigma getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentSigma shipmentSigma) {
        this.shipment = shipmentSigma;
    }

    public OrderSigma shipment(ShipmentSigma shipmentSigma) {
        this.setShipment(shipmentSigma);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderSigma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerSigma getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerSigma customerSigma) {
        this.customer = customerSigma;
    }

    public OrderSigma customer(CustomerSigma customerSigma) {
        this.setCustomer(customerSigma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderSigma)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderSigma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderSigma{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
