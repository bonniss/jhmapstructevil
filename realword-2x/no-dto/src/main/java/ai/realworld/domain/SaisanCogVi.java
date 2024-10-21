package ai.realworld.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SaisanCogVi.
 */
@Entity
@Table(name = "saisan_cog_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaisanCogVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "key", length = 256, nullable = false, unique = true)
    private String key;

    @Size(max = 10485760)
    @Column(name = "value_jason", length = 10485760)
    private String valueJason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SaisanCogVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public SaisanCogVi key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValueJason() {
        return this.valueJason;
    }

    public SaisanCogVi valueJason(String valueJason) {
        this.setValueJason(valueJason);
        return this;
    }

    public void setValueJason(String valueJason) {
        this.valueJason = valueJason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaisanCogVi)) {
            return false;
        }
        return getId() != null && getId().equals(((SaisanCogVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaisanCogVi{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", valueJason='" + getValueJason() + "'" +
            "}";
    }
}
