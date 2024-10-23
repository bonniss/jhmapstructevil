package xyz.jhmapstruct.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShipmentBeta.
 */
@Entity
@Table(name = "shipment_beta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentBeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "shipped_date")
    private Instant shippedDate;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentBeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public ShipmentBeta trackingNumber(String trackingNumber) {
        this.setTrackingNumber(trackingNumber);
        return this;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Instant getShippedDate() {
        return this.shippedDate;
    }

    public ShipmentBeta shippedDate(Instant shippedDate) {
        this.setShippedDate(shippedDate);
        return this;
    }

    public void setShippedDate(Instant shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Instant getDeliveryDate() {
        return this.deliveryDate;
    }

    public ShipmentBeta deliveryDate(Instant deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public ShipmentBeta tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentBeta)) {
            return false;
        }
        return getId() != null && getId().equals(((ShipmentBeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentBeta{" +
            "id=" + getId() +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", shippedDate='" + getShippedDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            "}";
    }
}
