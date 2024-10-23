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
import xyz.jhmapstruct.domain.enumeration.OrderViStatus;

/**
 * A NextOrderVi.
 */
@Entity
@Table(name = "next_order_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderVi implements Serializable {

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
    private OrderViStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<NextProductVi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentVi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentVi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerVi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderVi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderVi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderViStatus getStatus() {
        return this.status;
    }

    public NextOrderVi status(OrderViStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderViStatus status) {
        this.status = status;
    }

    public Set<NextProductVi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductVi> nextProductVis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductVis != null) {
            nextProductVis.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductVis;
    }

    public NextOrderVi products(Set<NextProductVi> nextProductVis) {
        this.setProducts(nextProductVis);
        return this;
    }

    public NextOrderVi addProducts(NextProductVi nextProductVi) {
        this.products.add(nextProductVi);
        nextProductVi.setOrder(this);
        return this;
    }

    public NextOrderVi removeProducts(NextProductVi nextProductVi) {
        this.products.remove(nextProductVi);
        nextProductVi.setOrder(null);
        return this;
    }

    public NextPaymentVi getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentVi nextPaymentVi) {
        this.payment = nextPaymentVi;
    }

    public NextOrderVi payment(NextPaymentVi nextPaymentVi) {
        this.setPayment(nextPaymentVi);
        return this;
    }

    public NextShipmentVi getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentVi nextShipmentVi) {
        this.shipment = nextShipmentVi;
    }

    public NextOrderVi shipment(NextShipmentVi nextShipmentVi) {
        this.setShipment(nextShipmentVi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderVi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerVi getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerVi nextCustomerVi) {
        this.customer = nextCustomerVi;
    }

    public NextOrderVi customer(NextCustomerVi nextCustomerVi) {
        this.setCustomer(nextCustomerVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderVi)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderVi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
