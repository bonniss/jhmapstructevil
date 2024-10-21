package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlGoogleMeet.
 */
@Entity
@Table(name = "al_google_meet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoogleMeet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @Column(name = "planned_date")
    private Instant plannedDate;

    @Column(name = "planned_duration_in_minutes")
    private Double plannedDurationInMinutes;

    @Column(name = "actual_date")
    private Instant actualDate;

    @Column(name = "actual_duration_in_minutes")
    private Double actualDurationInMinutes;

    @Size(max = 10485760)
    @Column(name = "content_jason", length = 10485760)
    private String contentJason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private AlPacino customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "agencyType", "logo", "application", "agents" }, allowSetters = true)
    private AlApple agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agency", "avatar", "internalUser", "appUser", "application", "agentRoles" }, allowSetters = true)
    private EdSheeran personInCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlGoogleMeet id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AlGoogleMeet title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public AlGoogleMeet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    public AlGoogleMeet numberOfParticipants(Integer numberOfParticipants) {
        this.setNumberOfParticipants(numberOfParticipants);
        return this;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Instant getPlannedDate() {
        return this.plannedDate;
    }

    public AlGoogleMeet plannedDate(Instant plannedDate) {
        this.setPlannedDate(plannedDate);
        return this;
    }

    public void setPlannedDate(Instant plannedDate) {
        this.plannedDate = plannedDate;
    }

    public Double getPlannedDurationInMinutes() {
        return this.plannedDurationInMinutes;
    }

    public AlGoogleMeet plannedDurationInMinutes(Double plannedDurationInMinutes) {
        this.setPlannedDurationInMinutes(plannedDurationInMinutes);
        return this;
    }

    public void setPlannedDurationInMinutes(Double plannedDurationInMinutes) {
        this.plannedDurationInMinutes = plannedDurationInMinutes;
    }

    public Instant getActualDate() {
        return this.actualDate;
    }

    public AlGoogleMeet actualDate(Instant actualDate) {
        this.setActualDate(actualDate);
        return this;
    }

    public void setActualDate(Instant actualDate) {
        this.actualDate = actualDate;
    }

    public Double getActualDurationInMinutes() {
        return this.actualDurationInMinutes;
    }

    public AlGoogleMeet actualDurationInMinutes(Double actualDurationInMinutes) {
        this.setActualDurationInMinutes(actualDurationInMinutes);
        return this;
    }

    public void setActualDurationInMinutes(Double actualDurationInMinutes) {
        this.actualDurationInMinutes = actualDurationInMinutes;
    }

    public String getContentJason() {
        return this.contentJason;
    }

    public AlGoogleMeet contentJason(String contentJason) {
        this.setContentJason(contentJason);
        return this;
    }

    public void setContentJason(String contentJason) {
        this.contentJason = contentJason;
    }

    public AlPacino getCustomer() {
        return this.customer;
    }

    public void setCustomer(AlPacino alPacino) {
        this.customer = alPacino;
    }

    public AlGoogleMeet customer(AlPacino alPacino) {
        this.setCustomer(alPacino);
        return this;
    }

    public AlApple getAgency() {
        return this.agency;
    }

    public void setAgency(AlApple alApple) {
        this.agency = alApple;
    }

    public AlGoogleMeet agency(AlApple alApple) {
        this.setAgency(alApple);
        return this;
    }

    public EdSheeran getPersonInCharge() {
        return this.personInCharge;
    }

    public void setPersonInCharge(EdSheeran edSheeran) {
        this.personInCharge = edSheeran;
    }

    public AlGoogleMeet personInCharge(EdSheeran edSheeran) {
        this.setPersonInCharge(edSheeran);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlGoogleMeet application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlGoogleMeet)) {
            return false;
        }
        return getId() != null && getId().equals(((AlGoogleMeet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoogleMeet{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfParticipants=" + getNumberOfParticipants() +
            ", plannedDate='" + getPlannedDate() + "'" +
            ", plannedDurationInMinutes=" + getPlannedDurationInMinutes() +
            ", actualDate='" + getActualDate() + "'" +
            ", actualDurationInMinutes=" + getActualDurationInMinutes() +
            ", contentJason='" + getContentJason() + "'" +
            "}";
    }
}
