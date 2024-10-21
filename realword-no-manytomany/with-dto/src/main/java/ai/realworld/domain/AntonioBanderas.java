package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AntonioBanderas.
 */
@Entity
@Table(name = "antonio_banderas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntonioBanderas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "level")
    private Integer level;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "code", length = 160, nullable = false, unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "native_name")
    private String nativeName;

    @Column(name = "official_code")
    private String officialCode;

    @Column(name = "division_term")
    private String divisionTerm;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AntonioBanderas current;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private AntonioBanderas parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private Set<AntonioBanderas> children = new HashSet<>();

    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "current")
    private AntonioBanderas antonioBanderas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AntonioBanderas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return this.level;
    }

    public AntonioBanderas level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return this.code;
    }

    public AntonioBanderas code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public AntonioBanderas name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public AntonioBanderas fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNativeName() {
        return this.nativeName;
    }

    public AntonioBanderas nativeName(String nativeName) {
        this.setNativeName(nativeName);
        return this;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getOfficialCode() {
        return this.officialCode;
    }

    public AntonioBanderas officialCode(String officialCode) {
        this.setOfficialCode(officialCode);
        return this;
    }

    public void setOfficialCode(String officialCode) {
        this.officialCode = officialCode;
    }

    public String getDivisionTerm() {
        return this.divisionTerm;
    }

    public AntonioBanderas divisionTerm(String divisionTerm) {
        this.setDivisionTerm(divisionTerm);
        return this;
    }

    public void setDivisionTerm(String divisionTerm) {
        this.divisionTerm = divisionTerm;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public AntonioBanderas isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public AntonioBanderas getCurrent() {
        return this.current;
    }

    public void setCurrent(AntonioBanderas antonioBanderas) {
        this.current = antonioBanderas;
    }

    public AntonioBanderas current(AntonioBanderas antonioBanderas) {
        this.setCurrent(antonioBanderas);
        return this;
    }

    public AntonioBanderas getParent() {
        return this.parent;
    }

    public void setParent(AntonioBanderas antonioBanderas) {
        this.parent = antonioBanderas;
    }

    public AntonioBanderas parent(AntonioBanderas antonioBanderas) {
        this.setParent(antonioBanderas);
        return this;
    }

    public Set<AntonioBanderas> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AntonioBanderas> antonioBanderas) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (antonioBanderas != null) {
            antonioBanderas.forEach(i -> i.setParent(this));
        }
        this.children = antonioBanderas;
    }

    public AntonioBanderas children(Set<AntonioBanderas> antonioBanderas) {
        this.setChildren(antonioBanderas);
        return this;
    }

    public AntonioBanderas addChildren(AntonioBanderas antonioBanderas) {
        this.children.add(antonioBanderas);
        antonioBanderas.setParent(this);
        return this;
    }

    public AntonioBanderas removeChildren(AntonioBanderas antonioBanderas) {
        this.children.remove(antonioBanderas);
        antonioBanderas.setParent(null);
        return this;
    }

    public AntonioBanderas getAntonioBanderas() {
        return this.antonioBanderas;
    }

    public void setAntonioBanderas(AntonioBanderas antonioBanderas) {
        if (this.antonioBanderas != null) {
            this.antonioBanderas.setCurrent(null);
        }
        if (antonioBanderas != null) {
            antonioBanderas.setCurrent(this);
        }
        this.antonioBanderas = antonioBanderas;
    }

    public AntonioBanderas antonioBanderas(AntonioBanderas antonioBanderas) {
        this.setAntonioBanderas(antonioBanderas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AntonioBanderas)) {
            return false;
        }
        return getId() != null && getId().equals(((AntonioBanderas) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntonioBanderas{" +
            "id=" + getId() +
            ", level=" + getLevel() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", nativeName='" + getNativeName() + "'" +
            ", officialCode='" + getOfficialCode() + "'" +
            ", divisionTerm='" + getDivisionTerm() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
