package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlActisoVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlActisoViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-actiso-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlActisoViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter valueJason;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlActisoViCriteria() {}

    public AlActisoViCriteria(AlActisoViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.key = other.optionalKey().map(StringFilter::copy).orElse(null);
        this.valueJason = other.optionalValueJason().map(StringFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlActisoViCriteria copy() {
        return new AlActisoViCriteria(this);
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

    public StringFilter getKey() {
        return key;
    }

    public Optional<StringFilter> optionalKey() {
        return Optional.ofNullable(key);
    }

    public StringFilter key() {
        if (key == null) {
            setKey(new StringFilter());
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getValueJason() {
        return valueJason;
    }

    public Optional<StringFilter> optionalValueJason() {
        return Optional.ofNullable(valueJason);
    }

    public StringFilter valueJason() {
        if (valueJason == null) {
            setValueJason(new StringFilter());
        }
        return valueJason;
    }

    public void setValueJason(StringFilter valueJason) {
        this.valueJason = valueJason;
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
        final AlActisoViCriteria that = (AlActisoViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(valueJason, that.valueJason) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, valueJason, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlActisoViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKey().map(f -> "key=" + f + ", ").orElse("") +
            optionalValueJason().map(f -> "valueJason=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}