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
 * A AlProtyVi.
 */
@Entity
@Table(name = "al_proty_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProtyVi implements Serializable {

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
    @JsonIgnoreProperties(value = { "parent", "operator", "propertyProfile", "avatar", "application", "children" }, allowSetters = true)
    private AlProtyVi parent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "agencyType", "logo", "application", "agents" }, allowSetters = true)
    private AlAppleVi operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "children" }, allowSetters = true)
    private AlProProVi propertyProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "operator", "propertyProfile", "avatar", "application", "children" }, allowSetters = true)
    private Set<AlProtyVi> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlProtyVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlProtyVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeitiga() {
        return this.descriptionHeitiga;
    }

    public AlProtyVi descriptionHeitiga(String descriptionHeitiga) {
        this.setDescriptionHeitiga(descriptionHeitiga);
        return this;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public String getCoordinate() {
        return this.coordinate;
    }

    public AlProtyVi coordinate(String coordinate) {
        this.setCoordinate(coordinate);
        return this;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCode() {
        return this.code;
    }

    public AlProtyVi code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PeteStatus getStatus() {
        return this.status;
    }

    public AlProtyVi status(PeteStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PeteStatus status) {
        this.status = status;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlProtyVi isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProtyVi getParent() {
        return this.parent;
    }

    public void setParent(AlProtyVi alProtyVi) {
        this.parent = alProtyVi;
    }

    public AlProtyVi parent(AlProtyVi alProtyVi) {
        this.setParent(alProtyVi);
        return this;
    }

    public AlAppleVi getOperator() {
        return this.operator;
    }

    public void setOperator(AlAppleVi alAppleVi) {
        this.operator = alAppleVi;
    }

    public AlProtyVi operator(AlAppleVi alAppleVi) {
        this.setOperator(alAppleVi);
        return this;
    }

    public AlProProVi getPropertyProfile() {
        return this.propertyProfile;
    }

    public void setPropertyProfile(AlProProVi alProProVi) {
        this.propertyProfile = alProProVi;
    }

    public AlProtyVi propertyProfile(AlProProVi alProProVi) {
        this.setPropertyProfile(alProProVi);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlProtyVi avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlProtyVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlProtyVi> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlProtyVi> alProtyVis) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alProtyVis != null) {
            alProtyVis.forEach(i -> i.setParent(this));
        }
        this.children = alProtyVis;
    }

    public AlProtyVi children(Set<AlProtyVi> alProtyVis) {
        this.setChildren(alProtyVis);
        return this;
    }

    public AlProtyVi addChildren(AlProtyVi alProtyVi) {
        this.children.add(alProtyVi);
        alProtyVi.setParent(this);
        return this;
    }

    public AlProtyVi removeChildren(AlProtyVi alProtyVi) {
        this.children.remove(alProtyVi);
        alProtyVi.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlProtyVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlProtyVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProtyVi{" +
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
