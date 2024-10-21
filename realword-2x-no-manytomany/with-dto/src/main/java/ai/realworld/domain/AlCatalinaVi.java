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
 * A AlCatalinaVi.
 */
@Entity
@Table(name = "al_catalina_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlCatalinaVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "tree_depth")
    private Integer treeDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "avatar", "children" }, allowSetters = true)
    private AlCatalinaVi parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Metaverse avatar;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "avatar", "children" }, allowSetters = true)
    private Set<AlCatalinaVi> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlCatalinaVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlCatalinaVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlCatalinaVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTreeDepth() {
        return this.treeDepth;
    }

    public AlCatalinaVi treeDepth(Integer treeDepth) {
        this.setTreeDepth(treeDepth);
        return this;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
    }

    public AlCatalinaVi getParent() {
        return this.parent;
    }

    public void setParent(AlCatalinaVi alCatalinaVi) {
        this.parent = alCatalinaVi;
    }

    public AlCatalinaVi parent(AlCatalinaVi alCatalinaVi) {
        this.setParent(alCatalinaVi);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlCatalinaVi avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public Set<AlCatalinaVi> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlCatalinaVi> alCatalinaVis) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alCatalinaVis != null) {
            alCatalinaVis.forEach(i -> i.setParent(this));
        }
        this.children = alCatalinaVis;
    }

    public AlCatalinaVi children(Set<AlCatalinaVi> alCatalinaVis) {
        this.setChildren(alCatalinaVis);
        return this;
    }

    public AlCatalinaVi addChildren(AlCatalinaVi alCatalinaVi) {
        this.children.add(alCatalinaVi);
        alCatalinaVi.setParent(this);
        return this;
    }

    public AlCatalinaVi removeChildren(AlCatalinaVi alCatalinaVi) {
        this.children.remove(alCatalinaVi);
        alCatalinaVi.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlCatalinaVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlCatalinaVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlCatalinaVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", treeDepth=" + getTreeDepth() +
            "}";
    }
}
