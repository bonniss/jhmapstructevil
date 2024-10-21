package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.BenedictToy;
import ai.realworld.domain.enumeration.PeteType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlProProVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlProProViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pro-pro-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProProViCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering BenedictToy
     */
    public static class BenedictToyFilter extends Filter<BenedictToy> {

        public BenedictToyFilter() {}

        public BenedictToyFilter(BenedictToyFilter filter) {
            super(filter);
        }

        @Override
        public BenedictToyFilter copy() {
            return new BenedictToyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter descriptionHeitiga;

    private PeteTypeFilter propertyType;

    private FloatFilter areaInSquareMeter;

    private IntegerFilter numberOfAdults;

    private IntegerFilter numberOfPreschoolers;

    private IntegerFilter numberOfChildren;

    private IntegerFilter numberOfRooms;

    private IntegerFilter numberOfFloors;

    private BenedictToyFilter bedSize;

    private BooleanFilter isEnabled;

    private UUIDFilter parentId;

    private UUIDFilter projectId;

    private LongFilter avatarId;

    private UUIDFilter applicationId;

    private UUIDFilter childrenId;

    private Boolean distinct;

    public AlProProViCriteria() {}

    public AlProProViCriteria(AlProProViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.descriptionHeitiga = other.optionalDescriptionHeitiga().map(StringFilter::copy).orElse(null);
        this.propertyType = other.optionalPropertyType().map(PeteTypeFilter::copy).orElse(null);
        this.areaInSquareMeter = other.optionalAreaInSquareMeter().map(FloatFilter::copy).orElse(null);
        this.numberOfAdults = other.optionalNumberOfAdults().map(IntegerFilter::copy).orElse(null);
        this.numberOfPreschoolers = other.optionalNumberOfPreschoolers().map(IntegerFilter::copy).orElse(null);
        this.numberOfChildren = other.optionalNumberOfChildren().map(IntegerFilter::copy).orElse(null);
        this.numberOfRooms = other.optionalNumberOfRooms().map(IntegerFilter::copy).orElse(null);
        this.numberOfFloors = other.optionalNumberOfFloors().map(IntegerFilter::copy).orElse(null);
        this.bedSize = other.optionalBedSize().map(BenedictToyFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(UUIDFilter::copy).orElse(null);
        this.projectId = other.optionalProjectId().map(UUIDFilter::copy).orElse(null);
        this.avatarId = other.optionalAvatarId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlProProViCriteria copy() {
        return new AlProProViCriteria(this);
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

    public StringFilter getDescriptionHeitiga() {
        return descriptionHeitiga;
    }

    public Optional<StringFilter> optionalDescriptionHeitiga() {
        return Optional.ofNullable(descriptionHeitiga);
    }

    public StringFilter descriptionHeitiga() {
        if (descriptionHeitiga == null) {
            setDescriptionHeitiga(new StringFilter());
        }
        return descriptionHeitiga;
    }

    public void setDescriptionHeitiga(StringFilter descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
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

    public FloatFilter getAreaInSquareMeter() {
        return areaInSquareMeter;
    }

    public Optional<FloatFilter> optionalAreaInSquareMeter() {
        return Optional.ofNullable(areaInSquareMeter);
    }

    public FloatFilter areaInSquareMeter() {
        if (areaInSquareMeter == null) {
            setAreaInSquareMeter(new FloatFilter());
        }
        return areaInSquareMeter;
    }

    public void setAreaInSquareMeter(FloatFilter areaInSquareMeter) {
        this.areaInSquareMeter = areaInSquareMeter;
    }

    public IntegerFilter getNumberOfAdults() {
        return numberOfAdults;
    }

    public Optional<IntegerFilter> optionalNumberOfAdults() {
        return Optional.ofNullable(numberOfAdults);
    }

    public IntegerFilter numberOfAdults() {
        if (numberOfAdults == null) {
            setNumberOfAdults(new IntegerFilter());
        }
        return numberOfAdults;
    }

    public void setNumberOfAdults(IntegerFilter numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public IntegerFilter getNumberOfPreschoolers() {
        return numberOfPreschoolers;
    }

    public Optional<IntegerFilter> optionalNumberOfPreschoolers() {
        return Optional.ofNullable(numberOfPreschoolers);
    }

    public IntegerFilter numberOfPreschoolers() {
        if (numberOfPreschoolers == null) {
            setNumberOfPreschoolers(new IntegerFilter());
        }
        return numberOfPreschoolers;
    }

    public void setNumberOfPreschoolers(IntegerFilter numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public IntegerFilter getNumberOfChildren() {
        return numberOfChildren;
    }

    public Optional<IntegerFilter> optionalNumberOfChildren() {
        return Optional.ofNullable(numberOfChildren);
    }

    public IntegerFilter numberOfChildren() {
        if (numberOfChildren == null) {
            setNumberOfChildren(new IntegerFilter());
        }
        return numberOfChildren;
    }

    public void setNumberOfChildren(IntegerFilter numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public IntegerFilter getNumberOfRooms() {
        return numberOfRooms;
    }

    public Optional<IntegerFilter> optionalNumberOfRooms() {
        return Optional.ofNullable(numberOfRooms);
    }

    public IntegerFilter numberOfRooms() {
        if (numberOfRooms == null) {
            setNumberOfRooms(new IntegerFilter());
        }
        return numberOfRooms;
    }

    public void setNumberOfRooms(IntegerFilter numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public IntegerFilter getNumberOfFloors() {
        return numberOfFloors;
    }

    public Optional<IntegerFilter> optionalNumberOfFloors() {
        return Optional.ofNullable(numberOfFloors);
    }

    public IntegerFilter numberOfFloors() {
        if (numberOfFloors == null) {
            setNumberOfFloors(new IntegerFilter());
        }
        return numberOfFloors;
    }

    public void setNumberOfFloors(IntegerFilter numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public BenedictToyFilter getBedSize() {
        return bedSize;
    }

    public Optional<BenedictToyFilter> optionalBedSize() {
        return Optional.ofNullable(bedSize);
    }

    public BenedictToyFilter bedSize() {
        if (bedSize == null) {
            setBedSize(new BenedictToyFilter());
        }
        return bedSize;
    }

    public void setBedSize(BenedictToyFilter bedSize) {
        this.bedSize = bedSize;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public Optional<BooleanFilter> optionalIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

    public BooleanFilter isEnabled() {
        if (isEnabled == null) {
            setIsEnabled(new BooleanFilter());
        }
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
    }

    public UUIDFilter getParentId() {
        return parentId;
    }

    public Optional<UUIDFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public UUIDFilter parentId() {
        if (parentId == null) {
            setParentId(new UUIDFilter());
        }
        return parentId;
    }

    public void setParentId(UUIDFilter parentId) {
        this.parentId = parentId;
    }

    public UUIDFilter getProjectId() {
        return projectId;
    }

    public Optional<UUIDFilter> optionalProjectId() {
        return Optional.ofNullable(projectId);
    }

    public UUIDFilter projectId() {
        if (projectId == null) {
            setProjectId(new UUIDFilter());
        }
        return projectId;
    }

    public void setProjectId(UUIDFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getAvatarId() {
        return avatarId;
    }

    public Optional<LongFilter> optionalAvatarId() {
        return Optional.ofNullable(avatarId);
    }

    public LongFilter avatarId() {
        if (avatarId == null) {
            setAvatarId(new LongFilter());
        }
        return avatarId;
    }

    public void setAvatarId(LongFilter avatarId) {
        this.avatarId = avatarId;
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

    public UUIDFilter getChildrenId() {
        return childrenId;
    }

    public Optional<UUIDFilter> optionalChildrenId() {
        return Optional.ofNullable(childrenId);
    }

    public UUIDFilter childrenId() {
        if (childrenId == null) {
            setChildrenId(new UUIDFilter());
        }
        return childrenId;
    }

    public void setChildrenId(UUIDFilter childrenId) {
        this.childrenId = childrenId;
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
        final AlProProViCriteria that = (AlProProViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(descriptionHeitiga, that.descriptionHeitiga) &&
            Objects.equals(propertyType, that.propertyType) &&
            Objects.equals(areaInSquareMeter, that.areaInSquareMeter) &&
            Objects.equals(numberOfAdults, that.numberOfAdults) &&
            Objects.equals(numberOfPreschoolers, that.numberOfPreschoolers) &&
            Objects.equals(numberOfChildren, that.numberOfChildren) &&
            Objects.equals(numberOfRooms, that.numberOfRooms) &&
            Objects.equals(numberOfFloors, that.numberOfFloors) &&
            Objects.equals(bedSize, that.bedSize) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            descriptionHeitiga,
            propertyType,
            areaInSquareMeter,
            numberOfAdults,
            numberOfPreschoolers,
            numberOfChildren,
            numberOfRooms,
            numberOfFloors,
            bedSize,
            isEnabled,
            parentId,
            projectId,
            avatarId,
            applicationId,
            childrenId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProProViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescriptionHeitiga().map(f -> "descriptionHeitiga=" + f + ", ").orElse("") +
            optionalPropertyType().map(f -> "propertyType=" + f + ", ").orElse("") +
            optionalAreaInSquareMeter().map(f -> "areaInSquareMeter=" + f + ", ").orElse("") +
            optionalNumberOfAdults().map(f -> "numberOfAdults=" + f + ", ").orElse("") +
            optionalNumberOfPreschoolers().map(f -> "numberOfPreschoolers=" + f + ", ").orElse("") +
            optionalNumberOfChildren().map(f -> "numberOfChildren=" + f + ", ").orElse("") +
            optionalNumberOfRooms().map(f -> "numberOfRooms=" + f + ", ").orElse("") +
            optionalNumberOfFloors().map(f -> "numberOfFloors=" + f + ", ").orElse("") +
            optionalBedSize().map(f -> "bedSize=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalProjectId().map(f -> "projectId=" + f + ", ").orElse("") +
            optionalAvatarId().map(f -> "avatarId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
