package ai.realworld.domain;

import ai.realworld.domain.enumeration.TyrantSex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EdSheeranVi.
 */
@Entity
@Table(name = "ed_sheeran_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EdSheeranVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "family_name", length = 256, nullable = false)
    private String familyName;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "given_name", length = 256, nullable = false)
    private String givenName;

    @Size(min = 2, max = 256)
    @Column(name = "display", length = 256)
    private String display;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private TyrantSex gender;

    @Column(name = "phone")
    private String phone;

    @Size(max = 10485760)
    @Column(name = "contacts_jason", length = 10485760)
    private String contactsJason;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "agencyType", "logo", "application", "agents" }, allowSetters = true)
    private AlAppleVi agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "application", "membershipTier", "alVueVueUsage", "membershipTierVi", "alVueVueViUsage" },
        allowSetters = true
    )
    private AlPacino appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agent", "role", "application" }, allowSetters = true)
    private Set<HandCraftVi> agentRoles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EdSheeranVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public EdSheeranVi familyName(String familyName) {
        this.setFamilyName(familyName);
        return this;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public EdSheeranVi givenName(String givenName) {
        this.setGivenName(givenName);
        return this;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDisplay() {
        return this.display;
    }

    public EdSheeranVi display(String display) {
        this.setDisplay(display);
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public EdSheeranVi dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return this.gender;
    }

    public EdSheeranVi gender(TyrantSex gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public EdSheeranVi phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactsJason() {
        return this.contactsJason;
    }

    public EdSheeranVi contactsJason(String contactsJason) {
        this.setContactsJason(contactsJason);
        return this;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public EdSheeranVi isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlAppleVi getAgency() {
        return this.agency;
    }

    public void setAgency(AlAppleVi alAppleVi) {
        this.agency = alAppleVi;
    }

    public EdSheeranVi agency(AlAppleVi alAppleVi) {
        this.setAgency(alAppleVi);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public EdSheeranVi avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public EdSheeranVi internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public AlPacino getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AlPacino alPacino) {
        this.appUser = alPacino;
    }

    public EdSheeranVi appUser(AlPacino alPacino) {
        this.setAppUser(alPacino);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public EdSheeranVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<HandCraftVi> getAgentRoles() {
        return this.agentRoles;
    }

    public void setAgentRoles(Set<HandCraftVi> handCraftVis) {
        if (this.agentRoles != null) {
            this.agentRoles.forEach(i -> i.setAgent(null));
        }
        if (handCraftVis != null) {
            handCraftVis.forEach(i -> i.setAgent(this));
        }
        this.agentRoles = handCraftVis;
    }

    public EdSheeranVi agentRoles(Set<HandCraftVi> handCraftVis) {
        this.setAgentRoles(handCraftVis);
        return this;
    }

    public EdSheeranVi addAgentRoles(HandCraftVi handCraftVi) {
        this.agentRoles.add(handCraftVi);
        handCraftVi.setAgent(this);
        return this;
    }

    public EdSheeranVi removeAgentRoles(HandCraftVi handCraftVi) {
        this.agentRoles.remove(handCraftVi);
        handCraftVi.setAgent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdSheeranVi)) {
            return false;
        }
        return getId() != null && getId().equals(((EdSheeranVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EdSheeranVi{" +
            "id=" + getId() +
            ", familyName='" + getFamilyName() + "'" +
            ", givenName='" + getGivenName() + "'" +
            ", display='" + getDisplay() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contactsJason='" + getContactsJason() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
