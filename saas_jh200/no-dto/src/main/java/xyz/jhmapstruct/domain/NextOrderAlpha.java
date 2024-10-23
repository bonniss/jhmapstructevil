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
 * A NextOrderAlpha.
 */
@Entity
@Table(name = "next_order_alpha")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderAlpha implements Serializable {

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
    private Set<NextProductAlpha> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentAlpha payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentAlpha shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerAlpha customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderAlpha id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderAlpha orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderAlpha totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderAlphaStatus getStatus() {
        return this.status;
    }

    public NextOrderAlpha status(OrderAlphaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderAlphaStatus status) {
        this.status = status;
    }

    public Set<NextProductAlpha> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductAlpha> nextProductAlphas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductAlphas != null) {
            nextProductAlphas.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductAlphas;
    }

    public NextOrderAlpha products(Set<NextProductAlpha> nextProductAlphas) {
        this.setProducts(nextProductAlphas);
        return this;
    }

    public NextOrderAlpha addProducts(NextProductAlpha nextProductAlpha) {
        this.products.add(nextProductAlpha);
        nextProductAlpha.setOrder(this);
        return this;
    }

    public NextOrderAlpha removeProducts(NextProductAlpha nextProductAlpha) {
        this.products.remove(nextProductAlpha);
        nextProductAlpha.setOrder(null);
        return this;
    }

    public NextPaymentAlpha getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentAlpha nextPaymentAlpha) {
        this.payment = nextPaymentAlpha;
    }

    public NextOrderAlpha payment(NextPaymentAlpha nextPaymentAlpha) {
        this.setPayment(nextPaymentAlpha);
        return this;
    }

    public NextShipmentAlpha getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentAlpha nextShipmentAlpha) {
        this.shipment = nextShipmentAlpha;
    }

    public NextOrderAlpha shipment(NextShipmentAlpha nextShipmentAlpha) {
        this.setShipment(nextShipmentAlpha);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderAlpha tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerAlpha getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerAlpha nextCustomerAlpha) {
        this.customer = nextCustomerAlpha;
    }

    public NextOrderAlpha customer(NextCustomerAlpha nextCustomerAlpha) {
        this.setCustomer(nextCustomerAlpha);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderAlpha)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderAlpha) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderAlpha{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
