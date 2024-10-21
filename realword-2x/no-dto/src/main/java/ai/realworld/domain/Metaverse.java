package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Metaverse.
 */
@Entity
@Table(name = "metaverse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Metaverse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "blurhash")
    private String blurhash;

    @Column(name = "object_name")
    private String objectName;

    @Size(max = 10485760)
    @Column(name = "object_meta_jason", length = 10485760)
    private String objectMetaJason;

    @Column(name = "url_lifespan_in_seconds")
    private Double urlLifespanInSeconds;

    @Column(name = "url_expired_date")
    private Instant urlExpiredDate;

    @Column(name = "auto_renew_url")
    private Boolean autoRenewUrl;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "amenities", "images", "children" }, allowSetters = true)
    private Set<AlProPro> alProPros = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "operator", "propertyProfile", "avatar", "application", "images", "children", "bookings" },
        allowSetters = true
    )
    private Set<AlProty> alProties = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "project", "avatar", "application", "amenities", "images", "children" }, allowSetters = true)
    private Set<AlProProVi> alProProVis = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "operator", "propertyProfile", "avatar", "application", "images", "children", "bookings" },
        allowSetters = true
    )
    private Set<AlProtyVi> alProtyVis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Metaverse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return this.filename;
    }

    public Metaverse filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Metaverse contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileExt() {
        return this.fileExt;
    }

    public Metaverse fileExt(String fileExt) {
        this.setFileExt(fileExt);
        return this;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public Metaverse fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public Metaverse fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public Metaverse thumbnailUrl(String thumbnailUrl) {
        this.setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBlurhash() {
        return this.blurhash;
    }

    public Metaverse blurhash(String blurhash) {
        this.setBlurhash(blurhash);
        return this;
    }

    public void setBlurhash(String blurhash) {
        this.blurhash = blurhash;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public Metaverse objectName(String objectName) {
        this.setObjectName(objectName);
        return this;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectMetaJason() {
        return this.objectMetaJason;
    }

    public Metaverse objectMetaJason(String objectMetaJason) {
        this.setObjectMetaJason(objectMetaJason);
        return this;
    }

    public void setObjectMetaJason(String objectMetaJason) {
        this.objectMetaJason = objectMetaJason;
    }

    public Double getUrlLifespanInSeconds() {
        return this.urlLifespanInSeconds;
    }

    public Metaverse urlLifespanInSeconds(Double urlLifespanInSeconds) {
        this.setUrlLifespanInSeconds(urlLifespanInSeconds);
        return this;
    }

    public void setUrlLifespanInSeconds(Double urlLifespanInSeconds) {
        this.urlLifespanInSeconds = urlLifespanInSeconds;
    }

    public Instant getUrlExpiredDate() {
        return this.urlExpiredDate;
    }

    public Metaverse urlExpiredDate(Instant urlExpiredDate) {
        this.setUrlExpiredDate(urlExpiredDate);
        return this;
    }

    public void setUrlExpiredDate(Instant urlExpiredDate) {
        this.urlExpiredDate = urlExpiredDate;
    }

    public Boolean getAutoRenewUrl() {
        return this.autoRenewUrl;
    }

    public Metaverse autoRenewUrl(Boolean autoRenewUrl) {
        this.setAutoRenewUrl(autoRenewUrl);
        return this;
    }

    public void setAutoRenewUrl(Boolean autoRenewUrl) {
        this.autoRenewUrl = autoRenewUrl;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public Metaverse isEnabled(Boolean isEnabled) {
        this.setIsEnabled(isEnabled);
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Set<AlProPro> getAlProPros() {
        return this.alProPros;
    }

    public void setAlProPros(Set<AlProPro> alProPros) {
        if (this.alProPros != null) {
            this.alProPros.forEach(i -> i.removeImage(this));
        }
        if (alProPros != null) {
            alProPros.forEach(i -> i.addImage(this));
        }
        this.alProPros = alProPros;
    }

    public Metaverse alProPros(Set<AlProPro> alProPros) {
        this.setAlProPros(alProPros);
        return this;
    }

    public Metaverse addAlProPro(AlProPro alProPro) {
        this.alProPros.add(alProPro);
        alProPro.getImages().add(this);
        return this;
    }

    public Metaverse removeAlProPro(AlProPro alProPro) {
        this.alProPros.remove(alProPro);
        alProPro.getImages().remove(this);
        return this;
    }

    public Set<AlProty> getAlProties() {
        return this.alProties;
    }

    public void setAlProties(Set<AlProty> alProties) {
        if (this.alProties != null) {
            this.alProties.forEach(i -> i.removeImage(this));
        }
        if (alProties != null) {
            alProties.forEach(i -> i.addImage(this));
        }
        this.alProties = alProties;
    }

    public Metaverse alProties(Set<AlProty> alProties) {
        this.setAlProties(alProties);
        return this;
    }

    public Metaverse addAlProty(AlProty alProty) {
        this.alProties.add(alProty);
        alProty.getImages().add(this);
        return this;
    }

    public Metaverse removeAlProty(AlProty alProty) {
        this.alProties.remove(alProty);
        alProty.getImages().remove(this);
        return this;
    }

    public Set<AlProProVi> getAlProProVis() {
        return this.alProProVis;
    }

    public void setAlProProVis(Set<AlProProVi> alProProVis) {
        if (this.alProProVis != null) {
            this.alProProVis.forEach(i -> i.removeImage(this));
        }
        if (alProProVis != null) {
            alProProVis.forEach(i -> i.addImage(this));
        }
        this.alProProVis = alProProVis;
    }

    public Metaverse alProProVis(Set<AlProProVi> alProProVis) {
        this.setAlProProVis(alProProVis);
        return this;
    }

    public Metaverse addAlProProVi(AlProProVi alProProVi) {
        this.alProProVis.add(alProProVi);
        alProProVi.getImages().add(this);
        return this;
    }

    public Metaverse removeAlProProVi(AlProProVi alProProVi) {
        this.alProProVis.remove(alProProVi);
        alProProVi.getImages().remove(this);
        return this;
    }

    public Set<AlProtyVi> getAlProtyVis() {
        return this.alProtyVis;
    }

    public void setAlProtyVis(Set<AlProtyVi> alProtyVis) {
        if (this.alProtyVis != null) {
            this.alProtyVis.forEach(i -> i.removeImage(this));
        }
        if (alProtyVis != null) {
            alProtyVis.forEach(i -> i.addImage(this));
        }
        this.alProtyVis = alProtyVis;
    }

    public Metaverse alProtyVis(Set<AlProtyVi> alProtyVis) {
        this.setAlProtyVis(alProtyVis);
        return this;
    }

    public Metaverse addAlProtyVi(AlProtyVi alProtyVi) {
        this.alProtyVis.add(alProtyVi);
        alProtyVi.getImages().add(this);
        return this;
    }

    public Metaverse removeAlProtyVi(AlProtyVi alProtyVi) {
        this.alProtyVis.remove(alProtyVi);
        alProtyVi.getImages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metaverse)) {
            return false;
        }
        return getId() != null && getId().equals(((Metaverse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Metaverse{" +
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
