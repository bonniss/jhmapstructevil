package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AcquisitionCanal;
import ai.realworld.domain.enumeration.TyrantSex;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlPacino} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoDTO implements Serializable {

    private UUID id;

    @Size(max = 1024)
    private String platformCode;

    @Size(min = 2, max = 256)
    private String platformUsername;

    private String platformAvatarUrl;

    private Boolean isSensitive;

    @Size(min = 1, max = 256)
    private String familyName;

    @Size(min = 1, max = 256)
    private String givenName;

    private LocalDate dob;

    private TyrantSex gender;

    private String phone;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private AcquisitionCanal acquiredFrom;

    private Integer currentPoints;

    private Integer totalPoints;

    private Boolean isFollowing;

    private Boolean isEnabled;

    private JohnLennonDTO application;

    private AlMemTierDTO membershipTier;

    private AlVueVueUsageDTO alVueVueUsage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformUsername() {
        return platformUsername;
    }

    public void setPlatformUsername(String platformUsername) {
        this.platformUsername = platformUsername;
    }

    public String getPlatformAvatarUrl() {
        return platformAvatarUrl;
    }

    public void setPlatformAvatarUrl(String platformAvatarUrl) {
        this.platformAvatarUrl = platformAvatarUrl;
    }

    public Boolean getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return gender;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AcquisitionCanal getAcquiredFrom() {
        return acquiredFrom;
    }

    public void setAcquiredFrom(AcquisitionCanal acquiredFrom) {
        this.acquiredFrom = acquiredFrom;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    public AlMemTierDTO getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(AlMemTierDTO membershipTier) {
        this.membershipTier = membershipTier;
    }

    public AlVueVueUsageDTO getAlVueVueUsage() {
        return alVueVueUsage;
    }

    public void setAlVueVueUsage(AlVueVueUsageDTO alVueVueUsage) {
        this.alVueVueUsage = alVueVueUsage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacinoDTO)) {
            return false;
        }

        AlPacinoDTO alPacinoDTO = (AlPacinoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPacinoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoDTO{" +
            "id='" + getId() + "'" +
            ", platformCode='" + getPlatformCode() + "'" +
            ", platformUsername='" + getPlatformUsername() + "'" +
            ", platformAvatarUrl='" + getPlatformAvatarUrl() + "'" +
            ", isSensitive='" + getIsSensitive() + "'" +
            ", familyName='" + getFamilyName() + "'" +
            ", givenName='" + getGivenName() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", acquiredFrom='" + getAcquiredFrom() + "'" +
            ", currentPoints=" + getCurrentPoints() +
            ", totalPoints=" + getTotalPoints() +
            ", isFollowing='" + getIsFollowing() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", application=" + getApplication() +
            ", membershipTier=" + getMembershipTier() +
            ", alVueVueUsage=" + getAlVueVueUsage() +
            "}";
    }
}
