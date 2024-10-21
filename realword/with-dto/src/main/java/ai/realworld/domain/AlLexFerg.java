package ai.realworld.domain;

import ai.realworld.domain.enumeration.PaoloStatus;
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
 * A AlLexFerg.
 */
@Entity
@Table(name = "al_lex_ferg")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLexFerg implements Serializable {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties" }, allowSetters = true)
    private Metaverse avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "avatar", "application", "children" }, allowSetters = true)
    private AlCatalina category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_al_lex_ferg__tag",
        joinColumns = @JoinColumn(name = "al_lex_ferg_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "application", "articles" }, allowSetters = true)
    private Set<AlBestTooth> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlLexFerg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AlLexFerg title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public AlLexFerg slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return this.summary;
    }

    public AlLexFerg summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentHeitiga() {
        return this.contentHeitiga;
    }

    public AlLexFerg contentHeitiga(String contentHeitiga) {
        this.setContentHeitiga(contentHeitiga);
        return this;
    }

    public void setContentHeitiga(String contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public PaoloStatus getPublicationStatus() {
        return this.publicationStatus;
    }

    public AlLexFerg publicationStatus(PaoloStatus publicationStatus) {
        this.setPublicationStatus(publicationStatus);
        return this;
    }

    public void setPublicationStatus(PaoloStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public Instant getPublishedDate() {
        return this.publishedDate;
    }

    public AlLexFerg publishedDate(Instant publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Metaverse getAvatar() {
        return this.avatar;
    }

    public void setAvatar(Metaverse metaverse) {
        this.avatar = metaverse;
    }

    public AlLexFerg avatar(Metaverse metaverse) {
        this.setAvatar(metaverse);
        return this;
    }

    public AlCatalina getCategory() {
        return this.category;
    }

    public void setCategory(AlCatalina alCatalina) {
        this.category = alCatalina;
    }

    public AlLexFerg category(AlCatalina alCatalina) {
        this.setCategory(alCatalina);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlLexFerg application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlBestTooth> getTags() {
        return this.tags;
    }

    public void setTags(Set<AlBestTooth> alBestTooths) {
        this.tags = alBestTooths;
    }

    public AlLexFerg tags(Set<AlBestTooth> alBestTooths) {
        this.setTags(alBestTooths);
        return this;
    }

    public AlLexFerg addTag(AlBestTooth alBestTooth) {
        this.tags.add(alBestTooth);
        return this;
    }

    public AlLexFerg removeTag(AlBestTooth alBestTooth) {
        this.tags.remove(alBestTooth);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlLexFerg)) {
            return false;
        }
        return getId() != null && getId().equals(((AlLexFerg) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLexFerg{" +
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
