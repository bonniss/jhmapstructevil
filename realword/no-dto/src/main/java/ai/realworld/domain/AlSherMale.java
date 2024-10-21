package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlphaSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlSherMale.
 */
@Entity
@Table(name = "al_sher_male")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlSherMale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_source_mapping_type")
    private AlphaSourceType dataSourceMappingType;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "property")
    private String property;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlSherMale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlphaSourceType getDataSourceMappingType() {
        return this.dataSourceMappingType;
    }

    public AlSherMale dataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.setDataSourceMappingType(dataSourceMappingType);
        return this;
    }

    public void setDataSourceMappingType(AlphaSourceType dataSourceMappingType) {
        this.dataSourceMappingType = dataSourceMappingType;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public AlSherMale keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProperty() {
        return this.property;
    }

    public AlSherMale property(String property) {
        this.setProperty(property);
        return this;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTitle() {
        return this.title;
    }

    public AlSherMale title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlSherMale application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlSherMale)) {
            return false;
        }
        return getId() != null && getId().equals(((AlSherMale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlSherMale{" +
            "id=" + getId() +
            ", dataSourceMappingType='" + getDataSourceMappingType() + "'" +
            ", keyword='" + getKeyword() + "'" +
            ", property='" + getProperty() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
