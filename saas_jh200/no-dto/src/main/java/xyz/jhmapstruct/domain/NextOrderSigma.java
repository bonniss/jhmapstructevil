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
 * A NextOrderSigma.
 */
@Entity
@Table(name = "next_order_sigma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderSigma implements Serializable {

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
    private Set<NextProductSigma> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentSigma payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentSigma shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerSigma customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderSigma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderSigma orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderSigma totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderSigmaStatus getStatus() {
        return this.status;
    }

    public NextOrderSigma status(OrderSigmaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderSigmaStatus status) {
        this.status = status;
    }

    public Set<NextProductSigma> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductSigma> nextProductSigmas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductSigmas != null) {
            nextProductSigmas.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductSigmas;
    }

    public NextOrderSigma products(Set<NextProductSigma> nextProductSigmas) {
        this.setProducts(nextProductSigmas);
        return this;
    }

    public NextOrderSigma addProducts(NextProductSigma nextProductSigma) {
        this.products.add(nextProductSigma);
        nextProductSigma.setOrder(this);
        return this;
    }

    public NextOrderSigma removeProducts(NextProductSigma nextProductSigma) {
        this.products.remove(nextProductSigma);
        nextProductSigma.setOrder(null);
        return this;
    }

    public NextPaymentSigma getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentSigma nextPaymentSigma) {
        this.payment = nextPaymentSigma;
    }

    public NextOrderSigma payment(NextPaymentSigma nextPaymentSigma) {
        this.setPayment(nextPaymentSigma);
        return this;
    }

    public NextShipmentSigma getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentSigma nextShipmentSigma) {
        this.shipment = nextShipmentSigma;
    }

    public NextOrderSigma shipment(NextShipmentSigma nextShipmentSigma) {
        this.setShipment(nextShipmentSigma);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderSigma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerSigma getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerSigma nextCustomerSigma) {
        this.customer = nextCustomerSigma;
    }

    public NextOrderSigma customer(NextCustomerSigma nextCustomerSigma) {
        this.setCustomer(nextCustomerSigma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderSigma)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderSigma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderSigma{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
