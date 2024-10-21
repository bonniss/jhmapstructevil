package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlZorroTemptationVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlZorroTemptationViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-zorro-temptation-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlZorroTemptationViCriteria implements Serializable, Criteria {

    /**
     * Class for filtering KnsIction
     */
    public static class KnsIctionFilter extends Filter<KnsIction> {

        public KnsIctionFilter() {}

        public KnsIctionFilter(KnsIctionFilter filter) {
            super(filter);
        }

        @Override
        public KnsIctionFilter copy() {
            return new KnsIctionFilter(this);
        }
    }

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

    private KnsIctionFilter zipAction;

    private StringFilter name;

    private StringFilter templateId;

    private AlphaSourceTypeFilter dataSourceMappingType;

    private StringFilter templateDataMapping;

    private StringFilter targetUrls;

    private LongFilter thumbnailId;

    private UUIDFilter applicationId;

    private Boolean distinct;

    public AlZorroTemptationViCriteria() {}

    public AlZorroTemptationViCriteria(AlZorroTemptationViCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.zipAction = other.optionalZipAction().map(KnsIctionFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.templateId = other.optionalTemplateId().map(StringFilter::copy).orElse(null);
        this.dataSourceMappingType = other.optionalDataSourceMappingType().map(AlphaSourceTypeFilter::copy).orElse(null);
        this.templateDataMapping = other.optionalTemplateDataMapping().map(StringFilter::copy).orElse(null);
        this.targetUrls = other.optionalTargetUrls().map(StringFilter::copy).orElse(null);
        this.thumbnailId = other.optionalThumbnailId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlZorroTemptationViCriteria copy() {
        return new AlZorroTemptationViCriteria(this);
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

    public KnsIctionFilter getZipAction() {
        return zipAction;
    }

    public Optional<KnsIctionFilter> optionalZipAction() {
        return Optional.ofNullable(zipAction);
    }

    public KnsIctionFilter zipAction() {
        if (zipAction == null) {
            setZipAction(new KnsIctionFilter());
        }
        return zipAction;
    }

    public void setZipAction(KnsIctionFilter zipAction) {
        this.zipAction = zipAction;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getTemplateId() {
        return templateId;
    }

    public Optional<StringFilter> optionalTemplateId() {
        return Optional.ofNullable(templateId);
    }

    public StringFilter templateId() {
        if (templateId == null) {
            setTemplateId(new StringFilter());
        }
        return templateId;
    }

    public void setTemplateId(StringFilter templateId) {
        this.templateId = templateId;
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

    public StringFilter getTemplateDataMapping() {
        return templateDataMapping;
    }

    public Optional<StringFilter> optionalTemplateDataMapping() {
        return Optional.ofNullable(templateDataMapping);
    }

    public StringFilter templateDataMapping() {
        if (templateDataMapping == null) {
            setTemplateDataMapping(new StringFilter());
        }
        return templateDataMapping;
    }

    public void setTemplateDataMapping(StringFilter templateDataMapping) {
        this.templateDataMapping = templateDataMapping;
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
        final AlZorroTemptationViCriteria that = (AlZorroTemptationViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(zipAction, that.zipAction) &&
            Objects.equals(name, that.name) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(dataSourceMappingType, that.dataSourceMappingType) &&
            Objects.equals(templateDataMapping, that.templateDataMapping) &&
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
            zipAction,
            name,
            templateId,
            dataSourceMappingType,
            templateDataMapping,
            targetUrls,
            thumbnailId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlZorroTemptationViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalZipAction().map(f -> "zipAction=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTemplateId().map(f -> "templateId=" + f + ", ").orElse("") +
            optionalDataSourceMappingType().map(f -> "dataSourceMappingType=" + f + ", ").orElse("") +
            optionalTemplateDataMapping().map(f -> "templateDataMapping=" + f + ", ").orElse("") +
            optionalTargetUrls().map(f -> "targetUrls=" + f + ", ").orElse("") +
            optionalThumbnailId().map(f -> "thumbnailId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
