package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlVueVueUsage} entity. This class is used
 * in {@link ai.realworld.web.rest.AlVueVueUsageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-vue-vue-usages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueUsageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter applicationId;

    private UUIDFilter voucherId;

    private UUIDFilter customerId;

    private Boolean distinct;

    public AlVueVueUsageCriteria() {}

    public AlVueVueUsageCriteria(AlVueVueUsageCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.voucherId = other.optionalVoucherId().map(UUIDFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlVueVueUsageCriteria copy() {
        return new AlVueVueUsageCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public UUIDFilter getApplicationId() {
        return applicationId;
    }

    public Optional<UUIDFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public UUIDFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new UUIDFilter());
        }
        return applicationId;
    }

    public void setApplicationId(UUIDFilter applicationId) {
        this.applicationId = applicationId;
    }

    public UUIDFilter getVoucherId() {
        return voucherId;
    }

    public Optional<UUIDFilter> optionalVoucherId() {
        return Optional.ofNullable(voucherId);
    }

    public UUIDFilter voucherId() {
        if (voucherId == null) {
            setVoucherId(new UUIDFilter());
        }
        return voucherId;
    }

    public void setVoucherId(UUIDFilter voucherId) {
        this.voucherId = voucherId;
    }

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
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
        final AlVueVueUsageCriteria that = (AlVueVueUsageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(voucherId, that.voucherId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicationId, voucherId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueUsageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalVoucherId().map(f -> "voucherId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}