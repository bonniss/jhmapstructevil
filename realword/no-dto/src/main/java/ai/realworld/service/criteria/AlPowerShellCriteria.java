package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPowerShell} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPowerShellResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-power-shells?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPowerShellCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter value;

    private UUIDFilter propertyProfileId;

    private LongFilter attributeTermId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlPowerShellCriteria() {}

    public AlPowerShellCriteria(AlPowerShellCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.value = other.optionalValue().map(StringFilter::copy).orElse(null);
        this.propertyProfileId = other.optionalPropertyProfileId().map(UUIDFilter::copy).orElse(null);
        this.attributeTermId = other.optionalAttributeTermId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPowerShellCriteria copy() {
        return new AlPowerShellCriteria(this);
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

    public StringFilter getValue() {
        return value;
    }

    public Optional<StringFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public StringFilter value() {
        if (value == null) {
            setValue(new StringFilter());
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public UUIDFilter getPropertyProfileId() {
        return propertyProfileId;
    }

    public Optional<UUIDFilter> optionalPropertyProfileId() {
        return Optional.ofNullable(propertyProfileId);
    }

    public UUIDFilter propertyProfileId() {
        if (propertyProfileId == null) {
            setPropertyProfileId(new UUIDFilter());
        }
        return propertyProfileId;
    }

    public void setPropertyProfileId(UUIDFilter propertyProfileId) {
        this.propertyProfileId = propertyProfileId;
    }

    public LongFilter getAttributeTermId() {
        return attributeTermId;
    }

    public Optional<LongFilter> optionalAttributeTermId() {
        return Optional.ofNullable(attributeTermId);
    }

    public LongFilter attributeTermId() {
        if (attributeTermId == null) {
            setAttributeTermId(new LongFilter());
        }
        return attributeTermId;
    }

    public void setAttributeTermId(LongFilter attributeTermId) {
        this.attributeTermId = attributeTermId;
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
        final AlPowerShellCriteria that = (AlPowerShellCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(propertyProfileId, that.propertyProfileId) &&
            Objects.equals(attributeTermId, that.attributeTermId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, propertyProfileId, attributeTermId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPowerShellCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalPropertyProfileId().map(f -> "propertyProfileId=" + f + ", ").orElse("") +
            optionalAttributeTermId().map(f -> "attributeTermId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
