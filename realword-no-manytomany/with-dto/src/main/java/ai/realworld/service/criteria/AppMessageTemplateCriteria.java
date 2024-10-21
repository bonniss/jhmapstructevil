package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlphaSourceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AppMessageTemplate} entity. This class is used
 * in {@link ai.realworld.web.rest.AppMessageTemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-message-templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMessageTemplateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlphaSourceType
     */
    public static class AlphaSourceTypeFilter extends Filter<AlphaSourceType> {

        public AlphaSourceTypeFilter() {}

        public AlphaSourceTypeFilter(AlphaSourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public AlphaSourceTypeFilter copy() {
            return new AlphaSourceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter topContent;

    private StringFilter content;

    private StringFilter bottomContent;

    private StringFilter propTitleMappingJason;

    private AlphaSourceTypeFilter dataSourceMappingType;

    private StringFilter targetUrls;

    private LongFilter thumbnailId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AppMessageTemplateCriteria() {}

    public AppMessageTemplateCriteria(AppMessageTemplateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.topContent = other.optionalTopContent().map(StringFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.bottomContent = other.optionalBottomContent().map(StringFilter::copy).orElse(null);
        this.propTitleMappingJason = other.optionalPropTitleMappingJason().map(StringFilter::copy).orElse(null);
        this.dataSourceMappingType = other.optionalDataSourceMappingType().map(AlphaSourceTypeFilter::copy).orElse(null);
        this.targetUrls = other.optionalTargetUrls().map(StringFilter::copy).orElse(null);
        this.thumbnailId = other.optionalThumbnailId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppMessageTemplateCriteria copy() {
        return new AppMessageTemplateCriteria(this);
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

    public StringFilter getTopContent() {
        return topContent;
    }

    public Optional<StringFilter> optionalTopContent() {
        return Optional.ofNullable(topContent);
    }

    public StringFilter topContent() {
        if (topContent == null) {
            setTopContent(new StringFilter());
        }
        return topContent;
    }

    public void setTopContent(StringFilter topContent) {
        this.topContent = topContent;
    }

    public StringFilter getContent() {
        return content;
    }

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public StringFilter getBottomContent() {
        return bottomContent;
    }

    public Optional<StringFilter> optionalBottomContent() {
        return Optional.ofNullable(bottomContent);
    }

    public StringFilter bottomContent() {
        if (bottomContent == null) {
            setBottomContent(new StringFilter());
        }
        return bottomContent;
    }

    public void setBottomContent(StringFilter bottomContent) {
        this.bottomContent = bottomContent;
    }

    public StringFilter getPropTitleMappingJason() {
        return propTitleMappingJason;
    }

    public Optional<StringFilter> optionalPropTitleMappingJason() {
        return Optional.ofNullable(propTitleMappingJason);
    }

    public StringFilter propTitleMappingJason() {
        if (propTitleMappingJason == null) {
            setPropTitleMappingJason(new StringFilter());
        }
        return propTitleMappingJason;
    }

    public void setPropTitleMappingJason(StringFilter propTitleMappingJason) {
        this.propTitleMappingJason = propTitleMappingJason;
    }

    public AlphaSourceTypeFilter getDataSourceMappingType() {
        return dataSourceMappingType;
    }

    public Optional<AlphaSourceTypeFilter> optionalDataSourceMappingType() {
        return Optional.ofNullable(dataSourceMappingType);
    }

    public AlphaSourceTypeFilter dataSourceMappingType() {
        if (dataSourceMappingType == null) {
            setDataSourceMappingType(new AlphaSourceTypeFilter());
        }
        return dataSourceMappingType;
    }

    public void setDataSourceMappingType(AlphaSourceTypeFilter dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public StringFilter getTargetUrls() {
        return targetUrls;
    }

    public Optional<StringFilter> optionalTargetUrls() {
        return Optional.ofNullable(targetUrls);
    }

    public StringFilter targetUrls() {
        if (targetUrls == null) {
            setTargetUrls(new StringFilter());
        }
        return targetUrls;
    }

    public void setTargetUrls(StringFilter targetUrls) {
        this.targetUrls = targetUrls;
    }

    public LongFilter getThumbnailId() {
        return thumbnailId;
    }

    public Optional<LongFilter> optionalThumbnailId() {
        return Optional.ofNullable(thumbnailId);
    }

    public LongFilter thumbnailId() {
        if (thumbnailId == null) {
            setThumbnailId(new LongFilter());
        }
        return thumbnailId;
    }

    public void setThumbnailId(LongFilter thumbnailId) {
        this.thumbnailId = thumbnailId;
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
        final AppMessageTemplateCriteria that = (AppMessageTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(topContent, that.topContent) &&
            Objects.equals(content, that.content) &&
            Objects.equals(bottomContent, that.bottomContent) &&
            Objects.equals(propTitleMappingJason, that.propTitleMappingJason) &&
            Objects.equals(dataSourceMappingType, that.dataSourceMappingType) &&
            Objects.equals(targetUrls, that.targetUrls) &&
            Objects.equals(thumbnailId, that.thumbnailId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            topContent,
            content,
            bottomContent,
            propTitleMappingJason,
            dataSourceMappingType,
            targetUrls,
            thumbnailId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppMessageTemplateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalTopContent().map(f -> "topContent=" + f + ", ").orElse("") +
            optionalContent().map(f -> "content=" + f + ", ").orElse("") +
            optionalBottomContent().map(f -> "bottomContent=" + f + ", ").orElse("") +
            optionalPropTitleMappingJason().map(f -> "propTitleMappingJason=" + f + ", ").orElse("") +
            optionalDataSourceMappingType().map(f -> "dataSourceMappingType=" + f + ", ").orElse("") +
            optionalTargetUrls().map(f -> "targetUrls=" + f + ", ").orElse("") +
            optionalThumbnailId().map(f -> "thumbnailId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
