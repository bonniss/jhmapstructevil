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
 * A AlZorroTemptation.
 */
@Entity
@Table(name = "al_zorro_temptation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlZorroTemptation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "zip_action")
    private KnsIction zipAction;

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
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Metaverse thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlZorroTemptation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnsIction getZipAction() {
        return this.zipAction;
    }

    public AlZorroTemptation zipAction(KnsIction zipAction) {
        this.setZipAction(zipAction);
        return this;
    }

    public void setZipAction(KnsIction zipAction) {
        this.zipAction = zipAction;
    }

    public String getName() {
        return this.name;
    }

    public AlZorroTemptation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public AlZorroTemptation templateId(String templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return this.dataSourceMappingType;
    }

    public AlZorroTemptation dataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.setDataSourceMappingType(dataSourceMappingType);
        return this;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getTemplateDataMapping() {
        return this.templateDataMapping;
    }

    public AlZorroTemptation templateDataMapping(String templateDataMapping) {
        this.setTemplateDataMapping(templateDataMapping);
        return this;
    }

    public void setTemplateDataMapping(String templateDataMapping) {
        this.templateDataMapping = templateDataMapping;
    }

    public String getTargetUrls() {
        return this.targetUrls;
    }

    public AlZorroTemptation targetUrls(String targetUrls) {
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

    public AlZorroTemptation thumbnail(Metaverse metaverse) {
        this.setThumbnail(metaverse);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlZorroTemptation application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlZorroTemptation)) {
            return false;
        }
        return getId() != null && getId().equals(((AlZorroTemptation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlZorroTemptation{" +
            "id=" + getId() +
            ", zipAction='" + getZipAction() + "'" +
            ", name='" + getName() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", templateDataMapping='" + getTemplateDataMapping() + "'" +
            ", targetUrls='" + getTargetUrls() + "'" +
            "}";
    }
}
