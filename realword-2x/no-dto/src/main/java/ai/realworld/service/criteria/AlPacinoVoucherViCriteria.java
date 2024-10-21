package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlPacinoVoucherVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlPacinoVoucherViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-pacino-voucher-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoVoucherViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter sourceTitle;

    private StringFilter sourceUrl;

    private InstantFilter collectedDate;

    private UUIDFilter userId;

    private UUIDFilter voucherId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlPacinoVoucherViCriteria() {}

    public AlPacinoVoucherViCriteria(AlPacinoVoucherViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.sourceTitle = other.optionalSourceTitle().map(StringFilter::copy).orElse(null);
        this.sourceUrl = other.optionalSourceUrl().map(StringFilter::copy).orElse(null);
        this.collectedDate = other.optionalCollectedDate().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.voucherId = other.optionalVoucherId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlPacinoVoucherViCriteria copy() {
        return new AlPacinoVoucherViCriteria(this);
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

    public StringFilter getSourceTitle() {
        return sourceTitle;
    }

    public Optional<StringFilter> optionalSourceTitle() {
        return Optional.ofNullable(sourceTitle);
    }

    public StringFilter sourceTitle() {
        if (sourceTitle == null) {
            setSourceTitle(new StringFilter());
        }
        return sourceTitle;
    }

    public void setSourceTitle(StringFilter sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public StringFilter getSourceUrl() {
        return sourceUrl;
    }

    public Optional<StringFilter> optionalSourceUrl() {
        return Optional.ofNullable(sourceUrl);
    }

    public StringFilter sourceUrl() {
        if (sourceUrl == null) {
            setSourceUrl(new StringFilter());
        }
        return sourceUrl;
    }

    public void setSourceUrl(StringFilter sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public InstantFilter getCollectedDate() {
        return collectedDate;
    }

    public Optional<InstantFilter> optionalCollectedDate() {
        return Optional.ofNullable(collectedDate);
    }

    public InstantFilter collectedDate() {
        if (collectedDate == null) {
            setCollectedDate(new InstantFilter());
        }
        return collectedDate;
    }

    public void setCollectedDate(InstantFilter collectedDate) {
        this.collectedDate = collectedDate;
    }

    public UUIDFilter getUserId() {
        return userId;
    }

    public Optional<UUIDFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public UUIDFilter userId() {
        if (userId == null) {
            setUserId(new UUIDFilter());
        }
        return userId;
    }

    public void setUserId(UUIDFilter userId) {
        this.userId = userId;
    }

    public UUIDFilter getVoucherId() {
        return voucherId;
    }

    public Optional<UUIDFilter> optionalVoucherId() {
        return Optional.ofNullable(voucherId);
    }

    public UUIDFilter voucherId() {
        if (voucherId == null) {
            setVoucherId(new UUIDFilter());
        }
        return voucherId;
    }

    public void setVoucherId(UUIDFilter voucherId) {
        this.voucherId = voucherId;
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
        final AlPacinoVoucherViCriteria that = (AlPacinoVoucherViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceTitle, that.sourceTitle) &&
            Objects.equals(sourceUrl, that.sourceUrl) &&
            Objects.equals(collectedDate, that.collectedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(voucherId, that.voucherId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceTitle, sourceUrl, collectedDate, userId, voucherId, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoVoucherViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSourceTitle().map(f -> "sourceTitle=" + f + ", ").orElse("") +
            optionalSourceUrl().map(f -> "sourceUrl=" + f + ", ").orElse("") +
            optionalCollectedDate().map(f -> "collectedDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalVoucherId().map(f -> "voucherId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
