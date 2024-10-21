package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlInquiry} entity. This class is used
 * in {@link ai.realworld.web.rest.AlInquiryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-inquiries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlInquiryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter body;

    private StringFilter sender;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter contentJason;

    private UUIDFilter customerId;

    private UUIDFilter agencyId;

    private LongFilter personInChargeId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlInquiryCriteria() {}

    public AlInquiryCriteria(AlInquiryCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.body = other.optionalBody().map(StringFilter::copy).orElse(null);
        this.sender = other.optionalSender().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.contentJason = other.optionalContentJason().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.agencyId = other.optionalAgencyId().map(UUIDFilter::copy).orElse(null);
        this.personInChargeId = other.optionalPersonInChargeId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlInquiryCriteria copy() {
        return new AlInquiryCriteria(this);
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

    public StringFilter getBody() {
        return body;
    }

    public Optional<StringFilter> optionalBody() {
        return Optional.ofNullable(body);
    }

    public StringFilter body() {
        if (body == null) {
            setBody(new StringFilter());
        }
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public StringFilter getSender() {
        return sender;
    }

    public Optional<StringFilter> optionalSender() {
        return Optional.ofNullable(sender);
    }

    public StringFilter sender() {
        if (sender == null) {
            setSender(new StringFilter());
        }
        return sender;
    }

    public void setSender(StringFilter sender) {
        this.sender = sender;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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
        final AlInquiryCriteria that = (AlInquiryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body) &&
            Objects.equals(sender, that.sender) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
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
            body,
            sender,
            email,
            phone,
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
        return "AlInquiryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalBody().map(f -> "body=" + f + ", ").orElse("") +
            optionalSender().map(f -> "sender=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalContentJason().map(f -> "contentJason=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalAgencyId().map(f -> "agencyId=" + f + ", ").orElse("") +
            optionalPersonInChargeId().map(f -> "personInChargeId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
