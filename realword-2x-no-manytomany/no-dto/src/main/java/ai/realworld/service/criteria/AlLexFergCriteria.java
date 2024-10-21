package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.PaoloStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlLexFerg} entity. This class is used
 * in {@link ai.realworld.web.rest.AlLexFergResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-lex-fergs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLexFergCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaoloStatus
     */
    public static class PaoloStatusFilter extends Filter<PaoloStatus> {

        public PaoloStatusFilter() {}

        public PaoloStatusFilter(PaoloStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaoloStatusFilter copy() {
            return new PaoloStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter slug;

    private StringFilter summary;

    private StringFilter contentHeitiga;

    private PaoloStatusFilter publicationStatus;

    private InstantFilter publishedDate;

    private LongFilter avatarId;

    private LongFilter categoryId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlLexFergCriteria() {}

    public AlLexFergCriteria(AlLexFergCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.summary = other.optionalSummary().map(StringFilter::copy).orElse(null);
        this.contentHeitiga = other.optionalContentHeitiga().map(StringFilter::copy).orElse(null);
        this.publicationStatus = other.optionalPublicationStatus().map(PaoloStatusFilter::copy).orElse(null);
        this.publishedDate = other.optionalPublishedDate().map(InstantFilter::copy).orElse(null);
        this.avatarId = other.optionalAvatarId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlLexFergCriteria copy() {
        return new AlLexFergCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
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

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getSummary() {
        return summary;
    }

    public Optional<StringFilter> optionalSummary() {
        return Optional.ofNullable(summary);
    }

    public StringFilter summary() {
        if (summary == null) {
            setSummary(new StringFilter());
        }
        return summary;
    }

    public void setSummary(StringFilter summary) {
        this.summary = summary;
    }

    public StringFilter getContentHeitiga() {
        return contentHeitiga;
    }

    public Optional<StringFilter> optionalContentHeitiga() {
        return Optional.ofNullable(contentHeitiga);
    }

    public StringFilter contentHeitiga() {
        if (contentHeitiga == null) {
            setContentHeitiga(new StringFilter());
        }
        return contentHeitiga;
    }

    public void setContentHeitiga(StringFilter contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public PaoloStatusFilter getPublicationStatus() {
        return publicationStatus;
    }

    public Optional<PaoloStatusFilter> optionalPublicationStatus() {
        return Optional.ofNullable(publicationStatus);
    }

    public PaoloStatusFilter publicationStatus() {
        if (publicationStatus == null) {
            setPublicationStatus(new PaoloStatusFilter());
        }
        return publicationStatus;
    }

    public void setPublicationStatus(PaoloStatusFilter publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public InstantFilter getPublishedDate() {
        return publishedDate;
    }

    public Optional<InstantFilter> optionalPublishedDate() {
        return Optional.ofNullable(publishedDate);
    }

    public InstantFilter publishedDate() {
        if (publishedDate == null) {
            setPublishedDate(new InstantFilter());
        }
        return publishedDate;
    }

    public void setPublishedDate(InstantFilter publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LongFilter getAvatarId() {
        return avatarId;
    }

    public Optional<LongFilter> optionalAvatarId() {
        return Optional.ofNullable(avatarId);
    }

    public LongFilter avatarId() {
        if (avatarId == null) {
            setAvatarId(new LongFilter());
        }
        return avatarId;
    }

    public void setAvatarId(LongFilter avatarId) {
        this.avatarId = avatarId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final AlLexFergCriteria that = (AlLexFergCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(contentHeitiga, that.contentHeitiga) &&
            Objects.equals(publicationStatus, that.publicationStatus) &&
            Objects.equals(publishedDate, that.publishedDate) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            slug,
            summary,
            contentHeitiga,
            publicationStatus,
            publishedDate,
            avatarId,
            categoryId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLexFergCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalSummary().map(f -> "summary=" + f + ", ").orElse("") +
            optionalContentHeitiga().map(f -> "contentHeitiga=" + f + ", ").orElse("") +
            optionalPublicationStatus().map(f -> "publicationStatus=" + f + ", ").orElse("") +
            optionalPublishedDate().map(f -> "publishedDate=" + f + ", ").orElse("") +
            optionalAvatarId().map(f -> "avatarId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}