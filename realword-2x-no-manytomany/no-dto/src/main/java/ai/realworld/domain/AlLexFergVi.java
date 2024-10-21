package ai.realworld.domain;

import ai.realworld.domain.enumeration.PaoloStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlLexFergVi.
 */
@Entity
@Table(name = "al_lex_ferg_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLexFergVi implements Serializable {

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

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "slug", length = 160, nullable = false)
    private String slug;

    @Size(max = 1000)
    @Column(name = "summary", length = 1000)
    private String summary;

    @Size(max = 10485760)
    @Column(name = "content_heitiga", length = 10485760)
    private String contentHeitiga;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status")
    private PaoloStatus publicationStatus;

    @Column(name = "published_date")
    private Instant publishedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlLexFergVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AlLexFergVi title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public AlLexFergVi slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return this.summary;
    }

    public AlLexFergVi summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentHeitiga() {
        return this.contentHeitiga;
    }

    public AlLexFergVi contentHeitiga(String contentHeitiga) {
        this.setContentHeitiga(contentHeitiga);
        return this;
    }

    public void setContentHeitiga(String contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public PaoloStatus getPublicationStatus() {
        return this.publicationStatus;
    }

    public AlLexFergVi publicationStatus(PaoloStatus publicationStatus) {
        this.setPublicationStatus(publicationStatus);
        return this;
    }

    public void setPublicationStatus(PaoloStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public Instant getPublishedDate() {
        return this.publishedDate;
    }

    public AlLexFergVi publishedDate(Instant publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlLexFergVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlLexFergVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLexFergVi{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", summary='" + getSummary() + "'" +
            ", contentHeitiga='" + getContentHeitiga() + "'" +
            ", publicationStatus='" + getPublicationStatus() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            "}";
    }
}
