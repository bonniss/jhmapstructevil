package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPounderVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPounderViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pounder-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPounderViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter weight;

    private LongFilter attributeTaxonomyId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlPounderViCriteria() {}

    public AlPounderViCriteria(AlPounderViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(IntegerFilter::copy).orElse(null);
        this.attributeTaxonomyId = other.optionalAttributeTaxonomyId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPounderViCriteria copy() {
        return new AlPounderViCriteria(this);
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

    public LongFilter getAttributeTaxonomyId() {
        return attributeTaxonomyId;
    }

    public Optional<LongFilter> optionalAttributeTaxonomyId() {
        return Optional.ofNullable(attributeTaxonomyId);
    }

    public LongFilter attributeTaxonomyId() {
        if (attributeTaxonomyId == null) {
            setAttributeTaxonomyId(new LongFilter());
        }
        return attributeTaxonomyId;
    }

    public void setAttributeTaxonomyId(LongFilter attributeTaxonomyId) {
        this.attributeTaxonomyId = attributeTaxonomyId;
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
        final AlPounderViCriteria that = (AlPounderViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(attributeTaxonomyId, that.attributeTaxonomyId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, attributeTaxonomyId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPounderViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalAttributeTaxonomyId().map(f -> "attributeTaxonomyId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
