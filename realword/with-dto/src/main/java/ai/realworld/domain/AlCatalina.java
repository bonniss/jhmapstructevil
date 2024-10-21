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
 * A AlCatalina.
 */
@Entity
@Table(name = "al_catalina")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlCatalina implements Serializable {

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
    @JsonIgnoreProperties(value = { "parent", "avatar", "application", "children" }, allowSetters = true)
    private AlCatalina parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "avatar", "application", "children" }, allowSetters = true)
    private Set<AlCatalina> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlCatalina id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlCatalina name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlCatalina description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTreeDepth() {
        return this.treeDepth;
    }

    public AlCatalina treeDepth(Integer treeDepth) {
        this.setTreeDepth(treeDepth);
        return this;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
    }

    public AlCatalina getParent() {
        return this.parent;
    }

    public void setParent(AlCatalina alCatalina) {
        this.parent = alCatalina;
    }

    public AlCatalina parent(AlCatalina alCatalina) {
        this.setParent(alCatalina);
        return this;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlCatalina avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlCatalina application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlCatalina> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AlCatalina> alCatalinas) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (alCatalinas != null) {
            alCatalinas.forEach(i -> i.setParent(this));
        }
        this.children = alCatalinas;
    }

    public AlCatalina children(Set<AlCatalina> alCatalinas) {
        this.setChildren(alCatalinas);
        return this;
    }

    public AlCatalina addChildren(AlCatalina alCatalina) {
        this.children.add(alCatalina);
        alCatalina.setParent(this);
        return this;
    }

    public AlCatalina removeChildren(AlCatalina alCatalina) {
        this.children.remove(alCatalina);
        alCatalina.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlCatalina)) {
            return false;
        }
        return getId() != null && getId().equals(((AlCatalina) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlCatalina{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", treeDepth=" + getTreeDepth() +
            "}";
    }
}
