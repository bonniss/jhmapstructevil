package ai.realworld.domain;

import ai.realworld.domain.enumeration.SitomutaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SicilyUmetoVi.
 */
@Entity
@Table(name = "sicily_umeto_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SicilyUmetoVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SitomutaType type;

    @Size(max = 10485760)
    @Column(name = "content", length = 10485760)
    private String content;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SicilyUmetoVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SitomutaType getType() {
        return this.type;
    }

    public SicilyUmetoVi type(SitomutaType type) {
        this.setType(type);
        return this;
    }

    public void setType(SitomutaType type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public SicilyUmetoVi content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SicilyUmetoVi)) {
            return false;
        }
        return getId() != null && getId().equals(((SicilyUmetoVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SicilyUmetoVi{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
