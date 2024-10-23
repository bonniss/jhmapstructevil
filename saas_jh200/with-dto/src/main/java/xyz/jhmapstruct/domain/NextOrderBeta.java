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
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;

/**
 * A NextOrderBeta.
 */
@Entity
@Table(name = "next_order_beta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderBeta implements Serializable {

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
    private OrderBetaStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<NextProductBeta> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentBeta payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentBeta shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerBeta customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderBeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderBeta orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderBeta totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderBetaStatus getStatus() {
        return this.status;
    }

    public NextOrderBeta status(OrderBetaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderBetaStatus status) {
        this.status = status;
    }

    public Set<NextProductBeta> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductBeta> nextProductBetas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductBetas != null) {
            nextProductBetas.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductBetas;
    }

    public NextOrderBeta products(Set<NextProductBeta> nextProductBetas) {
        this.setProducts(nextProductBetas);
        return this;
    }

    public NextOrderBeta addProducts(NextProductBeta nextProductBeta) {
        this.products.add(nextProductBeta);
        nextProductBeta.setOrder(this);
        return this;
    }

    public NextOrderBeta removeProducts(NextProductBeta nextProductBeta) {
        this.products.remove(nextProductBeta);
        nextProductBeta.setOrder(null);
        return this;
    }

    public NextPaymentBeta getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentBeta nextPaymentBeta) {
        this.payment = nextPaymentBeta;
    }

    public NextOrderBeta payment(NextPaymentBeta nextPaymentBeta) {
        this.setPayment(nextPaymentBeta);
        return this;
    }

    public NextShipmentBeta getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentBeta nextShipmentBeta) {
        this.shipment = nextShipmentBeta;
    }

    public NextOrderBeta shipment(NextShipmentBeta nextShipmentBeta) {
        this.setShipment(nextShipmentBeta);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderBeta tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerBeta getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerBeta nextCustomerBeta) {
        this.customer = nextCustomerBeta;
    }

    public NextOrderBeta customer(NextCustomerBeta nextCustomerBeta) {
        this.setCustomer(nextCustomerBeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderBeta)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderBeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderBeta{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
