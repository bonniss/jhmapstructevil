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
 * A OrderBeta.
 */
@Entity
@Table(name = "order_beta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderBeta implements Serializable {

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
    private Set<ProductBeta> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private PaymentBeta payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ShipmentBeta shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "tenant" }, allowSetters = true)
    private CustomerBeta customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderBeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public OrderBeta orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderBeta totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderBetaStatus getStatus() {
        return this.status;
    }

    public OrderBeta status(OrderBetaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderBetaStatus status) {
        this.status = status;
    }

    public Set<ProductBeta> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductBeta> productBetas) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (productBetas != null) {
            productBetas.forEach(i -> i.setOrder(this));
        }
        this.products = productBetas;
    }

    public OrderBeta products(Set<ProductBeta> productBetas) {
        this.setProducts(productBetas);
        return this;
    }

    public OrderBeta addProducts(ProductBeta productBeta) {
        this.products.add(productBeta);
        productBeta.setOrder(this);
        return this;
    }

    public OrderBeta removeProducts(ProductBeta productBeta) {
        this.products.remove(productBeta);
        productBeta.setOrder(null);
        return this;
    }

    public PaymentBeta getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentBeta paymentBeta) {
        this.payment = paymentBeta;
    }

    public OrderBeta payment(PaymentBeta paymentBeta) {
        this.setPayment(paymentBeta);
        return this;
    }

    public ShipmentBeta getShipment() {
        return this.shipment;
    }

    public void setShipment(ShipmentBeta shipmentBeta) {
        this.shipment = shipmentBeta;
    }

    public OrderBeta shipment(ShipmentBeta shipmentBeta) {
        this.setShipment(shipmentBeta);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public OrderBeta tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    public CustomerBeta getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerBeta customerBeta) {
        this.customer = customerBeta;
    }

    public OrderBeta customer(CustomerBeta customerBeta) {
        this.setCustomer(customerBeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderBeta)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderBeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderBeta{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
