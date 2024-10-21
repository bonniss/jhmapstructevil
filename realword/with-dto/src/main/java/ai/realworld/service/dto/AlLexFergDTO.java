package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.PaoloStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.realworld.domain.AlLexFerg} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLexFergDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String title;

    @NotNull
    @Size(min = 2, max = 160)
    private String slug;

    @Size(max = 1000)
    private String summary;

    @Size(max = 10485760)
    private String contentHeitiga;

    private PaoloStatus publicationStatus;

    private Instant publishedDate;

    private MetaverseDTO avatar;

    private AlCatalinaDTO category;

    private JohnLennonDTO application;

    private Set<AlBestToothDTO> tags = new HashSet<>();

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentHeitiga() {
        return contentHeitiga;
    }

    public void setContentHeitiga(String contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public PaoloStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PaoloStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public MetaverseDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(MetaverseDTO avatar) {
        this.avatar = avatar;
    }

    public AlCatalinaDTO getCategory() {
        return category;
    }

    public void setCategory(AlCatalinaDTO category) {
        this.category = category;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    public Set<AlBestToothDTO> getTags() {
        return tags;
    }

    public void setTags(Set<AlBestToothDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlLexFergDTO)) {
            return false;
        }

        AlLexFergDTO alLexFergDTO = (AlLexFergDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alLexFergDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLexFergDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", summary='" + getSummary() + "'" +
            ", contentHeitiga='" + getContentHeitiga() + "'" +
            ", publicationStatus='" + getPublicationStatus() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", avatar=" + getAvatar() +
            ", category=" + getCategory() +
            ", application=" + getApplication() +
            ", tags=" + getTags() +
            "}";
    }
}
