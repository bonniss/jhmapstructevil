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
 * A OrderViVi.
 */
@Entity
@Table(name = "order_vi_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderViVi implements Serializable {

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
    private Set<ProductViVi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentViVi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentViVi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerViVi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderViVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderViVi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderViVi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderViViStatus getStatus() {
        return this.status;
    }

    public OrderViVi status(OrderViViStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderViViStatus status) {
        this.status = status;
    }

    public Set<ProductViVi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductViVi> productViVis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productViVis != null) {
            productViVis.forEach(i -> i.setOrder(this));
        }
        this.products = productViVis;
    }

    public OrderViVi products(Set<ProductViVi> productViVis) {
        this.setProducts(productViVis);
        return this;
    }

    public OrderViVi addProducts(ProductViVi productViVi) {
        this.products.add(productViVi);
        productViVi.setOrder(this);
        return this;
    }

    public OrderViVi removeProducts(ProductViVi productViVi) {
        this.products.remove(productViVi);
        productViVi.setOrder(null);
        return this;
    }

    public PaymentViVi getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentViVi paymentViVi) {
        this.payment = paymentViVi;
    }

    public OrderViVi payment(PaymentViVi paymentViVi) {
        this.setPayment(paymentViVi);
        return this;
    }

    public ShipmentViVi getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentViVi shipmentViVi) {
        this.shipment = shipmentViVi;
    }

    public OrderViVi shipment(ShipmentViVi shipmentViVi) {
        this.setShipment(shipmentViVi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderViVi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerViVi getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerViVi customerViVi) {
        this.customer = customerViVi;
    }

    public OrderViVi customer(CustomerViVi customerViVi) {
        this.setCustomer(customerViVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderViVi)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderViVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderViVi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
