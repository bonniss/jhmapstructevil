package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.OlBakeryType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.OlMaster} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlMasterDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    private String slug;

    @Size(max = 10485760)
    private String descriptionHeitiga;

    private OlBakeryType businessType;

    private String email;

    private String hotline;

    private String taxCode;

    @Size(max = 10485760)
    private String contactsJason;

    @Size(max = 10485760)
    private String extensionJason;

    private Boolean isEnabled;

    private AndreiRightHandDTO address;

    private AndreiRightHandViDTO addressVi;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescriptionHeitiga() {
        return descriptionHeitiga;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public OlBakeryType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(OlBakeryType businessType) {
        this.businessType = businessType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AndreiRightHandDTO getAddress() {
        return address;
    }

    public void setAddress(AndreiRightHandDTO address) {
        this.address = address;
    }

    public AndreiRightHandViDTO getAddressVi() {
        return addressVi;
    }

    public void setAddressVi(AndreiRightHandViDTO addressVi) {
        this.addressVi = addressVi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OlMasterDTO)) {
            return false;
        }

        OlMasterDTO olMasterDTO = (OlMasterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, olMasterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlMasterDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", businessType='" + getBusinessType() + "'" +
            ", email='" + getEmail() + "'" +
            ", hotline='" + getHotline() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", contactsJason='" + getContactsJason() + "'" +
            ", extensionJason='" + getExtensionJason() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", address=" + getAddress() +
            ", addressVi=" + getAddressVi() +
            "}";
    }
}
