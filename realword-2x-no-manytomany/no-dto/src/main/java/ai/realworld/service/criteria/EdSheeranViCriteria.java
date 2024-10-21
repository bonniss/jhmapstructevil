package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.TyrantSex;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.EdSheeranVi} entity. This class is used
 * in {@link ai.realworld.web.rest.EdSheeranViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ed-sheeran-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EdSheeranViCriteria implements Serializable, Criteria {

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

    private StringFilter familyName;

    private StringFilter givenName;

    private StringFilter display;

    private LocalDateFilter dob;

    private TyrantSexFilter gender;

    private StringFilter phone;

    private StringFilter contactsJason;

    private BooleanFilter isEnabled;

    private UUIDFilter agencyId;

    private LongFilter avatarId;

    private LongFilter internalUserId;

    private UUIDFilter appUserId;

    private UUIDFilter applicationId;

    private LongFilter agentRolesId;

    private Boolean distinct;

    public EdSheeranViCriteria() {}

    public EdSheeranViCriteria(EdSheeranViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.familyName = other.optionalFamilyName().map(StringFilter::copy).orElse(null);
        this.givenName = other.optionalGivenName().map(StringFilter::copy).orElse(null);
        this.display = other.optionalDisplay().map(StringFilter::copy).orElse(null);
        this.dob = other.optionalDob().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(TyrantSexFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.contactsJason = other.optionalContactsJason().map(StringFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.agencyId = other.optionalAgencyId().map(UUIDFilter::copy).orElse(null);
        this.avatarId = other.optionalAvatarId().map(LongFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.agentRolesId = other.optionalAgentRolesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EdSheeranViCriteria copy() {
        return new EdSheeranViCriteria(this);
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

    public StringFilter getFamilyName() {
        return familyName;
    }

    public Optional<StringFilter> optionalFamilyName() {
        return Optional.ofNullable(familyName);
    }

    public StringFilter familyName() {
        if (familyName == null) {
            setFamilyName(new StringFilter());
        }
        return familyName;
    }

    public void setFamilyName(StringFilter familyName) {
        this.familyName = familyName;
    }

    public StringFilter getGivenName() {
        return givenName;
    }

    public Optional<StringFilter> optionalGivenName() {
        return Optional.ofNullable(givenName);
    }

    public StringFilter givenName() {
        if (givenName == null) {
            setGivenName(new StringFilter());
        }
        return givenName;
    }

    public void setGivenName(StringFilter givenName) {
        this.givenName = givenName;
    }

    public StringFilter getDisplay() {
        return display;
    }

    public Optional<StringFilter> optionalDisplay() {
        return Optional.ofNullable(display);
    }

    public StringFilter display() {
        if (display == null) {
            setDisplay(new StringFilter());
        }
        return display;
    }

    public void setDisplay(StringFilter display) {
        this.display = display;
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

    public StringFilter getContactsJason() {
        return contactsJason;
    }

    public Optional<StringFilter> optionalContactsJason() {
        return Optional.ofNullable(contactsJason);
    }

    public StringFilter contactsJason() {
        if (contactsJason == null) {
            setContactsJason(new StringFilter());
        }
        return contactsJason;
    }

    public void setContactsJason(StringFilter contactsJason) {
        this.contactsJason = contactsJason;
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

    public UUIDFilter getAgencyId() {
        return agencyId;
    }

    public Optional<UUIDFilter> optionalAgencyId() {
        return Optional.ofNullable(agencyId);
    }

    public UUIDFilter agencyId() {
        if (agencyId == null) {
            setAgencyId(new UUIDFilter());
        }
        return agencyId;
    }

    public void setAgencyId(UUIDFilter agencyId) {
        this.agencyId = agencyId;
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

    public UUIDFilter getAppUserId() {
        return appUserId;
    }

    public Optional<UUIDFilter> optionalAppUserId() {
        return Optional.ofNullable(appUserId);
    }

    public UUIDFilter appUserId() {
        if (appUserId == null) {
            setAppUserId(new UUIDFilter());
        }
        return appUserId;
    }

    public void setAppUserId(UUIDFilter appUserId) {
        this.appUserId = appUserId;
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

    public LongFilter getAgentRolesId() {
        return agentRolesId;
    }

    public Optional<LongFilter> optionalAgentRolesId() {
        return Optional.ofNullable(agentRolesId);
    }

    public LongFilter agentRolesId() {
        if (agentRolesId == null) {
            setAgentRolesId(new LongFilter());
        }
        return agentRolesId;
    }

    public void setAgentRolesId(LongFilter agentRolesId) {
        this.agentRolesId = agentRolesId;
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
        final EdSheeranViCriteria that = (EdSheeranViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(familyName, that.familyName) &&
            Objects.equals(givenName, that.givenName) &&
            Objects.equals(display, that.display) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(contactsJason, that.contactsJason) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(agencyId, that.agencyId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(agentRolesId, that.agentRolesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            familyName,
            givenName,
            display,
            dob,
            gender,
            phone,
            contactsJason,
            isEnabled,
            agencyId,
            avatarId,
            internalUserId,
            appUserId,
            applicationId,
            agentRolesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EdSheeranViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFamilyName().map(f -> "familyName=" + f + ", ").orElse("") +
            optionalGivenName().map(f -> "givenName=" + f + ", ").orElse("") +
            optionalDisplay().map(f -> "display=" + f + ", ").orElse("") +
            optionalDob().map(f -> "dob=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalContactsJason().map(f -> "contactsJason=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalAgencyId().map(f -> "agencyId=" + f + ", ").orElse("") +
            optionalAvatarId().map(f -> "avatarId=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalAgentRolesId().map(f -> "agentRolesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
