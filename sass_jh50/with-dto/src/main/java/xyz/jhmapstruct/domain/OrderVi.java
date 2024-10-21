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
 * A OrderVi.
 */
@Entity
@Table(name = "order_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderVi implements Serializable {

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
    private Set<ProductVi> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentVi payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentVi shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerVi customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderVi orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderVi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderViStatus getStatus() {
        return this.status;
    }

    public OrderVi status(OrderViStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderViStatus status) {
        this.status = status;
    }

    public Set<ProductVi> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductVi> productVis) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productVis != null) {
            productVis.forEach(i -> i.setOrder(this));
        }
        this.products = productVis;
    }

    public OrderVi products(Set<ProductVi> productVis) {
        this.setProducts(productVis);
        return this;
    }

    public OrderVi addProducts(ProductVi productVi) {
        this.products.add(productVi);
        productVi.setOrder(this);
        return this;
    }

    public OrderVi removeProducts(ProductVi productVi) {
        this.products.remove(productVi);
        productVi.setOrder(null);
        return this;
    }

    public PaymentVi getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentVi paymentVi) {
        this.payment = paymentVi;
    }

    public OrderVi payment(PaymentVi paymentVi) {
        this.setPayment(paymentVi);
        return this;
    }

    public ShipmentVi getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentVi shipmentVi) {
        this.shipment = shipmentVi;
    }

    public OrderVi shipment(ShipmentVi shipmentVi) {
        this.setShipment(shipmentVi);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderVi tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerVi getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerVi customerVi) {
        this.customer = customerVi;
    }

    public OrderVi customer(CustomerVi customerVi) {
        this.setCustomer(customerVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderVi)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderVi{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
