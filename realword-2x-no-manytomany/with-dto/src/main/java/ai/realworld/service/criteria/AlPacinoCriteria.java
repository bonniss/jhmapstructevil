package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AcquisitionCanal;
import ai.realworld.domain.enumeration.TyrantSex;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPacino} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPacinoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pacinos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering AcquisitionCanal
     */
    public static class AcquisitionCanalFilter extends Filter<AcquisitionCanal> {

        public AcquisitionCanalFilter() {}

        public AcquisitionCanalFilter(AcquisitionCanalFilter filter) {
            super(filter);
        }

        @Override
        public AcquisitionCanalFilter copy() {
            return new AcquisitionCanalFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter platformCode;

    private StringFilter platformUsername;

    private StringFilter platformAvatarUrl;

    private BooleanFilter isSensitive;

    private StringFilter familyName;

    private StringFilter givenName;

    private LocalDateFilter dob;

    private TyrantSexFilter gender;

    private StringFilter phone;

    private StringFilter email;

    private AcquisitionCanalFilter acquiredFrom;

    private IntegerFilter currentPoints;

    private IntegerFilter totalPoints;

    private BooleanFilter isFollowing;

    private BooleanFilter isEnabled;

    private UUIDFilter applicationId;

    private LongFilter membershipTierId;

    private UUIDFilter alVueVueUsageId;

    private LongFilter membershipTierViId;

    private UUIDFilter alVueVueViUsageId;

    private Boolean distinct;

    public AlPacinoCriteria() {}

    public AlPacinoCriteria(AlPacinoCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.platformCode = other.optionalPlatformCode().map(StringFilter::copy).orElse(null);
        this.platformUsername = other.optionalPlatformUsername().map(StringFilter::copy).orElse(null);
        this.platformAvatarUrl = other.optionalPlatformAvatarUrl().map(StringFilter::copy).orElse(null);
        this.isSensitive = other.optionalIsSensitive().map(BooleanFilter::copy).orElse(null);
        this.familyName = other.optionalFamilyName().map(StringFilter::copy).orElse(null);
        this.givenName = other.optionalGivenName().map(StringFilter::copy).orElse(null);
        this.dob = other.optionalDob().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(TyrantSexFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.acquiredFrom = other.optionalAcquiredFrom().map(AcquisitionCanalFilter::copy).orElse(null);
        this.currentPoints = other.optionalCurrentPoints().map(IntegerFilter::copy).orElse(null);
        this.totalPoints = other.optionalTotalPoints().map(IntegerFilter::copy).orElse(null);
        this.isFollowing = other.optionalIsFollowing().map(BooleanFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.membershipTierId = other.optionalMembershipTierId().map(LongFilter::copy).orElse(null);
        this.alVueVueUsageId = other.optionalAlVueVueUsageId().map(UUIDFilter::copy).orElse(null);
        this.membershipTierViId = other.optionalMembershipTierViId().map(LongFilter::copy).orElse(null);
        this.alVueVueViUsageId = other.optionalAlVueVueViUsageId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPacinoCriteria copy() {
        return new AlPacinoCriteria(this);
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

    public StringFilter getPlatformCode() {
        return platformCode;
    }

    public Optional<StringFilter> optionalPlatformCode() {
        return Optional.ofNullable(platformCode);
    }

    public StringFilter platformCode() {
        if (platformCode == null) {
            setPlatformCode(new StringFilter());
        }
        return platformCode;
    }

    public void setPlatformCode(StringFilter platformCode) {
        this.platformCode = platformCode;
    }

    public StringFilter getPlatformUsername() {
        return platformUsername;
    }

    public Optional<StringFilter> optionalPlatformUsername() {
        return Optional.ofNullable(platformUsername);
    }

    public StringFilter platformUsername() {
        if (platformUsername == null) {
            setPlatformUsername(new StringFilter());
        }
        return platformUsername;
    }

    public void setPlatformUsername(StringFilter platformUsername) {
        this.platformUsername = platformUsername;
    }

    public StringFilter getPlatformAvatarUrl() {
        return platformAvatarUrl;
    }

    public Optional<StringFilter> optionalPlatformAvatarUrl() {
        return Optional.ofNullable(platformAvatarUrl);
    }

    public StringFilter platformAvatarUrl() {
        if (platformAvatarUrl == null) {
            setPlatformAvatarUrl(new StringFilter());
        }
        return platformAvatarUrl;
    }

    public void setPlatformAvatarUrl(StringFilter platformAvatarUrl) {
        this.platformAvatarUrl = platformAvatarUrl;
    }

    public BooleanFilter getIsSensitive() {
        return isSensitive;
    }

    public Optional<BooleanFilter> optionalIsSensitive() {
        return Optional.ofNullable(isSensitive);
    }

    public BooleanFilter isSensitive() {
        if (isSensitive == null) {
            setIsSensitive(new BooleanFilter());
        }
        return isSensitive;
    }

    public void setIsSensitive(BooleanFilter isSensitive) {
        this.isSensitive = isSensitive;
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public AcquisitionCanalFilter getAcquiredFrom() {
        return acquiredFrom;
    }

    public Optional<AcquisitionCanalFilter> optionalAcquiredFrom() {
        return Optional.ofNullable(acquiredFrom);
    }

    public AcquisitionCanalFilter acquiredFrom() {
        if (acquiredFrom == null) {
            setAcquiredFrom(new AcquisitionCanalFilter());
        }
        return acquiredFrom;
    }

    public void setAcquiredFrom(AcquisitionCanalFilter acquiredFrom) {
        this.acquiredFrom = acquiredFrom;
    }

    public IntegerFilter getCurrentPoints() {
        return currentPoints;
    }

    public Optional<IntegerFilter> optionalCurrentPoints() {
        return Optional.ofNullable(currentPoints);
    }

    public IntegerFilter currentPoints() {
        if (currentPoints == null) {
            setCurrentPoints(new IntegerFilter());
        }
        return currentPoints;
    }

    public void setCurrentPoints(IntegerFilter currentPoints) {
        this.currentPoints = currentPoints;
    }

    public IntegerFilter getTotalPoints() {
        return totalPoints;
    }

    public Optional<IntegerFilter> optionalTotalPoints() {
        return Optional.ofNullable(totalPoints);
    }

    public IntegerFilter totalPoints() {
        if (totalPoints == null) {
            setTotalPoints(new IntegerFilter());
        }
        return totalPoints;
    }

    public void setTotalPoints(IntegerFilter totalPoints) {
        this.totalPoints = totalPoints;
    }

    public BooleanFilter getIsFollowing() {
        return isFollowing;
    }

    public Optional<BooleanFilter> optionalIsFollowing() {
        return Optional.ofNullable(isFollowing);
    }

    public BooleanFilter isFollowing() {
        if (isFollowing == null) {
            setIsFollowing(new BooleanFilter());
        }
        return isFollowing;
    }

    public void setIsFollowing(BooleanFilter isFollowing) {
        this.isFollowing = isFollowing;
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

    public LongFilter getMembershipTierId() {
        return membershipTierId;
    }

    public Optional<LongFilter> optionalMembershipTierId() {
        return Optional.ofNullable(membershipTierId);
    }

    public LongFilter membershipTierId() {
        if (membershipTierId == null) {
            setMembershipTierId(new LongFilter());
        }
        return membershipTierId;
    }

    public void setMembershipTierId(LongFilter membershipTierId) {
        this.membershipTierId = membershipTierId;
    }

    public UUIDFilter getAlVueVueUsageId() {
        return alVueVueUsageId;
    }

    public Optional<UUIDFilter> optionalAlVueVueUsageId() {
        return Optional.ofNullable(alVueVueUsageId);
    }

    public UUIDFilter alVueVueUsageId() {
        if (alVueVueUsageId == null) {
            setAlVueVueUsageId(new UUIDFilter());
        }
        return alVueVueUsageId;
    }

    public void setAlVueVueUsageId(UUIDFilter alVueVueUsageId) {
        this.alVueVueUsageId = alVueVueUsageId;
    }

    public LongFilter getMembershipTierViId() {
        return membershipTierViId;
    }

    public Optional<LongFilter> optionalMembershipTierViId() {
        return Optional.ofNullable(membershipTierViId);
    }

    public LongFilter membershipTierViId() {
        if (membershipTierViId == null) {
            setMembershipTierViId(new LongFilter());
        }
        return membershipTierViId;
    }

    public void setMembershipTierViId(LongFilter membershipTierViId) {
        this.membershipTierViId = membershipTierViId;
    }

    public UUIDFilter getAlVueVueViUsageId() {
        return alVueVueViUsageId;
    }

    public Optional<UUIDFilter> optionalAlVueVueViUsageId() {
        return Optional.ofNullable(alVueVueViUsageId);
    }

    public UUIDFilter alVueVueViUsageId() {
        if (alVueVueViUsageId == null) {
            setAlVueVueViUsageId(new UUIDFilter());
        }
        return alVueVueViUsageId;
    }

    public void setAlVueVueViUsageId(UUIDFilter alVueVueViUsageId) {
        this.alVueVueViUsageId = alVueVueViUsageId;
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
        final AlPacinoCriteria that = (AlPacinoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(platformCode, that.platformCode) &&
            Objects.equals(platformUsername, that.platformUsername) &&
            Objects.equals(platformAvatarUrl, that.platformAvatarUrl) &&
            Objects.equals(isSensitive, that.isSensitive) &&
            Objects.equals(familyName, that.familyName) &&
            Objects.equals(givenName, that.givenName) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(acquiredFrom, that.acquiredFrom) &&
            Objects.equals(currentPoints, that.currentPoints) &&
            Objects.equals(totalPoints, that.totalPoints) &&
            Objects.equals(isFollowing, that.isFollowing) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(membershipTierId, that.membershipTierId) &&
            Objects.equals(alVueVueUsageId, that.alVueVueUsageId) &&
            Objects.equals(membershipTierViId, that.membershipTierViId) &&
            Objects.equals(alVueVueViUsageId, that.alVueVueViUsageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            platformCode,
            platformUsername,
            platformAvatarUrl,
            isSensitive,
            familyName,
            givenName,
            dob,
            gender,
            phone,
            email,
            acquiredFrom,
            currentPoints,
            totalPoints,
            isFollowing,
            isEnabled,
            applicationId,
            membershipTierId,
            alVueVueUsageId,
            membershipTierViId,
            alVueVueViUsageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPlatformCode().map(f -> "platformCode=" + f + ", ").orElse("") +
            optionalPlatformUsername().map(f -> "platformUsername=" + f + ", ").orElse("") +
            optionalPlatformAvatarUrl().map(f -> "platformAvatarUrl=" + f + ", ").orElse("") +
            optionalIsSensitive().map(f -> "isSensitive=" + f + ", ").orElse("") +
            optionalFamilyName().map(f -> "familyName=" + f + ", ").orElse("") +
            optionalGivenName().map(f -> "givenName=" + f + ", ").orElse("") +
            optionalDob().map(f -> "dob=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalAcquiredFrom().map(f -> "acquiredFrom=" + f + ", ").orElse("") +
            optionalCurrentPoints().map(f -> "currentPoints=" + f + ", ").orElse("") +
            optionalTotalPoints().map(f -> "totalPoints=" + f + ", ").orElse("") +
            optionalIsFollowing().map(f -> "isFollowing=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalMembershipTierId().map(f -> "membershipTierId=" + f + ", ").orElse("") +
            optionalAlVueVueUsageId().map(f -> "alVueVueUsageId=" + f + ", ").orElse("") +
            optionalMembershipTierViId().map(f -> "membershipTierViId=" + f + ", ").orElse("") +
            optionalAlVueVueViUsageId().map(f -> "alVueVueViUsageId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
