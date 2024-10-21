package ai.realworld.domain;

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
 * A AlAppleVi.
 */
@Entity
@Table(name = "al_apple_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAppleVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

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
    private AndreiRightHandVi address;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "asSuppliers", "asCustomers", "agencies" }, allowSetters = true)
    private AlAlexTypeVi agencyType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Metaverse logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agency", "avatar", "internalUser", "appUser", "application", "agentRoles" }, allowSetters = true)
    private Set<EdSheeranVi> agents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlAppleVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlAppleVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlAppleVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHotline() {
        return this.hotline;
    }

    public AlAppleVi hotline(String hotline) {
        this.setHotline(hotline);
        return this;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getTaxCode() {
        return this.taxCode;
    }

    public AlAppleVi taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getContactsJason() {
        return this.contactsJason;
    }

    public AlAppleVi contactsJason(String contactsJason) {
        this.setContactsJason(contactsJason);
        return this;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public String getExtensionJason() {
        return this.extensionJason;
    }

    public AlAppleVi extensionJason(String extensionJason) {
        this.setExtensionJason(extensionJason);
        return this;
    }

    public void setExtensionJason(String extensionJason) {
        this.extensionJason = extensionJason;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlAppleVi isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AndreiRightHandVi getAddress() {
        return this.address;
    }

    public void setAddress(AndreiRightHandVi andreiRightHandVi) {
        this.address = andreiRightHandVi;
    }

    public AlAppleVi address(AndreiRightHandVi andreiRightHandVi) {
        this.setAddress(andreiRightHandVi);
        return this;
    }

    public AlAlexTypeVi getAgencyType() {
        return this.agencyType;
    }

    public void setAgencyType(AlAlexTypeVi alAlexTypeVi) {
        this.agencyType = alAlexTypeVi;
    }

    public AlAppleVi agencyType(AlAlexTypeVi alAlexTypeVi) {
        this.setAgencyType(alAlexTypeVi);
        return this;
    }

    public Metaverse getLogo() {
        return this.logo;
    }

    public void setLogo(Metaverse metaverse) {
        this.logo = metaverse;
    }

    public AlAppleVi logo(Metaverse metaverse) {
        this.setLogo(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlAppleVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<EdSheeranVi> getAgents() {
        return this.agents;
    }

    public void setAgents(Set<EdSheeranVi> edSheeranVis) {
        if (this.agents != null) {
            this.agents.forEach(i -> i.setAgency(null));
        }
        if (edSheeranVis != null) {
            edSheeranVis.forEach(i -> i.setAgency(this));
        }
        this.agents = edSheeranVis;
    }

    public AlAppleVi agents(Set<EdSheeranVi> edSheeranVis) {
        this.setAgents(edSheeranVis);
        return this;
    }

    public AlAppleVi addAgents(EdSheeranVi edSheeranVi) {
        this.agents.add(edSheeranVi);
        edSheeranVi.setAgency(this);
        return this;
    }

    public AlAppleVi removeAgents(EdSheeranVi edSheeranVi) {
        this.agents.remove(edSheeranVi);
        edSheeranVi.setAgency(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlAppleVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlAppleVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAppleVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hotline='" + getHotline() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", contactsJason='" + getContactsJason() + "'" +
            ", extensionJason='" + getExtensionJason() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
