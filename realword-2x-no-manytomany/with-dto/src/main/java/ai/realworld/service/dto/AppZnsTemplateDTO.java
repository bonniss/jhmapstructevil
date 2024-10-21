package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AppZnsTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppZnsTemplateDTO implements Serializable {

    private Long id;

    private KnsIction znsAction;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private String templateId;

    private AlphaSourceType dataSourceMappingType;

    private String templateDataMapping;

    private String targetUrls;

    private MetaverseDTO thumbnail;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnsIction getZnsAction() {
        return znsAction;
    }

    public void setZnsAction(KnsIction znsAction) {
        this.znsAction = znsAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return dataSourceMappingType;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getTemplateDataMapping() {
        return templateDataMapping;
    }

    public void setTemplateDataMapping(String templateDataMapping) {
        this.templateDataMapping = templateDataMapping;
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
        if (!(o instanceof AppZnsTemplateDTO)) {
            return false;
        }

        AppZnsTemplateDTO appZnsTemplateDTO = (AppZnsTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appZnsTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppZnsTemplateDTO{" +
            "id=" + getId() +
            ", znsAction='" + getZnsAction() + "'" +
            ", name='" + getName() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", templateDataMapping='" + getTemplateDataMapping() + "'" +
            ", targetUrls='" + getTargetUrls() + "'" +
            ", thumbnail=" + getThumbnail() +
            ", application=" + getApplication() +
            "}";
    }
}
