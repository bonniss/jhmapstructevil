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
 * A NextOrderMiMi.
 */
@Entity
@Table(name = "next_order_mi_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOrderMiMi implements Serializable {

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
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<NextProductMiMi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentMiMi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentMiMi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerMiMi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOrderMiMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public NextOrderMiMi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public NextOrderMiMi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderMiMiStatus getStatus() {
        return this.status;
    }

    public NextOrderMiMi status(OrderMiMiStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderMiMiStatus status) {
        this.status = status;
    }

    public Set<NextProductMiMi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<NextProductMiMi> nextProductMiMis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (nextProductMiMis != null) {
            nextProductMiMis.forEach(i -> i.setOrder(this));
        }
        this.products = nextProductMiMis;
    }

    public NextOrderMiMi products(Set<NextProductMiMi> nextProductMiMis) {
        this.setProducts(nextProductMiMis);
        return this;
    }

    public NextOrderMiMi addProducts(NextProductMiMi nextProductMiMi) {
        this.products.add(nextProductMiMi);
        nextProductMiMi.setOrder(this);
        return this;
    }

    public NextOrderMiMi removeProducts(NextProductMiMi nextProductMiMi) {
        this.products.remove(nextProductMiMi);
        nextProductMiMi.setOrder(null);
        return this;
    }

    public NextPaymentMiMi getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentMiMi nextPaymentMiMi) {
        this.payment = nextPaymentMiMi;
    }

    public NextOrderMiMi payment(NextPaymentMiMi nextPaymentMiMi) {
        this.setPayment(nextPaymentMiMi);
        return this;
    }

    public NextShipmentMiMi getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentMiMi nextShipmentMiMi) {
        this.shipment = nextShipmentMiMi;
    }

    public NextOrderMiMi shipment(NextShipmentMiMi nextShipmentMiMi) {
        this.setShipment(nextShipmentMiMi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public NextOrderMiMi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerMiMi getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerMiMi nextCustomerMiMi) {
        this.customer = nextCustomerMiMi;
    }

    public NextOrderMiMi customer(NextCustomerMiMi nextCustomerMiMi) {
        this.setCustomer(nextCustomerMiMi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOrderMiMi)) {
            return false;
        }
        return getId() != null && getId().equals(((NextOrderMiMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOrderMiMi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
