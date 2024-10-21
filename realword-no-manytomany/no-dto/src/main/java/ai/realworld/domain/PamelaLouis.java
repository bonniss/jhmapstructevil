package ai.realworld.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PamelaLouis.
 */
@Entity
@Table(name = "pamela_louis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PamelaLouis implements Serializable {

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

    @Size(max = 10485760)
    @Column(name = "config_jason", length = 10485760)
    private String configJason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PamelaLouis id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PamelaLouis name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigJason() {
        return this.configJason;
    }

    public PamelaLouis configJason(String configJason) {
        this.setConfigJason(configJason);
        return this;
    }

    public void setConfigJason(String configJason) {
        this.configJason = configJason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PamelaLouis)) {
            return false;
        }
        return getId() != null && getId().equals(((PamelaLouis) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PamelaLouis{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", configJason='" + getConfigJason() + "'" +
            "}";
    }
}
