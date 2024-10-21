package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.Metaverse} entity. This class is used
 * in {@link ai.realworld.web.rest.MetaverseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /metaverses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaverseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter filename;

    private StringFilter contentType;

    private StringFilter fileExt;

    private LongFilter fileSize;

    private StringFilter fileUrl;

    private StringFilter thumbnailUrl;

    private StringFilter blurhash;

    private StringFilter objectName;

    private StringFilter objectMetaJason;

    private DoubleFilter urlLifespanInSeconds;

    private InstantFilter urlExpiredDate;

    private BooleanFilter autoRenewUrl;

    private BooleanFilter isEnabled;

    private Boolean distinct;

    public MetaverseCriteria() {}

    public MetaverseCriteria(MetaverseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.filename = other.optionalFilename().map(StringFilter::copy).orElse(null);
        this.contentType = other.optionalContentType().map(StringFilter::copy).orElse(null);
        this.fileExt = other.optionalFileExt().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.fileUrl = other.optionalFileUrl().map(StringFilter::copy).orElse(null);
        this.thumbnailUrl = other.optionalThumbnailUrl().map(StringFilter::copy).orElse(null);
        this.blurhash = other.optionalBlurhash().map(StringFilter::copy).orElse(null);
        this.objectName = other.optionalObjectName().map(StringFilter::copy).orElse(null);
        this.objectMetaJason = other.optionalObjectMetaJason().map(StringFilter::copy).orElse(null);
        this.urlLifespanInSeconds = other.optionalUrlLifespanInSeconds().map(DoubleFilter::copy).orElse(null);
        this.urlExpiredDate = other.optionalUrlExpiredDate().map(InstantFilter::copy).orElse(null);
        this.autoRenewUrl = other.optionalAutoRenewUrl().map(BooleanFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MetaverseCriteria copy() {
        return new MetaverseCriteria(this);
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

    public StringFilter getFilename() {
        return filename;
    }

    public Optional<StringFilter> optionalFilename() {
        return Optional.ofNullable(filename);
    }

    public StringFilter filename() {
        if (filename == null) {
            setFilename(new StringFilter());
        }
        return filename;
    }

    public void setFilename(StringFilter filename) {
        this.filename = filename;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public Optional<StringFilter> optionalContentType() {
        return Optional.ofNullable(contentType);
    }

    public StringFilter contentType() {
        if (contentType == null) {
            setContentType(new StringFilter());
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public StringFilter getFileExt() {
        return fileExt;
    }

    public Optional<StringFilter> optionalFileExt() {
        return Optional.ofNullable(fileExt);
    }

    public StringFilter fileExt() {
        if (fileExt == null) {
            setFileExt(new StringFilter());
        }
        return fileExt;
    }

    public void setFileExt(StringFilter fileExt) {
        this.fileExt = fileExt;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public Optional<StringFilter> optionalFileUrl() {
        return Optional.ofNullable(fileUrl);
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            setFileUrl(new StringFilter());
        }
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public StringFilter getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Optional<StringFilter> optionalThumbnailUrl() {
        return Optional.ofNullable(thumbnailUrl);
    }

    public StringFilter thumbnailUrl() {
        if (thumbnailUrl == null) {
            setThumbnailUrl(new StringFilter());
        }
        return thumbnailUrl;
    }

    public void setThumbnailUrl(StringFilter thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public StringFilter getBlurhash() {
        return blurhash;
    }

    public Optional<StringFilter> optionalBlurhash() {
        return Optional.ofNullable(blurhash);
    }

    public StringFilter blurhash() {
        if (blurhash == null) {
            setBlurhash(new StringFilter());
        }
        return blurhash;
    }

    public void setBlurhash(StringFilter blurhash) {
        this.blurhash = blurhash;
    }

    public StringFilter getObjectName() {
        return objectName;
    }

    public Optional<StringFilter> optionalObjectName() {
        return Optional.ofNullable(objectName);
    }

    public StringFilter objectName() {
        if (objectName == null) {
            setObjectName(new StringFilter());
        }
        return objectName;
    }

    public void setObjectName(StringFilter objectName) {
        this.objectName = objectName;
    }

    public StringFilter getObjectMetaJason() {
        return objectMetaJason;
    }

    public Optional<StringFilter> optionalObjectMetaJason() {
        return Optional.ofNullable(objectMetaJason);
    }

    public StringFilter objectMetaJason() {
        if (objectMetaJason == null) {
            setObjectMetaJason(new StringFilter());
        }
        return objectMetaJason;
    }

    public void setObjectMetaJason(StringFilter objectMetaJason) {
        this.objectMetaJason = objectMetaJason;
    }

    public DoubleFilter getUrlLifespanInSeconds() {
        return urlLifespanInSeconds;
    }

    public Optional<DoubleFilter> optionalUrlLifespanInSeconds() {
        return Optional.ofNullable(urlLifespanInSeconds);
    }

    public DoubleFilter urlLifespanInSeconds() {
        if (urlLifespanInSeconds == null) {
            setUrlLifespanInSeconds(new DoubleFilter());
        }
        return urlLifespanInSeconds;
    }

    public void setUrlLifespanInSeconds(DoubleFilter urlLifespanInSeconds) {
        this.urlLifespanInSeconds = urlLifespanInSeconds;
    }

    public InstantFilter getUrlExpiredDate() {
        return urlExpiredDate;
    }

    public Optional<InstantFilter> optionalUrlExpiredDate() {
        return Optional.ofNullable(urlExpiredDate);
    }

    public InstantFilter urlExpiredDate() {
        if (urlExpiredDate == null) {
            setUrlExpiredDate(new InstantFilter());
        }
        return urlExpiredDate;
    }

    public void setUrlExpiredDate(InstantFilter urlExpiredDate) {
        this.urlExpiredDate = urlExpiredDate;
    }

    public BooleanFilter getAutoRenewUrl() {
        return autoRenewUrl;
    }

    public Optional<BooleanFilter> optionalAutoRenewUrl() {
        return Optional.ofNullable(autoRenewUrl);
    }

    public BooleanFilter autoRenewUrl() {
        if (autoRenewUrl == null) {
            setAutoRenewUrl(new BooleanFilter());
        }
        return autoRenewUrl;
    }

    public void setAutoRenewUrl(BooleanFilter autoRenewUrl) {
        this.autoRenewUrl = autoRenewUrl;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public Optional<BooleanFilter> optionalIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

    public BooleanFilter isEnabled() {
        if (isEnabled == null) {
            setIsEnabled(new BooleanFilter());
        }
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
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
        final MetaverseCriteria that = (MetaverseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileExt, that.fileExt) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(thumbnailUrl, that.thumbnailUrl) &&
            Objects.equals(blurhash, that.blurhash) &&
            Objects.equals(objectName, that.objectName) &&
            Objects.equals(objectMetaJason, that.objectMetaJason) &&
            Objects.equals(urlLifespanInSeconds, that.urlLifespanInSeconds) &&
            Objects.equals(urlExpiredDate, that.urlExpiredDate) &&
            Objects.equals(autoRenewUrl, that.autoRenewUrl) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            filename,
            contentType,
            fileExt,
            fileSize,
            fileUrl,
            thumbnailUrl,
            blurhash,
            objectName,
            objectMetaJason,
            urlLifespanInSeconds,
            urlExpiredDate,
            autoRenewUrl,
            isEnabled,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaverseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFilename().map(f -> "filename=" + f + ", ").orElse("") +
            optionalContentType().map(f -> "contentType=" + f + ", ").orElse("") +
            optionalFileExt().map(f -> "fileExt=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalFileUrl().map(f -> "fileUrl=" + f + ", ").orElse("") +
            optionalThumbnailUrl().map(f -> "thumbnailUrl=" + f + ", ").orElse("") +
            optionalBlurhash().map(f -> "blurhash=" + f + ", ").orElse("") +
            optionalObjectName().map(f -> "objectName=" + f + ", ").orElse("") +
            optionalObjectMetaJason().map(f -> "objectMetaJason=" + f + ", ").orElse("") +
            optionalUrlLifespanInSeconds().map(f -> "urlLifespanInSeconds=" + f + ", ").orElse("") +
            optionalUrlExpiredDate().map(f -> "urlExpiredDate=" + f + ", ").orElse("") +
            optionalAutoRenewUrl().map(f -> "autoRenewUrl=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
