package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlphaSourceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AppMessageTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMessageTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String title;

    private String topContent;

    private String content;

    private String bottomContent;

    private String propTitleMappingJason;

    private AlphaSourceType dataSourceMappingType;

    private String targetUrls;

    private MetaverseDTO thumbnail;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopContent() {
        return topContent;
    }

    public void setTopContent(String topContent) {
        this.topContent = topContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBottomContent() {
        return bottomContent;
    }

    public void setBottomContent(String bottomContent) {
        this.bottomContent = bottomContent;
    }

    public String getPropTitleMappingJason() {
        return propTitleMappingJason;
    }

    public void setPropTitleMappingJason(String propTitleMappingJason) {
        this.propTitleMappingJason = propTitleMappingJason;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return dataSourceMappingType;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getTargetUrls() {
        return targetUrls;
    }

    public void setTargetUrls(String targetUrls) {
        this.targetUrls = targetUrls;
    }

    public MetaverseDTO getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MetaverseDTO thumbnail) {
        this.thumbnail = thumbnail;
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
        if (!(o instanceof AppMessageTemplateDTO)) {
            return false;
        }

        AppMessageTemplateDTO appMessageTemplateDTO = (AppMessageTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appMessageTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppMessageTemplateDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", topContent='" + getTopContent() + "'" +
            ", content='" + getContent() + "'" +
            ", bottomContent='" + getBottomContent() + "'" +
            ", propTitleMappingJason='" + getPropTitleMappingJason() + "'" +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", targetUrls='" + getTargetUrls() + "'" +
            ", thumbnail=" + getThumbnail() +
            ", application=" + getApplication() +
            "}";
    }
}
