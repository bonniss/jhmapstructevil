package ai.realworld.domain;

import ai.realworld.domain.enumeration.AcquisitionCanal;
import ai.realworld.domain.enumeration.TyrantSex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPacino.
 */
@Entity
@Table(name = "al_pacino")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacino implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Size(max = 1024)
    @Column(name = "platform_code", length = 1024)
    private String platformCode;

    @Size(min = 2, max = 256)
    @Column(name = "platform_username", length = 256)
    private String platformUsername;

    @Column(name = "platform_avatar_url")
    private String platformAvatarUrl;

    @Column(name = "is_sensitive")
    private Boolean isSensitive;

    @Size(min = 1, max = 256)
    @Column(name = "family_name", length = 256)
    private String familyName;

    @Size(min = 1, max = 256)
    @Column(name = "given_name", length = 256)
    private String givenName;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private TyrantSex gender;

    @Column(name = "phone")
    private String phone;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "acquired_from")
    private AcquisitionCanal acquiredFrom;

    @Column(name = "current_points")
    private Integer currentPoints;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "is_following")
    private Boolean isFollowing;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private AlMemTier membershipTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "vouchers", "customers" }, allowSetters = true)
    private AlVueVueUsage alVueVueUsage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private AlMemTierVi membershipTierVi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "vouchers", "customers" }, allowSetters = true)
    private AlVueVueViUsage alVueVueViUsage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlPacino id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return this.platformCode;
    }

    public AlPacino platformCode(String platformCode) {
        this.setPlatformCode(platformCode);
        return this;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformUsername() {
        return this.platformUsername;
    }

    public AlPacino platformUsername(String platformUsername) {
        this.setPlatformUsername(platformUsername);
        return this;
    }

    public void setPlatformUsername(String platformUsername) {
        this.platformUsername = platformUsername;
    }

    public String getPlatformAvatarUrl() {
        return this.platformAvatarUrl;
    }

    public AlPacino platformAvatarUrl(String platformAvatarUrl) {
        this.setPlatformAvatarUrl(platformAvatarUrl);
        return this;
    }

    public void setPlatformAvatarUrl(String platformAvatarUrl) {
        this.platformAvatarUrl = platformAvatarUrl;
    }

    public Boolean getIsSensitive() {
        return this.isSensitive;
    }

    public AlPacino isSensitive(Boolean isSensitive) {
        this.setIsSensitive(isSensitive);
        return this;
    }

    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public AlPacino familyName(String familyName) {
        this.setFamilyName(familyName);
        return this;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public AlPacino givenName(String givenName) {
        this.setGivenName(givenName);
        return this;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public AlPacino dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return this.gender;
    }

    public AlPacino gender(TyrantSex gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public AlPacino phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public AlPacino email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AcquisitionCanal getAcquiredFrom() {
        return this.acquiredFrom;
    }

    public AlPacino acquiredFrom(AcquisitionCanal acquiredFrom) {
        this.setAcquiredFrom(acquiredFrom);
        return this;
    }

    public void setAcquiredFrom(AcquisitionCanal acquiredFrom) {
        this.acquiredFrom = acquiredFrom;
    }

    public Integer getCurrentPoints() {
        return this.currentPoints;
    }

    public AlPacino currentPoints(Integer currentPoints) {
        this.setCurrentPoints(currentPoints);
        return this;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Integer getTotalPoints() {
        return this.totalPoints;
    }

    public AlPacino totalPoints(Integer totalPoints) {
        this.setTotalPoints(totalPoints);
        return this;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Boolean getIsFollowing() {
        return this.isFollowing;
    }

    public AlPacino isFollowing(Boolean isFollowing) {
        this.setIsFollowing(isFollowing);
        return this;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlPacino isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPacino application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public AlMemTier getMembershipTier() {
        return this.membershipTier;
    }

    public void setMembershipTier(AlMemTier alMemTier) {
        this.membershipTier = alMemTier;
    }

    public AlPacino membershipTier(AlMemTier alMemTier) {
        this.setMembershipTier(alMemTier);
        return this;
    }

    public AlVueVueUsage getAlVueVueUsage() {
        return this.alVueVueUsage;
    }

    public void setAlVueVueUsage(AlVueVueUsage alVueVueUsage) {
        this.alVueVueUsage = alVueVueUsage;
    }

    public AlPacino alVueVueUsage(AlVueVueUsage alVueVueUsage) {
        this.setAlVueVueUsage(alVueVueUsage);
        return this;
    }

    public AlMemTierVi getMembershipTierVi() {
        return this.membershipTierVi;
    }

    public void setMembershipTierVi(AlMemTierVi alMemTierVi) {
        this.membershipTierVi = alMemTierVi;
    }

    public AlPacino membershipTierVi(AlMemTierVi alMemTierVi) {
        this.setMembershipTierVi(alMemTierVi);
        return this;
    }

    public AlVueVueViUsage getAlVueVueViUsage() {
        return this.alVueVueViUsage;
    }

    public void setAlVueVueViUsage(AlVueVueViUsage alVueVueViUsage) {
        this.alVueVueViUsage = alVueVueViUsage;
    }

    public AlPacino alVueVueViUsage(AlVueVueViUsage alVueVueViUsage) {
        this.setAlVueVueViUsage(alVueVueViUsage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPacino)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPacino) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacino{" +
            "id=" + getId() +
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
            "}";
    }
}
