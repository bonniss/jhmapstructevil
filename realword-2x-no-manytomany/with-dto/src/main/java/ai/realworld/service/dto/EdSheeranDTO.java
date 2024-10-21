package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.TyrantSex;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.EdSheeran} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EdSheeranDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    private String familyName;

    @NotNull
    @Size(min = 1, max = 256)
    private String givenName;

    @Size(min = 2, max = 256)
    private String display;

    private LocalDate dob;

    private TyrantSex gender;

    private String phone;

    @Size(max = 10485760)
    private String contactsJason;

    private Boolean isEnabled;

    @NotNull
    private AlAppleDTO agency;

    private MetaverseDTO avatar;

    private UserDTO internalUser;

    private AlPacinoDTO appUser;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
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

    public String getContactsJason() {
        return contactsJason;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlAppleDTO getAgency() {
        return agency;
    }

    public void setAgency(AlAppleDTO agency) {
        this.agency = agency;
    }

    public MetaverseDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(MetaverseDTO avatar) {
        this.avatar = avatar;
    }

    public UserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(UserDTO internalUser) {
        this.internalUser = internalUser;
    }

    public AlPacinoDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AlPacinoDTO appUser) {
        this.appUser = appUser;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdSheeranDTO)) {
            return false;
        }

        EdSheeranDTO edSheeranDTO = (EdSheeranDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, edSheeranDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EdSheeranDTO{" +
            "id=" + getId() +
            ", familyName='" + getFamilyName() + "'" +
            ", givenName='" + getGivenName() + "'" +
            ", display='" + getDisplay() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contactsJason='" + getContactsJason() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", agency=" + getAgency() +
            ", avatar=" + getAvatar() +
            ", internalUser=" + getInternalUser() +
            ", appUser=" + getAppUser() +
            ", application=" + getApplication() +
            "}";
    }
}
