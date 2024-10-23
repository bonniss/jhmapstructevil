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
 * A NextOrderGamma.
 */
@Entity
@Table(name = "next_order_gamma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderGamma implements Serializable {

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
    private Set<NextProductGamma> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentGamma payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentGamma shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerGamma customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderGamma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderGamma orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderGamma totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderGammaStatus getStatus() {
        return this.status;
    }

    public NextOrderGamma status(OrderGammaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderGammaStatus status) {
        this.status = status;
    }

    public Set<NextProductGamma> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductGamma> nextProductGammas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductGammas != null) {
            nextProductGammas.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductGammas;
    }

    public NextOrderGamma products(Set<NextProductGamma> nextProductGammas) {
        this.setProducts(nextProductGammas);
        return this;
    }

    public NextOrderGamma addProducts(NextProductGamma nextProductGamma) {
        this.products.add(nextProductGamma);
        nextProductGamma.setOrder(this);
        return this;
    }

    public NextOrderGamma removeProducts(NextProductGamma nextProductGamma) {
        this.products.remove(nextProductGamma);
        nextProductGamma.setOrder(null);
        return this;
    }

    public NextPaymentGamma getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentGamma nextPaymentGamma) {
        this.payment = nextPaymentGamma;
    }

    public NextOrderGamma payment(NextPaymentGamma nextPaymentGamma) {
        this.setPayment(nextPaymentGamma);
        return this;
    }

    public NextShipmentGamma getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentGamma nextShipmentGamma) {
        this.shipment = nextShipmentGamma;
    }

    public NextOrderGamma shipment(NextShipmentGamma nextShipmentGamma) {
        this.setShipment(nextShipmentGamma);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderGamma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerGamma getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerGamma nextCustomerGamma) {
        this.customer = nextCustomerGamma;
    }

    public NextOrderGamma customer(NextCustomerGamma nextCustomerGamma) {
        this.setCustomer(nextCustomerGamma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderGamma)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderGamma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderGamma{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
