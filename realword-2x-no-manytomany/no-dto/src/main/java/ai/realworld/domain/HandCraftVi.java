package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HandCraftVi.
 */
@Entity
@Table(name = "hand_craft_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HandCraftVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agency", "avatar", "internalUser", "appUser", "application", "agentRoles" }, allowSetters = true)
    private EdSheeranVi agent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "application", "agentRoles" }, allowSetters = true)
    private RihannaVi role;

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

    public HandCraftVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EdSheeranVi getAgent() {
        return this.agent;
    }

    public void setAgent(EdSheeranVi edSheeranVi) {
        this.agent = edSheeranVi;
    }

    public HandCraftVi agent(EdSheeranVi edSheeranVi) {
        this.setAgent(edSheeranVi);
        return this;
    }

    public RihannaVi getRole() {
        return this.role;
    }

    public void setRole(RihannaVi rihannaVi) {
        this.role = rihannaVi;
    }

    public HandCraftVi role(RihannaVi rihannaVi) {
        this.setRole(rihannaVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public HandCraftVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandCraftVi)) {
            return false;
        }
        return getId() != null && getId().equals(((HandCraftVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HandCraftVi{" +
            "id=" + getId() +
            "}";
    }
}
