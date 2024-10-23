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
import xyz.jhmapstruct.domain.enumeration.OrderGammaStatus;

/**
 * A OrderGamma.
 */
@Entity
@Table(name = "order_gamma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderGamma implements Serializable {

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
    private OrderGammaStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductGamma> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentGamma payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentGamma shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerGamma customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderGamma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderGamma orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderGamma totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderGammaStatus getStatus() {
        return this.status;
    }

    public OrderGamma status(OrderGammaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderGammaStatus status) {
        this.status = status;
    }

    public Set<ProductGamma> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductGamma> productGammas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productGammas != null) {
            productGammas.forEach(i -> i.setOrder(this));
        }
        this.products = productGammas;
    }

    public OrderGamma products(Set<ProductGamma> productGammas) {
        this.setProducts(productGammas);
        return this;
    }

    public OrderGamma addProducts(ProductGamma productGamma) {
        this.products.add(productGamma);
        productGamma.setOrder(this);
        return this;
    }

    public OrderGamma removeProducts(ProductGamma productGamma) {
        this.products.remove(productGamma);
        productGamma.setOrder(null);
        return this;
    }

    public PaymentGamma getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentGamma paymentGamma) {
        this.payment = paymentGamma;
    }

    public OrderGamma payment(PaymentGamma paymentGamma) {
        this.setPayment(paymentGamma);
        return this;
    }

    public ShipmentGamma getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentGamma shipmentGamma) {
        this.shipment = shipmentGamma;
    }

    public OrderGamma shipment(ShipmentGamma shipmentGamma) {
        this.setShipment(shipmentGamma);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderGamma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerGamma getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerGamma customerGamma) {
        this.customer = customerGamma;
    }

    public OrderGamma customer(CustomerGamma customerGamma) {
        this.setCustomer(customerGamma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderGamma)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderGamma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderGamma{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
