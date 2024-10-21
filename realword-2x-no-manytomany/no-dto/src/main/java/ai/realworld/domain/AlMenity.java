package ai.realworld.domain;

import ai.realworld.domain.enumeration.PeteType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlMenity.
 */
@Entity
@Table(name = "al_menity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlMenity implements Serializable {

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

    @Column(name = "icon_svg")
    private String iconSvg;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PeteType propertyType;

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

    public AlMenity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlMenity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconSvg() {
        return this.iconSvg;
    }

    public AlMenity iconSvg(String iconSvg) {
        this.setIconSvg(iconSvg);
        return this;
    }

    public void setIconSvg(String iconSvg) {
        this.iconSvg = iconSvg;
    }

    public PeteType getPropertyType() {
        return this.propertyType;
    }

    public AlMenity propertyType(PeteType propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PeteType propertyType) {
        this.propertyType = propertyType;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlMenity application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlMenity)) {
            return false;
        }
        return getId() != null && getId().equals(((AlMenity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlMenity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", iconSvg='" + getIconSvg() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            "}";
    }
}
