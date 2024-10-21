package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppZnsTemplate.
 */
@Entity
@Table(name = "app_zns_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppZnsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "zns_action")
    private KnsIction znsAction;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "template_id")
    private String templateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_source_mapping_type")
    private AlphaSourceType dataSourceMappingType;

    @Column(name = "template_data_mapping")
    private String templateDataMapping;

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

    public AppZnsTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnsIction getZnsAction() {
        return this.znsAction;
    }

    public AppZnsTemplate znsAction(KnsIction znsAction) {
        this.setZnsAction(znsAction);
        return this;
    }

    public void setZnsAction(KnsIction znsAction) {
        this.znsAction = znsAction;
    }

    public String getName() {
        return this.name;
    }

    public AppZnsTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public AppZnsTemplate templateId(String templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return this.dataSourceMappingType;
    }

    public AppZnsTemplate dataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.setDataSourceMappingType(dataSourceMappingType);
        return this;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getTemplateDataMapping() {
        return this.templateDataMapping;
    }

    public AppZnsTemplate templateDataMapping(String templateDataMapping) {
        this.setTemplateDataMapping(templateDataMapping);
        return this;
    }

    public void setTemplateDataMapping(String templateDataMapping) {
        this.templateDataMapping = templateDataMapping;
    }

    public String getTargetUrls() {
        return this.targetUrls;
    }

    public AppZnsTemplate targetUrls(String targetUrls) {
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

    public AppZnsTemplate thumbnail(Metaverse metaverse) {
        this.setThumbnail(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AppZnsTemplate application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppZnsTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((AppZnsTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppZnsTemplate{" +
            "id=" + getId() +
            ", znsAction='" + getZnsAction() + "'" +
            ", name='" + getName() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", templateDataMapping='" + getTemplateDataMapping() + "'" +
            ", targetUrls='" + getTargetUrls() + "'" +
            "}";
    }
}
