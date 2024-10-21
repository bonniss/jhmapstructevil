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
 * A EdSheeran.
 */
@Entity
@Table(name = "ed_sheeran")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EdSheeran implements Serializable {

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
    private AlApple agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private AlPacino appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agent", "role", "application" }, allowSetters = true)
    private Set<HandCraft> agentRoles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EdSheeran id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public EdSheeran familyName(String familyName) {
        this.setFamilyName(familyName);
        return this;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public EdSheeran givenName(String givenName) {
        this.setGivenName(givenName);
        return this;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDisplay() {
        return this.display;
    }

    public EdSheeran display(String display) {
        this.setDisplay(display);
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public EdSheeran dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return this.gender;
    }

    public EdSheeran gender(TyrantSex gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public EdSheeran phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactsJason() {
        return this.contactsJason;
    }

    public EdSheeran contactsJason(String contactsJason) {
        this.setContactsJason(contactsJason);
        return this;
    }

    public void setContactsJason(String contactsJason) {
        this.contactsJason = contactsJason;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public EdSheeran isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlApple getAgency() {
        return this.agency;
    }

    public void setAgency(AlApple alApple) {
        this.agency = alApple;
    }

    public EdSheeran agency(AlApple alApple) {
        this.setAgency(alApple);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public EdSheeran avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public EdSheeran internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public AlPacino getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AlPacino alPacino) {
        this.appUser = alPacino;
    }

    public EdSheeran appUser(AlPacino alPacino) {
        this.setAppUser(alPacino);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public EdSheeran application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<HandCraft> getAgentRoles() {
        return this.agentRoles;
    }

    public void setAgentRoles(Set<HandCraft> handCrafts) {
        if (this.agentRoles != null) {
            this.agentRoles.forEach(i -> i.setAgent(null));
        }
        if (handCrafts != null) {
            handCrafts.forEach(i -> i.setAgent(this));
        }
        this.agentRoles = handCrafts;
    }

    public EdSheeran agentRoles(Set<HandCraft> handCrafts) {
        this.setAgentRoles(handCrafts);
        return this;
    }

    public EdSheeran addAgentRoles(HandCraft handCraft) {
        this.agentRoles.add(handCraft);
        handCraft.setAgent(this);
        return this;
    }

    public EdSheeran removeAgentRoles(HandCraft handCraft) {
        this.agentRoles.remove(handCraft);
        handCraft.setAgent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdSheeran)) {
            return false;
        }
        return getId() != null && getId().equals(((EdSheeran) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EdSheeran{" +
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
