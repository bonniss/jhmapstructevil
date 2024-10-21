package ai.realworld.domain;

import ai.realworld.domain.enumeration.BenedictToy;
import ai.realworld.domain.enumeration.PeteType;
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
 * A AlProProVi.
 */
@Entity
@Table(name = "al_pro_pro_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProProVi implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PeteType propertyType;

    @Column(name = "area_in_square_meter")
    private Float areaInSquareMeter;

    @Min(value = 1)
    @Column(name = "number_of_adults")
    private Integer numberOfAdults;

    @Min(value = 0)
    @Column(name = "number_of_preschoolers")
    private Integer numberOfPreschoolers;

    @Min(value = 0)
    @Column(name = "number_of_children")
    private Integer numberOfChildren;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "number_of_floors")
    private Integer numberOfFloors;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_size")
    private BenedictToy bedSize;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "children" }, allowSetters = true)
    private AlProProVi parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "avatar", "application" }, allowSetters = true)
    private AlLadyGagaVi project;

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
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "children" }, allowSetters = true)
    private Set<AlProProVi> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlProProVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlProProVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeitiga() {
        return this.descriptionHeitiga;
    }

    public AlProProVi descriptionHeitiga(String descriptionHeitiga) {
        this.setDescriptionHeitiga(descriptionHeitiga);
        return this;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public PeteType getPropertyType() {
        return this.propertyType;
    }

    public AlProProVi propertyType(PeteType propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
    }

    public Float getAreaInSquareMeter() {
        return this.areaInSquareMeter;
    }

    public AlProProVi areaInSquareMeter(Float areaInSquareMeter) {
        this.setAreaInSquareMeter(areaInSquareMeter);
        return this;
    }

    public void setAreaInSquareMeter(Float areaInSquareMeter) {
        this.areaInSquareMeter = areaInSquareMeter;
    }

    public Integer getNumberOfAdults() {
        return this.numberOfAdults;
    }

    public AlProProVi numberOfAdults(Integer numberOfAdults) {
        this.setNumberOfAdults(numberOfAdults);
        return this;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfPreschoolers() {
        return this.numberOfPreschoolers;
    }

    public AlProProVi numberOfPreschoolers(Integer numberOfPreschoolers) {
        this.setNumberOfPreschoolers(numberOfPreschoolers);
        return this;
    }

    public void setNumberOfPreschoolers(Integer numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public Integer getNumberOfChildren() {
        return this.numberOfChildren;
    }

    public AlProProVi numberOfChildren(Integer numberOfChildren) {
        this.setNumberOfChildren(numberOfChildren);
        return this;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfRooms() {
        return this.numberOfRooms;
    }

    public AlProProVi numberOfRooms(Integer numberOfRooms) {
        this.setNumberOfRooms(numberOfRooms);
        return this;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfFloors() {
        return this.numberOfFloors;
    }

    public AlProProVi numberOfFloors(Integer numberOfFloors) {
        this.setNumberOfFloors(numberOfFloors);
        return this;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public BenedictToy getBedSize() {
        return this.bedSize;
    }

    public AlProProVi bedSize(BenedictToy bedSize) {
        this.setBedSize(bedSize);
        return this;
    }

    public void setBedSize(BenedictToy bedSize) {
        this.bedSize = bedSize;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlProProVi isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProProVi getParent() {
        return this.parent;
    }

    public void setParent(AlProProVi alProProVi) {
        this.parent = alProProVi;
    }

    public AlProProVi parent(AlProProVi alProProVi) {
        this.setParent(alProProVi);
        return this;
    }

    public AlLadyGagaVi getProject() {
        return this.project;
    }

    public void setProject(AlLadyGagaVi alLadyGagaVi) {
        this.project = alLadyGagaVi;
    }

    public AlProProVi project(AlLadyGagaVi alLadyGagaVi) {
        this.setProject(alLadyGagaVi);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlProProVi avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlProProVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlProProVi> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlProProVi> alProProVis) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alProProVis != null) {
            alProProVis.forEach(i -> i.setParent(this));
        }
        this.children = alProProVis;
    }

    public AlProProVi children(Set<AlProProVi> alProProVis) {
        this.setChildren(alProProVis);
        return this;
    }

    public AlProProVi addChildren(AlProProVi alProProVi) {
        this.children.add(alProProVi);
        alProProVi.setParent(this);
        return this;
    }

    public AlProProVi removeChildren(AlProProVi alProProVi) {
        this.children.remove(alProProVi);
        alProProVi.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlProProVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlProProVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProProVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptionHeitiga='" + getDescriptionHeitiga() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", areaInSquareMeter=" + getAreaInSquareMeter() +
            ", numberOfAdults=" + getNumberOfAdults() +
            ", numberOfPreschoolers=" + getNumberOfPreschoolers() +
            ", numberOfChildren=" + getNumberOfChildren() +
            ", numberOfRooms=" + getNumberOfRooms() +
            ", numberOfFloors=" + getNumberOfFloors() +
            ", bedSize='" + getBedSize() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
