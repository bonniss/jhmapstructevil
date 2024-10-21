package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlphaSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AllMassageThaiVi.
 */
@Entity
@Table(name = "all_massage_thai_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllMassageThaiVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "top_content")
    private String topContent;

    @Column(name = "content")
    private String content;

    @Column(name = "bottom_content")
    private String bottomContent;

    @Column(name = "prop_title_mapping_jason")
    private String propTitleMappingJason;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_source_mapping_type")
    private AlphaSourceType dataSourceMappingType;

    @Column(name = "target_urls")
    private String targetUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    private Metaverse thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AllMassageThaiVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AllMassageThaiVi title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopContent() {
        return this.topContent;
    }

    public AllMassageThaiVi topContent(String topContent) {
        this.setTopContent(topContent);
        return this;
    }

    public void setTopContent(String topContent) {
        this.topContent = topContent;
    }

    public String getContent() {
        return this.content;
    }

    public AllMassageThaiVi content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBottomContent() {
        return this.bottomContent;
    }

    public AllMassageThaiVi bottomContent(String bottomContent) {
        this.setBottomContent(bottomContent);
        return this;
    }

    public void setBottomContent(String bottomContent) {
        this.bottomContent = bottomContent;
    }

    public String getPropTitleMappingJason() {
        return this.propTitleMappingJason;
    }

    public AllMassageThaiVi propTitleMappingJason(String propTitleMappingJason) {
        this.setPropTitleMappingJason(propTitleMappingJason);
        return this;
    }

    public void setPropTitleMappingJason(String propTitleMappingJason) {
        this.propTitleMappingJason = propTitleMappingJason;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return this.dataSourceMappingType;
    }

    public AllMassageThaiVi dataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.setDataSourceMappingType(dataSourceMappingType);
        return this;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getTargetUrls() {
        return this.targetUrls;
    }

    public AllMassageThaiVi targetUrls(String targetUrls) {
        this.setTargetUrls(targetUrls);
        return this;
    }

    public void setTargetUrls(String targetUrls) {
        this.targetUrls = targetUrls;
    }

    public Metaverse getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Metaverse metaverse) {
        this.thumbnail = metaverse;
    }

    public AllMassageThaiVi thumbnail(Metaverse metaverse) {
        this.setThumbnail(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AllMassageThaiVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllMassageThaiVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AllMassageThaiVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllMassageThaiVi{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", topContent='" + getTopContent() + "'" +
            ", content='" + getContent() + "'" +
            ", bottomContent='" + getBottomContent() + "'" +
            ", propTitleMappingJason='" + getPropTitleMappingJason() + "'" +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", targetUrls='" + getTargetUrls() + "'" +
            "}";
    }
}
