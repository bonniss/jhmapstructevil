package xyz.jhmapstruct.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextShipmentAlpha} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextShipmentAlphaDTO implements Serializable {

    private Long id;

    @NotNull
    private String trackingNumber;

    private Instant shippedDate;

    private Instant deliveryDate;

    private MasterTenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Instant getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Instant shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextShipmentAlphaDTO)) {
            return false;
        }

        NextShipmentAlphaDTO nextShipmentAlphaDTO = (NextShipmentAlphaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextShipmentAlphaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextShipmentAlphaDTO{" +
            "id=" + getId() +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", shippedDate='" + getShippedDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
