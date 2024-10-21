package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.TyrantSex;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.HexChar} entity. This class is used
 * in {@link ai.realworld.web.rest.HexCharResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hex-chars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HexCharCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TyrantSex
     */
    public static class TyrantSexFilter extends Filter<TyrantSex> {

        public TyrantSexFilter() {}

        public TyrantSexFilter(TyrantSexFilter filter) {
            super(filter);
        }

        @Override
        public TyrantSexFilter copy() {
            return new TyrantSexFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dob;

    private TyrantSexFilter gender;

    private StringFilter phone;

    private StringFilter bioHeitiga;

    private BooleanFilter isEnabled;

    private LongFilter internalUserId;

    private LongFilter roleId;

    private Boolean distinct;

    public HexCharCriteria() {}

    public HexCharCriteria(HexCharCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dob = other.optionalDob().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(TyrantSexFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.bioHeitiga = other.optionalBioHeitiga().map(StringFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.roleId = other.optionalRoleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HexCharCriteria copy() {
        return new HexCharCriteria(this);
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

    public LocalDateFilter getDob() {
        return dob;
    }

    public Optional<LocalDateFilter> optionalDob() {
        return Optional.ofNullable(dob);
    }

    public LocalDateFilter dob() {
        if (dob == null) {
            setDob(new LocalDateFilter());
        }
        return dob;
    }

    public void setDob(LocalDateFilter dob) {
        this.dob = dob;
    }

    public TyrantSexFilter getGender() {
        return gender;
    }

    public Optional<TyrantSexFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public TyrantSexFilter gender() {
        if (gender == null) {
            setGender(new TyrantSexFilter());
        }
        return gender;
    }

    public void setGender(TyrantSexFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getBioHeitiga() {
        return bioHeitiga;
    }

    public Optional<StringFilter> optionalBioHeitiga() {
        return Optional.ofNullable(bioHeitiga);
    }

    public StringFilter bioHeitiga() {
        if (bioHeitiga == null) {
            setBioHeitiga(new StringFilter());
        }
        return bioHeitiga;
    }

    public void setBioHeitiga(StringFilter bioHeitiga) {
        this.bioHeitiga = bioHeitiga;
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

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public Optional<LongFilter> optionalInternalUserId() {
        return Optional.ofNullable(internalUserId);
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            setInternalUserId(new LongFilter());
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public Optional<LongFilter> optionalRoleId() {
        return Optional.ofNullable(roleId);
    }

    public LongFilter roleId() {
        if (roleId == null) {
            setRoleId(new LongFilter());
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final HexCharCriteria that = (HexCharCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(bioHeitiga, that.bioHeitiga) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dob, gender, phone, bioHeitiga, isEnabled, internalUserId, roleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HexCharCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDob().map(f -> "dob=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalBioHeitiga().map(f -> "bioHeitiga=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalRoleId().map(f -> "roleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
