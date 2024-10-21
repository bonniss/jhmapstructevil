package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.Metaverse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaverseDTO implements Serializable {

    private Long id;

    private String filename;

    private String contentType;

    private String fileExt;

    private Long fileSize;

    private String fileUrl;

    private String thumbnailUrl;

    private String blurhash;

    private String objectName;

    @Size(max = 10485760)
    private String objectMetaJason;

    private Double urlLifespanInSeconds;

    private Instant urlExpiredDate;

    private Boolean autoRenewUrl;

    private Boolean isEnabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBlurhash() {
        return blurhash;
    }

    public void setBlurhash(String blurhash) {
        this.blurhash = blurhash;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectMetaJason() {
        return objectMetaJason;
    }

    public void setObjectMetaJason(String objectMetaJason) {
        this.objectMetaJason = objectMetaJason;
    }

    public Double getUrlLifespanInSeconds() {
        return urlLifespanInSeconds;
    }

    public void setUrlLifespanInSeconds(Double urlLifespanInSeconds) {
        this.urlLifespanInSeconds = urlLifespanInSeconds;
    }

    public Instant getUrlExpiredDate() {
        return urlExpiredDate;
    }

    public void setUrlExpiredDate(Instant urlExpiredDate) {
        this.urlExpiredDate = urlExpiredDate;
    }

    public Boolean getAutoRenewUrl() {
        return autoRenewUrl;
    }

    public void setAutoRenewUrl(Boolean autoRenewUrl) {
        this.autoRenewUrl = autoRenewUrl;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaverseDTO)) {
            return false;
        }

        MetaverseDTO metaverseDTO = (MetaverseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, metaverseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaverseDTO{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileExt='" + getFileExt() + "'" +
            ", fileSize=" + getFileSize() +
            ", fileUrl='" + getFileUrl() + "'" +
            ", thumbnailUrl='" + getThumbnailUrl() + "'" +
            ", blurhash='" + getBlurhash() + "'" +
            ", objectName='" + getObjectName() + "'" +
            ", objectMetaJason='" + getObjectMetaJason() + "'" +
            ", urlLifespanInSeconds=" + getUrlLifespanInSeconds() +
            ", urlExpiredDate='" + getUrlExpiredDate() + "'" +
            ", autoRenewUrl='" + getAutoRenewUrl() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            "}";
    }
}
