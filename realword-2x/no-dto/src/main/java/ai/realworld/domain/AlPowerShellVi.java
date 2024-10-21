package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPowerShellVi.
 */
@Entity
@Table(name = "al_power_shell_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPowerShellVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "amenities", "images", "children" }, allowSetters = true)
    private AlProProVi propertyProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributeTaxonomy", "application" }, allowSetters = true)
    private AlPounderVi attributeTerm;

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

    public AlPowerShellVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public AlPowerShellVi value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AlProProVi getPropertyProfile() {
        return this.propertyProfile;
    }

    public void setPropertyProfile(AlProProVi alProProVi) {
        this.propertyProfile = alProProVi;
    }

    public AlPowerShellVi propertyProfile(AlProProVi alProProVi) {
        this.setPropertyProfile(alProProVi);
        return this;
    }

    public AlPounderVi getAttributeTerm() {
        return this.attributeTerm;
    }

    public void setAttributeTerm(AlPounderVi alPounderVi) {
        this.attributeTerm = alPounderVi;
    }

    public AlPowerShellVi attributeTerm(AlPounderVi alPounderVi) {
        this.setAttributeTerm(alPounderVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPowerShellVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPowerShellVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPowerShellVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPowerShellVi{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
