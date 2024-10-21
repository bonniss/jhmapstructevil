package ai.realworld.domain;

import ai.realworld.domain.enumeration.OlBakeryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OlMaster.
 */
@Entity
@Table(name = "ol_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "slug", length = 160, nullable = false, unique = true)
    private String slug;

    @Size(max = 10485760)
    @Column(name = "description_heitiga", length = 10485760)
    private String descriptionHeitiga;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type")
    private OlBakeryType businessType;

    @Column(name = "email")
    private String email;

    @Column(name = "hotline")
    private String hotline;

    @Column(name = "tax_code")
    private String taxCode;

    @Size(max = 10485760)
    @Column(name = "contacts_jason", length = 10485760)
    private String contactsJason;

    @Size(max = 10485760)
    @Column(name = "extension_jason", length = 10485760)
    private String extensionJason;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country", "province", "district", "ward" }, allowSetters = true)
    private AndreiRightHand address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country", "province", "district", "ward" }, allowSetters = true)
    private AndreiRightHandVi addressVi;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private Set<JohnLennon> applications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public OlMaster id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public OlMaster name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return this.slug;
    }

    public OlMaster slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescriptionHeitiga() {
        return this.descriptionHeitiga;
    }

    public OlMaster descriptionHeitiga(String descriptionHeitiga) {
        this.setDescriptionHeitiga(descriptionHeitiga);
        return this;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public OlBakeryType getBusinessType() {
        return this.businessType;
    }

    public OlMaster businessType(OlBakeryType businessType) {
        this.setBusinessType(businessType);
        return this;
    }

    public void setBusinessType(OlBakeryType businessType) {
        this.businessType = businessType;
    }

    public String getEmail() {
        return this.email;
    }

    public OlMaster email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHotline() {
        return this.hotline;
    }

    public OlMaster hotline(String hotline) {
        this.setHotline(hotline);
        return this;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getTaxCode() {
        return this.taxCode;
    }

    public OlMaster taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getContactsJason() {
        return this.contactsJason;
    }

    public OlMaster contactsJason(String contactsJason) {
        this.setContactsJason(contactsJason);
        return this;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public String getExtensionJason() {
        return this.extensionJason;
    }

    public OlMaster extensionJason(String extensionJason) {
        this.setExtensionJason(extensionJason);
        return this;
    }

    public void setExtensionJason(String extensionJason) {
        this.extensionJason = extensionJason;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public OlMaster isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AndreiRightHand getAddress() {
        return this.address;
    }

    public void setAddress(AndreiRightHand andreiRightHand) {
        this.address = andreiRightHand;
    }

    public OlMaster address(AndreiRightHand andreiRightHand) {
        this.setAddress(andreiRightHand);
        return this;
    }

    public AndreiRightHandVi getAddressVi() {
        return this.addressVi;
    }

    public void setAddressVi(AndreiRightHandVi andreiRightHandVi) {
        this.addressVi = andreiRightHandVi;
    }

    public OlMaster addressVi(AndreiRightHandVi andreiRightHandVi) {
        this.setAddressVi(andreiRightHandVi);
        return this;
    }

    public Set<JohnLennon> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<JohnLennon> johnLennons) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.setOrganization(null));
        }
        if (johnLennons != null) {
            johnLennons.forEach(i -> i.setOrganization(this));
        }
        this.applications = johnLennons;
    }

    public OlMaster applications(Set<JohnLennon> johnLennons) {
        this.setApplications(johnLennons);
        return this;
    }

    public OlMaster addApplications(JohnLennon johnLennon) {
        this.applications.add(johnLennon);
        johnLennon.setOrganization(this);
        return this;
    }

    public OlMaster removeApplications(JohnLennon johnLennon) {
        this.applications.remove(johnLennon);
        johnLennon.setOrganization(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OlMaster)) {
            return false;
        }
        return getId() != null && getId().equals(((OlMaster) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlMaster{" +
            "id=" + getId() +
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
            "}";
    }
}
