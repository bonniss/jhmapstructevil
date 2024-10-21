package ai.realworld.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HashRossVi.
 */
@Entity
@Table(name = "hash_ross_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HashRossVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "slug", length = 160, nullable = false, unique = true)
    private String slug;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Size(max = 10485760)
    @Column(name = "permission_grid_jason", length = 10485760)
    private String permissionGridJason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HashRossVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HashRossVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return this.slug;
    }

    public HashRossVi slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return this.description;
    }

    public HashRossVi description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissionGridJason() {
        return this.permissionGridJason;
    }

    public HashRossVi permissionGridJason(String permissionGridJason) {
        this.setPermissionGridJason(permissionGridJason);
        return this;
    }

    public void setPermissionGridJason(String permissionGridJason) {
        this.permissionGridJason = permissionGridJason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashRossVi)) {
            return false;
        }
        return getId() != null && getId().equals(((HashRossVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HashRossVi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", description='" + getDescription() + "'" +
            ", permissionGridJason='" + getPermissionGridJason() + "'" +
            "}";
    }
}
