package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPowerShell.
 */
@Entity
@Table(name = "al_power_shell")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPowerShell implements Serializable {

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
    private AlProPro propertyProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributeTaxonomy", "application" }, allowSetters = true)
    private AlPounder attributeTerm;

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

    public AlPowerShell id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public AlPowerShell value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AlProPro getPropertyProfile() {
        return this.propertyProfile;
    }

    public void setPropertyProfile(AlProPro alProPro) {
        this.propertyProfile = alProPro;
    }

    public AlPowerShell propertyProfile(AlProPro alProPro) {
        this.setPropertyProfile(alProPro);
        return this;
    }

    public AlPounder getAttributeTerm() {
        return this.attributeTerm;
    }

    public void setAttributeTerm(AlPounder alPounder) {
        this.attributeTerm = alPounder;
    }

    public AlPowerShell attributeTerm(AlPounder alPounder) {
        this.setAttributeTerm(alPounder);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPowerShell application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPowerShell)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPowerShell) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPowerShell{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
