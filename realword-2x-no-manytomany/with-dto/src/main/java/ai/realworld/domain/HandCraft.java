package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HandCraft.
 */
@Entity
@Table(name = "hand_craft")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HandCraft implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agency", "avatar", "internalUser", "appUser", "application", "agentRoles" }, allowSetters = true)
    private EdSheeran agent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "agentRoles" }, allowSetters = true)
    private Rihanna role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HandCraft id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EdSheeran getAgent() {
        return this.agent;
    }

    public void setAgent(EdSheeran edSheeran) {
        this.agent = edSheeran;
    }

    public HandCraft agent(EdSheeran edSheeran) {
        this.setAgent(edSheeran);
        return this;
    }

    public Rihanna getRole() {
        return this.role;
    }

    public void setRole(Rihanna rihanna) {
        this.role = rihanna;
    }

    public HandCraft role(Rihanna rihanna) {
        this.setRole(rihanna);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public HandCraft application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandCraft)) {
            return false;
        }
        return getId() != null && getId().equals(((HandCraft) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HandCraft{" +
            "id=" + getId() +
            "}";
    }
}
