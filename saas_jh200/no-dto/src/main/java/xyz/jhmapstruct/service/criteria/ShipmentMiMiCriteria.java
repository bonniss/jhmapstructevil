package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.ShipmentMiMi} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.ShipmentMiMiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipment-mi-mis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentMiMiCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter trackingNumber;

    private InstantFilter shippedDate;

    private InstantFilter deliveryDate;

    private LongFilter tenantId;

    private Boolean distinct;

    public ShipmentMiMiCriteria() {}

    public ShipmentMiMiCriteria(ShipmentMiMiCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.trackingNumber = other.optionalTrackingNumber().map(StringFilter::copy).orElse(null);
        this.shippedDate = other.optionalShippedDate().map(InstantFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShipmentMiMiCriteria copy() {
        return new ShipmentMiMiCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTrackingNumber() {
        return trackingNumber;
    }

    public Optional<StringFilter> optionalTrackingNumber() {
        return Optional.ofNullable(trackingNumber);
    }

    public StringFilter trackingNumber() {
        if (trackingNumber == null) {
            setTrackingNumber(new StringFilter());
        }
        return trackingNumber;
    }

    public void setTrackingNumber(StringFilter trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public InstantFilter getShippedDate() {
        return shippedDate;
    }

    public Optional<InstantFilter> optionalShippedDate() {
        return Optional.ofNullable(shippedDate);
    }

    public InstantFilter shippedDate() {
        if (shippedDate == null) {
            setShippedDate(new InstantFilter());
        }
        return shippedDate;
    }

    public void setShippedDate(InstantFilter shippedDate) {
        this.shippedDate = shippedDate;
    }

    public InstantFilter getDeliveryDate() {
        return deliveryDate;
    }

    public Optional<InstantFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public InstantFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new InstantFilter());
        }
        return deliveryDate;
    }

    public void setDeliveryDate(InstantFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShipmentMiMiCriteria that = (ShipmentMiMiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(trackingNumber, that.trackingNumber) &&
            Objects.equals(shippedDate, that.shippedDate) &&
            Objects.equals(deliveryDate, that.deliveryDate) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackingNumber, shippedDate, deliveryDate, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentMiMiCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTrackingNumber().map(f -> "trackingNumber=" + f + ", ").orElse("") +
            optionalShippedDate().map(f -> "shippedDate=" + f + ", ").orElse("") +
            optionalDeliveryDate().map(f -> "deliveryDate=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
