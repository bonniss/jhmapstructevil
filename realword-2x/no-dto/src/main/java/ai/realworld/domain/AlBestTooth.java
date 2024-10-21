package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlBestTooth.
 */
@Entity
@Table(name = "al_best_tooth")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBestTooth implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avatar", "category", "application", "tags" }, allowSetters = true)
    private Set<AlLexFerg> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlBestTooth id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlBestTooth name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlBestTooth application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlLexFerg> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<AlLexFerg> alLexFergs) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.removeTag(this));
        }
        if (alLexFergs != null) {
            alLexFergs.forEach(i -> i.addTag(this));
        }
        this.articles = alLexFergs;
    }

    public AlBestTooth articles(Set<AlLexFerg> alLexFergs) {
        this.setArticles(alLexFergs);
        return this;
    }

    public AlBestTooth addArticle(AlLexFerg alLexFerg) {
        this.articles.add(alLexFerg);
        alLexFerg.getTags().add(this);
        return this;
    }

    public AlBestTooth removeArticle(AlLexFerg alLexFerg) {
        this.articles.remove(alLexFerg);
        alLexFerg.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBestTooth)) {
            return false;
        }
        return getId() != null && getId().equals(((AlBestTooth) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBestTooth{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
