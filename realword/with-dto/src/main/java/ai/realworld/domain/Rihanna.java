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
 * A Rihanna.
 */
@Entity
@Table(name = "rihanna")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rihanna implements Serializable {

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

    @Size(max = 10485760)
    @Column(name = "permission_grid_jason", length = 10485760)
    private String permissionGridJason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agent", "role", "application" }, allowSetters = true)
    private Set<HandCraft> agentRoles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rihanna id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Rihanna name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Rihanna description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissionGridJason() {
        return this.permissionGridJason;
    }

    public Rihanna permissionGridJason(String permissionGridJason) {
        this.setPermissionGridJason(permissionGridJason);
        return this;
    }

    public void setPermissionGridJason(String permissionGridJason) {
        this.permissionGridJason = permissionGridJason;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public Rihanna application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<HandCraft> getAgentRoles() {
        return this.agentRoles;
    }

    public void setAgentRoles(Set<HandCraft> handCrafts) {
        if (this.agentRoles != null) {
            this.agentRoles.forEach(i -> i.setRole(null));
        }
        if (handCrafts != null) {
            handCrafts.forEach(i -> i.setRole(this));
        }
        this.agentRoles = handCrafts;
    }

    public Rihanna agentRoles(Set<HandCraft> handCrafts) {
        this.setAgentRoles(handCrafts);
        return this;
    }

    public Rihanna addAgentRoles(HandCraft handCraft) {
        this.agentRoles.add(handCraft);
        handCraft.setRole(this);
        return this;
    }

    public Rihanna removeAgentRoles(HandCraft handCraft) {
        this.agentRoles.remove(handCraft);
        handCraft.setRole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rihanna)) {
            return false;
        }
        return getId() != null && getId().equals(((Rihanna) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rihanna{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", permissionGridJason='" + getPermissionGridJason() + "'" +
            "}";
    }
}
