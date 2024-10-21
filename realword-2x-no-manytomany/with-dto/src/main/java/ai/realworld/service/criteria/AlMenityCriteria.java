package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.PeteType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlMenity} entity. This class is used
 * in {@link ai.realworld.web.rest.AlMenityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-menities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlMenityCriteria implements Serializable, Criteria {

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

    private StringFilter iconSvg;

    private PeteTypeFilter propertyType;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlMenityCriteria() {}

    public AlMenityCriteria(AlMenityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.iconSvg = other.optionalIconSvg().map(StringFilter::copy).orElse(null);
        this.propertyType = other.optionalPropertyType().map(PeteTypeFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlMenityCriteria copy() {
        return new AlMenityCriteria(this);
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

    public StringFilter getIconSvg() {
        return iconSvg;
    }

    public Optional<StringFilter> optionalIconSvg() {
        return Optional.ofNullable(iconSvg);
    }

    public StringFilter iconSvg() {
        if (iconSvg == null) {
            setIconSvg(new StringFilter());
        }
        return iconSvg;
    }

    public void setIconSvg(StringFilter iconSvg) {
        this.iconSvg = iconSvg;
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
        final AlMenityCriteria that = (AlMenityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(iconSvg, that.iconSvg) &&
            Objects.equals(propertyType, that.propertyType) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iconSvg, propertyType, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlMenityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalIconSvg().map(f -> "iconSvg=" + f + ", ").orElse("") +
            optionalPropertyType().map(f -> "propertyType=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
