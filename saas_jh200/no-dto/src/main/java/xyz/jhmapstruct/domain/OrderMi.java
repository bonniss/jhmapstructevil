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
import xyz.jhmapstruct.domain.enumeration.OrderMiStatus;

/**
 * A OrderMi.
 */
@Entity
@Table(name = "order_mi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderMi implements Serializable {

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
    private OrderMiStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private Set<ProductMi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextPaymentMi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private NextShipmentMi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private NextCustomerMi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderMi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderMi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderMi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderMiStatus getStatus() {
        return this.status;
    }

    public OrderMi status(OrderMiStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderMiStatus status) {
        this.status = status;
    }

    public Set<ProductMi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductMi> productMis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productMis != null) {
            productMis.forEach(i -> i.setOrder(this));
        }
        this.products = productMis;
    }

    public OrderMi products(Set<ProductMi> productMis) {
        this.setProducts(productMis);
        return this;
    }

    public OrderMi addProducts(ProductMi productMi) {
        this.products.add(productMi);
        productMi.setOrder(this);
        return this;
    }

    public OrderMi removeProducts(ProductMi productMi) {
        this.products.remove(productMi);
        productMi.setOrder(null);
        return this;
    }

    public NextPaymentMi getPayment() {
        return this.payment;
    }

    public void setPayment(NextPaymentMi nextPaymentMi) {
        this.payment = nextPaymentMi;
    }

    public OrderMi payment(NextPaymentMi nextPaymentMi) {
        this.setPayment(nextPaymentMi);
        return this;
    }

    public NextShipmentMi getShipment() {
        return this.shipment;
    }

    public void setShipment(NextShipmentMi nextShipmentMi) {
        this.shipment = nextShipmentMi;
    }

    public OrderMi shipment(NextShipmentMi nextShipmentMi) {
        this.setShipment(nextShipmentMi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderMi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public NextCustomerMi getCustomer() {
        return this.customer;
    }

    public void setCustomer(NextCustomerMi nextCustomerMi) {
        this.customer = nextCustomerMi;
    }

    public OrderMi customer(NextCustomerMi nextCustomerMi) {
        this.setCustomer(nextCustomerMi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderMi)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderMi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderMi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
