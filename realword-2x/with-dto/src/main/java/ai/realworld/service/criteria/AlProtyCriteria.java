package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.PeteStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlProty} entity. This class is used
 * in {@link ai.realworld.web.rest.AlProtyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-proties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProtyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PeteStatus
     */
    public static class PeteStatusFilter extends Filter<PeteStatus> {

        public PeteStatusFilter() {}

        public PeteStatusFilter(PeteStatusFilter filter) {
            super(filter);
        }

        @Override
        public PeteStatusFilter copy() {
            return new PeteStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter descriptionHeitiga;

    private StringFilter coordinate;

    private StringFilter code;

    private PeteStatusFilter status;

    private BooleanFilter isEnabled;

    private UUIDFilter parentId;

    private UUIDFilter operatorId;

    private UUIDFilter propertyProfileId;

    private LongFilter avatarId;

    private UUIDFilter applicationId;

    private LongFilter imageId;

    private UUIDFilter childrenId;

    private UUIDFilter bookingId;

    private Boolean distinct;

    public AlProtyCriteria() {}

    public AlProtyCriteria(AlProtyCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.descriptionHeitiga = other.optionalDescriptionHeitiga().map(StringFilter::copy).orElse(null);
        this.coordinate = other.optionalCoordinate().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PeteStatusFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(UUIDFilter::copy).orElse(null);
        this.operatorId = other.optionalOperatorId().map(UUIDFilter::copy).orElse(null);
        this.propertyProfileId = other.optionalPropertyProfileId().map(UUIDFilter::copy).orElse(null);
        this.avatarId = other.optionalAvatarId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.imageId = other.optionalImageId().map(LongFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(UUIDFilter::copy).orElse(null);
        this.bookingId = other.optionalBookingId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlProtyCriteria copy() {
        return new AlProtyCriteria(this);
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

    public StringFilter getCoordinate() {
        return coordinate;
    }

    public Optional<StringFilter> optionalCoordinate() {
        return Optional.ofNullable(coordinate);
    }

    public StringFilter coordinate() {
        if (coordinate == null) {
            setCoordinate(new StringFilter());
        }
        return coordinate;
    }

    public void setCoordinate(StringFilter coordinate) {
        this.coordinate = coordinate;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public PeteStatusFilter getStatus() {
        return status;
    }

    public Optional<PeteStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PeteStatusFilter status() {
        if (status == null) {
            setStatus(new PeteStatusFilter());
        }
        return status;
    }

    public void setStatus(PeteStatusFilter status) {
        this.status = status;
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

    public UUIDFilter getOperatorId() {
        return operatorId;
    }

    public Optional<UUIDFilter> optionalOperatorId() {
        return Optional.ofNullable(operatorId);
    }

    public UUIDFilter operatorId() {
        if (operatorId == null) {
            setOperatorId(new UUIDFilter());
        }
        return operatorId;
    }

    public void setOperatorId(UUIDFilter operatorId) {
        this.operatorId = operatorId;
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

    public LongFilter getImageId() {
        return imageId;
    }

    public Optional<LongFilter> optionalImageId() {
        return Optional.ofNullable(imageId);
    }

    public LongFilter imageId() {
        if (imageId == null) {
            setImageId(new LongFilter());
        }
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
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

    public UUIDFilter getBookingId() {
        return bookingId;
    }

    public Optional<UUIDFilter> optionalBookingId() {
        return Optional.ofNullable(bookingId);
    }

    public UUIDFilter bookingId() {
        if (bookingId == null) {
            setBookingId(new UUIDFilter());
        }
        return bookingId;
    }

    public void setBookingId(UUIDFilter bookingId) {
        this.bookingId = bookingId;
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
        final AlProtyCriteria that = (AlProtyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(descriptionHeitiga, that.descriptionHeitiga) &&
            Objects.equals(coordinate, that.coordinate) &&
            Objects.equals(code, that.code) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(operatorId, that.operatorId) &&
            Objects.equals(propertyProfileId, that.propertyProfileId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            descriptionHeitiga,
            coordinate,
            code,
            status,
            isEnabled,
            parentId,
            operatorId,
            propertyProfileId,
            avatarId,
            applicationId,
            imageId,
            childrenId,
            bookingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProtyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescriptionHeitiga().map(f -> "descriptionHeitiga=" + f + ", ").orElse("") +
            optionalCoordinate().map(f -> "coordinate=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalOperatorId().map(f -> "operatorId=" + f + ", ").orElse("") +
            optionalPropertyProfileId().map(f -> "propertyProfileId=" + f + ", ").orElse("") +
            optionalAvatarId().map(f -> "avatarId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalImageId().map(f -> "imageId=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalBookingId().map(f -> "bookingId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
