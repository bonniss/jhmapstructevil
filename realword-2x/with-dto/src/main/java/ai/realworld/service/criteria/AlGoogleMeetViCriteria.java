package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlGoogleMeetVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlGoogleMeetViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-google-meet-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoogleMeetViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter numberOfParticipants;

    private InstantFilter plannedDate;

    private DoubleFilter plannedDurationInMinutes;

    private InstantFilter actualDate;

    private DoubleFilter actualDurationInMinutes;

    private StringFilter contentJason;

    private UUIDFilter customerId;

    private UUIDFilter agencyId;

    private LongFilter personInChargeId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlGoogleMeetViCriteria() {}

    public AlGoogleMeetViCriteria(AlGoogleMeetViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.numberOfParticipants = other.optionalNumberOfParticipants().map(IntegerFilter::copy).orElse(null);
        this.plannedDate = other.optionalPlannedDate().map(InstantFilter::copy).orElse(null);
        this.plannedDurationInMinutes = other.optionalPlannedDurationInMinutes().map(DoubleFilter::copy).orElse(null);
        this.actualDate = other.optionalActualDate().map(InstantFilter::copy).orElse(null);
        this.actualDurationInMinutes = other.optionalActualDurationInMinutes().map(DoubleFilter::copy).orElse(null);
        this.contentJason = other.optionalContentJason().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.agencyId = other.optionalAgencyId().map(UUIDFilter::copy).orElse(null);
        this.personInChargeId = other.optionalPersonInChargeId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlGoogleMeetViCriteria copy() {
        return new AlGoogleMeetViCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public Optional<IntegerFilter> optionalNumberOfParticipants() {
        return Optional.ofNullable(numberOfParticipants);
    }

    public IntegerFilter numberOfParticipants() {
        if (numberOfParticipants == null) {
            setNumberOfParticipants(new IntegerFilter());
        }
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(IntegerFilter numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public InstantFilter getPlannedDate() {
        return plannedDate;
    }

    public Optional<InstantFilter> optionalPlannedDate() {
        return Optional.ofNullable(plannedDate);
    }

    public InstantFilter plannedDate() {
        if (plannedDate == null) {
            setPlannedDate(new InstantFilter());
        }
        return plannedDate;
    }

    public void setPlannedDate(InstantFilter plannedDate) {
        this.plannedDate = plannedDate;
    }

    public DoubleFilter getPlannedDurationInMinutes() {
        return plannedDurationInMinutes;
    }

    public Optional<DoubleFilter> optionalPlannedDurationInMinutes() {
        return Optional.ofNullable(plannedDurationInMinutes);
    }

    public DoubleFilter plannedDurationInMinutes() {
        if (plannedDurationInMinutes == null) {
            setPlannedDurationInMinutes(new DoubleFilter());
        }
        return plannedDurationInMinutes;
    }

    public void setPlannedDurationInMinutes(DoubleFilter plannedDurationInMinutes) {
        this.plannedDurationInMinutes = plannedDurationInMinutes;
    }

    public InstantFilter getActualDate() {
        return actualDate;
    }

    public Optional<InstantFilter> optionalActualDate() {
        return Optional.ofNullable(actualDate);
    }

    public InstantFilter actualDate() {
        if (actualDate == null) {
            setActualDate(new InstantFilter());
        }
        return actualDate;
    }

    public void setActualDate(InstantFilter actualDate) {
        this.actualDate = actualDate;
    }

    public DoubleFilter getActualDurationInMinutes() {
        return actualDurationInMinutes;
    }

    public Optional<DoubleFilter> optionalActualDurationInMinutes() {
        return Optional.ofNullable(actualDurationInMinutes);
    }

    public DoubleFilter actualDurationInMinutes() {
        if (actualDurationInMinutes == null) {
            setActualDurationInMinutes(new DoubleFilter());
        }
        return actualDurationInMinutes;
    }

    public void setActualDurationInMinutes(DoubleFilter actualDurationInMinutes) {
        this.actualDurationInMinutes = actualDurationInMinutes;
    }

    public StringFilter getContentJason() {
        return contentJason;
    }

    public Optional<StringFilter> optionalContentJason() {
        return Optional.ofNullable(contentJason);
    }

    public StringFilter contentJason() {
        if (contentJason == null) {
            setContentJason(new StringFilter());
        }
        return contentJason;
    }

    public void setContentJason(StringFilter contentJason) {
        this.contentJason = contentJason;
    }

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
    }

    public UUIDFilter getAgencyId() {
        return agencyId;
    }

    public Optional<UUIDFilter> optionalAgencyId() {
        return Optional.ofNullable(agencyId);
    }

    public UUIDFilter agencyId() {
        if (agencyId == null) {
            setAgencyId(new UUIDFilter());
        }
        return agencyId;
    }

    public void setAgencyId(UUIDFilter agencyId) {
        this.agencyId = agencyId;
    }

    public LongFilter getPersonInChargeId() {
        return personInChargeId;
    }

    public Optional<LongFilter> optionalPersonInChargeId() {
        return Optional.ofNullable(personInChargeId);
    }

    public LongFilter personInChargeId() {
        if (personInChargeId == null) {
            setPersonInChargeId(new LongFilter());
        }
        return personInChargeId;
    }

    public void setPersonInChargeId(LongFilter personInChargeId) {
        this.personInChargeId = personInChargeId;
    }

    public UUIDFilter getApplicationId() {
        return applicationId;
    }

    public Optional<UUIDFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public UUIDFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new UUIDFilter());
        }
        return applicationId;
    }

    public void setApplicationId(UUIDFilter applicationId) {
        this.applicationId = applicationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlGoogleMeetViCriteria that = (AlGoogleMeetViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(numberOfParticipants, that.numberOfParticipants) &&
            Objects.equals(plannedDate, that.plannedDate) &&
            Objects.equals(plannedDurationInMinutes, that.plannedDurationInMinutes) &&
            Objects.equals(actualDate, that.actualDate) &&
            Objects.equals(actualDurationInMinutes, that.actualDurationInMinutes) &&
            Objects.equals(contentJason, that.contentJason) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(agencyId, that.agencyId) &&
            Objects.equals(personInChargeId, that.personInChargeId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            numberOfParticipants,
            plannedDate,
            plannedDurationInMinutes,
            actualDate,
            actualDurationInMinutes,
            contentJason,
            customerId,
            agencyId,
            personInChargeId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoogleMeetViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalNumberOfParticipants().map(f -> "numberOfParticipants=" + f + ", ").orElse("") +
            optionalPlannedDate().map(f -> "plannedDate=" + f + ", ").orElse("") +
            optionalPlannedDurationInMinutes().map(f -> "plannedDurationInMinutes=" + f + ", ").orElse("") +
            optionalActualDate().map(f -> "actualDate=" + f + ", ").orElse("") +
            optionalActualDurationInMinutes().map(f -> "actualDurationInMinutes=" + f + ", ").orElse("") +
            optionalContentJason().map(f -> "contentJason=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalAgencyId().map(f -> "agencyId=" + f + ", ").orElse("") +
            optionalPersonInChargeId().map(f -> "personInChargeId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
