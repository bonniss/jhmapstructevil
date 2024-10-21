package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlphaSourceType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlSherMale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlSherMaleDTO implements Serializable {

    private Long id;

    private AlphaSourceType dataSourceMappingType;

    private String keyword;

    private String property;

    private String title;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return dataSourceMappingType;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if (!(o instanceof AlSherMaleDTO)) {
            return false;
        }

        AlSherMaleDTO alSherMaleDTO = (AlSherMaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alSherMaleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlSherMaleDTO{" +
            "id=" + getId() +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", keyword='" + getKeyword() + "'" +
            ", property='" + getProperty() + "'" +
            ", title='" + getTitle() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
