package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlGoogleMeet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoogleMeetDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String title;

    @Size(max = 65535)
    private String description;

    private Integer numberOfParticipants;

    private Instant plannedDate;

    private Double plannedDurationInMinutes;

    private Instant actualDate;

    private Double actualDurationInMinutes;

    @Size(max = 10485760)
    private String contentJason;

    private AlPacinoDTO customer;

    private AlAppleDTO agency;

    private EdSheeranDTO personInCharge;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Instant getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(Instant plannedDate) {
        this.plannedDate = plannedDate;
    }

    public Double getPlannedDurationInMinutes() {
        return plannedDurationInMinutes;
    }

    public void setPlannedDurationInMinutes(Double plannedDurationInMinutes) {
        this.plannedDurationInMinutes = plannedDurationInMinutes;
    }

    public Instant getActualDate() {
        return actualDate;
    }

    public void setActualDate(Instant actualDate) {
        this.actualDate = actualDate;
    }

    public Double getActualDurationInMinutes() {
        return actualDurationInMinutes;
    }

    public void setActualDurationInMinutes(Double actualDurationInMinutes) {
        this.actualDurationInMinutes = actualDurationInMinutes;
    }

    public String getContentJason() {
        return contentJason;
    }

    public void setContentJason(String contentJason) {
        this.contentJason = contentJason;
    }

    public AlPacinoDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlPacinoDTO customer) {
        this.customer = customer;
    }

    public AlAppleDTO getAgency() {
        return agency;
    }

    public void setAgency(AlAppleDTO agency) {
        this.agency = agency;
    }

    public EdSheeranDTO getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(EdSheeranDTO personInCharge) {
        this.personInCharge = personInCharge;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlGoogleMeetDTO)) {
            return false;
        }

        AlGoogleMeetDTO alGoogleMeetDTO = (AlGoogleMeetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alGoogleMeetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoogleMeetDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfParticipants=" + getNumberOfParticipants() +
            ", plannedDate='" + getPlannedDate() + "'" +
            ", plannedDurationInMinutes=" + getPlannedDurationInMinutes() +
            ", actualDate='" + getActualDate() + "'" +
            ", actualDurationInMinutes=" + getActualDurationInMinutes() +
            ", contentJason='" + getContentJason() + "'" +
            ", customer=" + getCustomer() +
            ", agency=" + getAgency() +
            ", personInCharge=" + getPersonInCharge() +
            ", application=" + getApplication() +
            "}";
    }
}
