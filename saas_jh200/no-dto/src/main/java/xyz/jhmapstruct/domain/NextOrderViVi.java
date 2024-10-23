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
import xyz.jhmapstruct.domain.enumeration.OrderViViStatus;

/**
 * A NextOrderViVi.
 */
@Entity
@Table(name = "next_order_vi_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderViVi implements Serializable {

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
    private OrderViViStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<NextProductViVi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentViVi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentViVi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerViVi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderViVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderViVi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderViVi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderViViStatus getStatus() {
        return this.status;
    }

    public NextOrderViVi status(OrderViViStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderViViStatus status) {
        this.status = status;
    }

    public Set<NextProductViVi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductViVi> nextProductViVis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductViVis != null) {
            nextProductViVis.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductViVis;
    }

    public NextOrderViVi products(Set<NextProductViVi> nextProductViVis) {
        this.setProducts(nextProductViVis);
        return this;
    }

    public NextOrderViVi addProducts(NextProductViVi nextProductViVi) {
        this.products.add(nextProductViVi);
        nextProductViVi.setOrder(this);
        return this;
    }

    public NextOrderViVi removeProducts(NextProductViVi nextProductViVi) {
        this.products.remove(nextProductViVi);
        nextProductViVi.setOrder(null);
        return this;
    }

    public NextPaymentViVi getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentViVi nextPaymentViVi) {
        this.payment = nextPaymentViVi;
    }

    public NextOrderViVi payment(NextPaymentViVi nextPaymentViVi) {
        this.setPayment(nextPaymentViVi);
        return this;
    }

    public NextShipmentViVi getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentViVi nextShipmentViVi) {
        this.shipment = nextShipmentViVi;
    }

    public NextOrderViVi shipment(NextShipmentViVi nextShipmentViVi) {
        this.setShipment(nextShipmentViVi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderViVi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerViVi getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerViVi nextCustomerViVi) {
        this.customer = nextCustomerViVi;
    }

    public NextOrderViVi customer(NextCustomerViVi nextCustomerViVi) {
        this.setCustomer(nextCustomerViVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderViVi)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderViVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderViVi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
