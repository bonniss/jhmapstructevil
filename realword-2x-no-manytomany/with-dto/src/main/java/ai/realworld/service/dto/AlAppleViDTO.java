package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlAppleVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAppleViDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    private String hotline;

    private String taxCode;

    @Size(max = 10485760)
    private String contactsJason;

    @Size(max = 10485760)
    private String extensionJason;

    private Boolean isEnabled;

    private AndreiRightHandViDTO address;

    @NotNull
    private AlAlexTypeViDTO agencyType;

    private MetaverseDTO logo;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getContactsJason() {
        return contactsJason;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public String getExtensionJason() {
        return extensionJason;
    }

    public void setExtensionJason(String extensionJason) {
        this.extensionJason = extensionJason;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AndreiRightHandViDTO getAddress() {
        return address;
    }

    public void setAddress(AndreiRightHandViDTO address) {
        this.address = address;
    }

    public AlAlexTypeViDTO getAgencyType() {
        return agencyType;
    }

    public void setAgencyType(AlAlexTypeViDTO agencyType) {
        this.agencyType = agencyType;
    }

    public MetaverseDTO getLogo() {
        return logo;
    }

    public void setLogo(MetaverseDTO logo) {
        this.logo = logo;
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
        if (!(o instanceof AlAppleViDTO)) {
            return false;
        }

        AlAppleViDTO alAppleViDTO = (AlAppleViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alAppleViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAppleViDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hotline='" + getHotline() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", contactsJason='" + getContactsJason() + "'" +
            ", extensionJason='" + getExtensionJason() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", address=" + getAddress() +
            ", agencyType=" + getAgencyType() +
            ", logo=" + getLogo() +
            ", application=" + getApplication() +
            "}";
    }
}
