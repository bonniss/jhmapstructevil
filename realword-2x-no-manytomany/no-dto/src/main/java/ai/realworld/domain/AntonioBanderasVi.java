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
 * A AntonioBanderasVi.
 */
@Entity
@Table(name = "antonio_banderas_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntonioBanderasVi implements Serializable {

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

    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AntonioBanderasVi current;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private AntonioBanderasVi parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private Set<AntonioBanderasVi> children = new HashSet<>();

    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "current")
    private AntonioBanderasVi antonioBanderasVi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AntonioBanderasVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return this.level;
    }

    public AntonioBanderasVi level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return this.code;
    }

    public AntonioBanderasVi code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public AntonioBanderasVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public AntonioBanderasVi fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNativeName() {
        return this.nativeName;
    }

    public AntonioBanderasVi nativeName(String nativeName) {
        this.setNativeName(nativeName);
        return this;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getOfficialCode() {
        return this.officialCode;
    }

    public AntonioBanderasVi officialCode(String officialCode) {
        this.setOfficialCode(officialCode);
        return this;
    }

    public void setOfficialCode(String officialCode) {
        this.officialCode = officialCode;
    }

    public String getDivisionTerm() {
        return this.divisionTerm;
    }

    public AntonioBanderasVi divisionTerm(String divisionTerm) {
        this.setDivisionTerm(divisionTerm);
        return this;
    }

    public void setDivisionTerm(String divisionTerm) {
        this.divisionTerm = divisionTerm;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public AntonioBanderasVi isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public AntonioBanderasVi getCurrent() {
        return this.current;
    }

    public void setCurrent(AntonioBanderasVi antonioBanderasVi) {
        this.current = antonioBanderasVi;
    }

    public AntonioBanderasVi current(AntonioBanderasVi antonioBanderasVi) {
        this.setCurrent(antonioBanderasVi);
        return this;
    }

    public AntonioBanderasVi getParent() {
        return this.parent;
    }

    public void setParent(AntonioBanderasVi antonioBanderasVi) {
        this.parent = antonioBanderasVi;
    }

    public AntonioBanderasVi parent(AntonioBanderasVi antonioBanderasVi) {
        this.setParent(antonioBanderasVi);
        return this;
    }

    public Set<AntonioBanderasVi> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AntonioBanderasVi> antonioBanderasVis) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (antonioBanderasVis != null) {
            antonioBanderasVis.forEach(i -> i.setParent(this));
        }
        this.children = antonioBanderasVis;
    }

    public AntonioBanderasVi children(Set<AntonioBanderasVi> antonioBanderasVis) {
        this.setChildren(antonioBanderasVis);
        return this;
    }

    public AntonioBanderasVi addChildren(AntonioBanderasVi antonioBanderasVi) {
        this.children.add(antonioBanderasVi);
        antonioBanderasVi.setParent(this);
        return this;
    }

    public AntonioBanderasVi removeChildren(AntonioBanderasVi antonioBanderasVi) {
        this.children.remove(antonioBanderasVi);
        antonioBanderasVi.setParent(null);
        return this;
    }

    public AntonioBanderasVi getAntonioBanderasVi() {
        return this.antonioBanderasVi;
    }

    public void setAntonioBanderasVi(AntonioBanderasVi antonioBanderasVi) {
        if (this.antonioBanderasVi != null) {
            this.antonioBanderasVi.setCurrent(null);
        }
        if (antonioBanderasVi != null) {
            antonioBanderasVi.setCurrent(this);
        }
        this.antonioBanderasVi = antonioBanderasVi;
    }

    public AntonioBanderasVi antonioBanderasVi(AntonioBanderasVi antonioBanderasVi) {
        this.setAntonioBanderasVi(antonioBanderasVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AntonioBanderasVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AntonioBanderasVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntonioBanderasVi{" +
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
