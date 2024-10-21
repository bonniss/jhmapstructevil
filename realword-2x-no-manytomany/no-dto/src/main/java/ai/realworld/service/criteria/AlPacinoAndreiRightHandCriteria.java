package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPacinoAndreiRightHand} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPacinoAndreiRightHandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pacino-andrei-right-hands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoAndreiRightHandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter isDefault;

    private UUIDFilter userId;

    private LongFilter addressId;

    private Boolean distinct;

    public AlPacinoAndreiRightHandCriteria() {}

    public AlPacinoAndreiRightHandCriteria(AlPacinoAndreiRightHandCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPacinoAndreiRightHandCriteria copy() {
        return new AlPacinoAndreiRightHandCriteria(this);
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

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public UUIDFilter getUserId() {
        return userId;
    }

    public Optional<UUIDFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public UUIDFilter userId() {
        if (userId == null) {
            setUserId(new UUIDFilter());
        }
        return userId;
    }

    public void setUserId(UUIDFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
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
        final AlPacinoAndreiRightHandCriteria that = (AlPacinoAndreiRightHandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isDefault, userId, addressId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoAndreiRightHandCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
