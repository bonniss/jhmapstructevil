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
 * A NextOrderTheta.
 */
@Entity
@Table(name = "next_order_theta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderTheta implements Serializable {

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
    private Set<NextProductTheta> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentTheta payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentTheta shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerTheta customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderTheta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderTheta orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderTheta totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderThetaStatus getStatus() {
        return this.status;
    }

    public NextOrderTheta status(OrderThetaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderThetaStatus status) {
        this.status = status;
    }

    public Set<NextProductTheta> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductTheta> nextProductThetas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductThetas != null) {
            nextProductThetas.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductThetas;
    }

    public NextOrderTheta products(Set<NextProductTheta> nextProductThetas) {
        this.setProducts(nextProductThetas);
        return this;
    }

    public NextOrderTheta addProducts(NextProductTheta nextProductTheta) {
        this.products.add(nextProductTheta);
        nextProductTheta.setOrder(this);
        return this;
    }

    public NextOrderTheta removeProducts(NextProductTheta nextProductTheta) {
        this.products.remove(nextProductTheta);
        nextProductTheta.setOrder(null);
        return this;
    }

    public NextPaymentTheta getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentTheta nextPaymentTheta) {
        this.payment = nextPaymentTheta;
    }

    public NextOrderTheta payment(NextPaymentTheta nextPaymentTheta) {
        this.setPayment(nextPaymentTheta);
        return this;
    }

    public NextShipmentTheta getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentTheta nextShipmentTheta) {
        this.shipment = nextShipmentTheta;
    }

    public NextOrderTheta shipment(NextShipmentTheta nextShipmentTheta) {
        this.setShipment(nextShipmentTheta);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderTheta tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerTheta getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerTheta nextCustomerTheta) {
        this.customer = nextCustomerTheta;
    }

    public NextOrderTheta customer(NextCustomerTheta nextCustomerTheta) {
        this.setCustomer(nextCustomerTheta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderTheta)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderTheta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderTheta{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
