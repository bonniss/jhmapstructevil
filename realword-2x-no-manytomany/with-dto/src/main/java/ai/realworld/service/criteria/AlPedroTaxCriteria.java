package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.PeteType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPedroTax} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPedroTaxResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pedro-taxes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPedroTaxCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PeteType
     */
    public static class PeteTypeFilter extends Filter<PeteType> {

        public PeteTypeFilter() {}

        public PeteTypeFilter(PeteTypeFilter filter) {
            super(filter);
        }

        @Override
        public PeteTypeFilter copy() {
            return new PeteTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter weight;

    private PeteTypeFilter propertyType;

    private UUIDFilter applicationId;

    private LongFilter attributeTermsId;

    private Boolean distinct;

    public AlPedroTaxCriteria() {}

    public AlPedroTaxCriteria(AlPedroTaxCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(IntegerFilter::copy).orElse(null);
        this.propertyType = other.optionalPropertyType().map(PeteTypeFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.attributeTermsId = other.optionalAttributeTermsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPedroTaxCriteria copy() {
        return new AlPedroTaxCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getWeight() {
        return weight;
    }

    public Optional<IntegerFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public IntegerFilter weight() {
        if (weight == null) {
            setWeight(new IntegerFilter());
        }
        return weight;
    }

    public void setWeight(IntegerFilter weight) {
        this.weight = weight;
    }

    public PeteTypeFilter getPropertyType() {
        return propertyType;
    }

    public Optional<PeteTypeFilter> optionalPropertyType() {
        return Optional.ofNullable(propertyType);
    }

    public PeteTypeFilter propertyType() {
        if (propertyType == null) {
            setPropertyType(new PeteTypeFilter());
        }
        return propertyType;
    }

    public void setPropertyType(PeteTypeFilter propertyType) {
        this.propertyType = propertyType;
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

    public LongFilter getAttributeTermsId() {
        return attributeTermsId;
    }

    public Optional<LongFilter> optionalAttributeTermsId() {
        return Optional.ofNullable(attributeTermsId);
    }

    public LongFilter attributeTermsId() {
        if (attributeTermsId == null) {
            setAttributeTermsId(new LongFilter());
        }
        return attributeTermsId;
    }

    public void setAttributeTermsId(LongFilter attributeTermsId) {
        this.attributeTermsId = attributeTermsId;
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
        final AlPedroTaxCriteria that = (AlPedroTaxCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(propertyType, that.propertyType) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(attributeTermsId, that.attributeTermsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, weight, propertyType, applicationId, attributeTermsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPedroTaxCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalPropertyType().map(f -> "propertyType=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalAttributeTermsId().map(f -> "attributeTermsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
