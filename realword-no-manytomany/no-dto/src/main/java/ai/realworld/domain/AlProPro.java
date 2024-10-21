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
 * A AlProPro.
 */
@Entity
@Table(name = "al_pro_pro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlProPro implements Serializable {

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
    private AlProPro parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "avatar", "application" }, allowSetters = true)
    private AlLadyGaga project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "children" }, allowSetters = true)
    private Set<AlProPro> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlProPro id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlProPro name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeitiga() {
        return this.descriptionHeitiga;
    }

    public AlProPro descriptionHeitiga(String descriptionHeitiga) {
        this.setDescriptionHeitiga(descriptionHeitiga);
        return this;
    }

    public void setDescriptionHeitiga(String descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public PeteType getPropertyType() {
        return this.propertyType;
    }

    public AlProPro propertyType(PeteType propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
    }

    public Float getAreaInSquareMeter() {
        return this.areaInSquareMeter;
    }

    public AlProPro areaInSquareMeter(Float areaInSquareMeter) {
        this.setAreaInSquareMeter(areaInSquareMeter);
        return this;
    }

    public void setAreaInSquareMeter(Float areaInSquareMeter) {
        this.areaInSquareMeter = areaInSquareMeter;
    }

    public Integer getNumberOfAdults() {
        return this.numberOfAdults;
    }

    public AlProPro numberOfAdults(Integer numberOfAdults) {
        this.setNumberOfAdults(numberOfAdults);
        return this;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfPreschoolers() {
        return this.numberOfPreschoolers;
    }

    public AlProPro numberOfPreschoolers(Integer numberOfPreschoolers) {
        this.setNumberOfPreschoolers(numberOfPreschoolers);
        return this;
    }

    public void setNumberOfPreschoolers(Integer numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public Integer getNumberOfChildren() {
        return this.numberOfChildren;
    }

    public AlProPro numberOfChildren(Integer numberOfChildren) {
        this.setNumberOfChildren(numberOfChildren);
        return this;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfRooms() {
        return this.numberOfRooms;
    }

    public AlProPro numberOfRooms(Integer numberOfRooms) {
        this.setNumberOfRooms(numberOfRooms);
        return this;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfFloors() {
        return this.numberOfFloors;
    }

    public AlProPro numberOfFloors(Integer numberOfFloors) {
        this.setNumberOfFloors(numberOfFloors);
        return this;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public BenedictToy getBedSize() {
        return this.bedSize;
    }

    public AlProPro bedSize(BenedictToy bedSize) {
        this.setBedSize(bedSize);
        return this;
    }

    public void setBedSize(BenedictToy bedSize) {
        this.bedSize = bedSize;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlProPro isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProPro getParent() {
        return this.parent;
    }

    public void setParent(AlProPro alProPro) {
        this.parent = alProPro;
    }

    public AlProPro parent(AlProPro alProPro) {
        this.setParent(alProPro);
        return this;
    }

    public AlLadyGaga getProject() {
        return this.project;
    }

    public void setProject(AlLadyGaga alLadyGaga) {
        this.project = alLadyGaga;
    }

    public AlProPro project(AlLadyGaga alLadyGaga) {
        this.setProject(alLadyGaga);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlProPro avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlProPro application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlProPro> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlProPro> alProPros) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alProPros != null) {
            alProPros.forEach(i -> i.setParent(this));
        }
        this.children = alProPros;
    }

    public AlProPro children(Set<AlProPro> alProPros) {
        this.setChildren(alProPros);
        return this;
    }

    public AlProPro addChildren(AlProPro alProPro) {
        this.children.add(alProPro);
        alProPro.setParent(this);
        return this;
    }

    public AlProPro removeChildren(AlProPro alProPro) {
        this.children.remove(alProPro);
        alProPro.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlProPro)) {
            return false;
        }
        return getId() != null && getId().equals(((AlProPro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlProPro{" +
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
