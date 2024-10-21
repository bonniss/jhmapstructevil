package ai.realworld.domain;

import ai.realworld.domain.enumeration.PeteStatus;
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
 * A AlProty.
 */
@Entity
@Table(name = "al_proty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 10485760)
    @Column(name = "description_heitiga", length = 10485760)
    private String descriptionHeitiga;

    @Column(name = "coordinate")
    private String coordinate;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PeteStatus status;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "parent", "operator", "propertyProfile", "avatar", "application", "images", "children", "bookings" },
        allowSetters = true
    )
    private AlProty parent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "agencyType", "logo", "application", "agents" }, allowSetters = true)
    private AlApple operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "amenities", "images", "children" }, allowSetters = true)
    private AlProPro propertyProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_al_proty__image",
        joinColumns = @JoinColumn(name = "al_proty_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Set<Metaverse> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "operator", "propertyProfile", "avatar", "application", "images", "children", "bookings" },
        allowSetters = true
    )
    private Set<AlProty> children = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "properties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "personInCharge", "application", "properties" }, allowSetters = true)
    private Set<AlPyuJoker> bookings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlProty id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlProty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeitiga() {
        return this.descriptionHeitiga;
    }

    public AlProty descriptionHeitiga(String descriptionHeitiga) {
        this.setDescriptionHeitiga(descriptionHeitiga);
        return this;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public String getCoordinate() {
        return this.coordinate;
    }

    public AlProty coordinate(String coordinate) {
        this.setCoordinate(coordinate);
        return this;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCode() {
        return this.code;
    }

    public AlProty code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PeteStatus getStatus() {
        return this.status;
    }

    public AlProty status(PeteStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PeteStatus status) {
        this.status = status;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlProty isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProty getParent() {
        return this.parent;
    }

    public void setParent(AlProty alProty) {
        this.parent = alProty;
    }

    public AlProty parent(AlProty alProty) {
        this.setParent(alProty);
        return this;
    }

    public AlApple getOperator() {
        return this.operator;
    }

    public void setOperator(AlApple alApple) {
        this.operator = alApple;
    }

    public AlProty operator(AlApple alApple) {
        this.setOperator(alApple);
        return this;
    }

    public AlProPro getPropertyProfile() {
        return this.propertyProfile;
    }

    public void setPropertyProfile(AlProPro alProPro) {
        this.propertyProfile = alProPro;
    }

    public AlProty propertyProfile(AlProPro alProPro) {
        this.setPropertyProfile(alProPro);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlProty avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlProty application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<Metaverse> getImages() {
        return this.images;
    }

    public void setImages(Set<Metaverse> metaverses) {
        this.images = metaverses;
    }

    public AlProty images(Set<Metaverse> metaverses) {
        this.setImages(metaverses);
        return this;
    }

    public AlProty addImage(Metaverse metaverse) {
        this.images.add(metaverse);
        return this;
    }

    public AlProty removeImage(Metaverse metaverse) {
        this.images.remove(metaverse);
        return this;
    }

    public Set<AlProty> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlProty> alProties) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alProties != null) {
            alProties.forEach(i -> i.setParent(this));
        }
        this.children = alProties;
    }

    public AlProty children(Set<AlProty> alProties) {
        this.setChildren(alProties);
        return this;
    }

    public AlProty addChildren(AlProty alProty) {
        this.children.add(alProty);
        alProty.setParent(this);
        return this;
    }

    public AlProty removeChildren(AlProty alProty) {
        this.children.remove(alProty);
        alProty.setParent(null);
        return this;
    }

    public Set<AlPyuJoker> getBookings() {
        return this.bookings;
    }

    public void setBookings(Set<AlPyuJoker> alPyuJokers) {
        if (this.bookings != null) {
            this.bookings.forEach(i -> i.removeProperty(this));
        }
        if (alPyuJokers != null) {
            alPyuJokers.forEach(i -> i.addProperty(this));
        }
        this.bookings = alPyuJokers;
    }

    public AlProty bookings(Set<AlPyuJoker> alPyuJokers) {
        this.setBookings(alPyuJokers);
        return this;
    }

    public AlProty addBooking(AlPyuJoker alPyuJoker) {
        this.bookings.add(alPyuJoker);
        alPyuJoker.getProperties().add(this);
        return this;
    }

    public AlProty removeBooking(AlPyuJoker alPyuJoker) {
        this.bookings.remove(alPyuJoker);
        alPyuJoker.getProperties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlProty)) {
            return false;
        }
        return getId() != null && getId().equals(((AlProty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", coordinate='" + getCoordinate() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
