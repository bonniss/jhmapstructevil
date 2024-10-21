package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlLeandro.
 */
@Entity
@Table(name = "al_leandro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "separate_winning_by_periods")
    private Boolean separateWinningByPeriods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse programBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse wheelBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maggi")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "image", "maggi", "application" }, allowSetters = true)
    private Set<AlDesire> awards = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maggi")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "image", "maggi" }, allowSetters = true)
    private Set<AlDesireVi> awardVis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlLeandro id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlLeandro name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public AlLeandro weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return this.description;
    }

    public AlLeandro description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFromDate() {
        return this.fromDate;
    }

    public AlLeandro fromDate(LocalDate fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return this.toDate;
    }

    public AlLeandro toDate(LocalDate toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public AlLeandro isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getSeparateWinningByPeriods() {
        return this.separateWinningByPeriods;
    }

    public AlLeandro separateWinningByPeriods(Boolean separateWinningByPeriods) {
        this.setSeparateWinningByPeriods(separateWinningByPeriods);
        return this;
    }

    public void setSeparateWinningByPeriods(Boolean separateWinningByPeriods) {
        this.separateWinningByPeriods = separateWinningByPeriods;
    }

    public Metaverse getProgramBackground() {
        return this.programBackground;
    }

    public void setProgramBackground(Metaverse metaverse) {
        this.programBackground = metaverse;
    }

    public AlLeandro programBackground(Metaverse metaverse) {
        this.setProgramBackground(metaverse);
        return this;
    }

    public Metaverse getWheelBackground() {
        return this.wheelBackground;
    }

    public void setWheelBackground(Metaverse metaverse) {
        this.wheelBackground = metaverse;
    }

    public AlLeandro wheelBackground(Metaverse metaverse) {
        this.setWheelBackground(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlLeandro application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlDesire> getAwards() {
        return this.awards;
    }

    public void setAwards(Set<AlDesire> alDesires) {
        if (this.awards != null) {
            this.awards.forEach(i -> i.setMaggi(null));
        }
        if (alDesires != null) {
            alDesires.forEach(i -> i.setMaggi(this));
        }
        this.awards = alDesires;
    }

    public AlLeandro awards(Set<AlDesire> alDesires) {
        this.setAwards(alDesires);
        return this;
    }

    public AlLeandro addAwards(AlDesire alDesire) {
        this.awards.add(alDesire);
        alDesire.setMaggi(this);
        return this;
    }

    public AlLeandro removeAwards(AlDesire alDesire) {
        this.awards.remove(alDesire);
        alDesire.setMaggi(null);
        return this;
    }

    public Set<AlDesireVi> getAwardVis() {
        return this.awardVis;
    }

    public void setAwardVis(Set<AlDesireVi> alDesireVis) {
        if (this.awardVis != null) {
            this.awardVis.forEach(i -> i.setMaggi(null));
        }
        if (alDesireVis != null) {
            alDesireVis.forEach(i -> i.setMaggi(this));
        }
        this.awardVis = alDesireVis;
    }

    public AlLeandro awardVis(Set<AlDesireVi> alDesireVis) {
        this.setAwardVis(alDesireVis);
        return this;
    }

    public AlLeandro addAwardVis(AlDesireVi alDesireVi) {
        this.awardVis.add(alDesireVi);
        alDesireVi.setMaggi(this);
        return this;
    }

    public AlLeandro removeAwardVis(AlDesireVi alDesireVi) {
        this.awardVis.remove(alDesireVi);
        alDesireVi.setMaggi(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlLeandro)) {
            return false;
        }
        return getId() != null && getId().equals(((AlLeandro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandro{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", description='" + getDescription() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", separateWinningByPeriods='" + getSeparateWinningByPeriods() + "'" +
            "}";
    }
}
