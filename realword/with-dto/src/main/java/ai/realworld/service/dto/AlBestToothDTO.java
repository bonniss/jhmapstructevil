package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ai.realworld.domain.AlBestTooth} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlBestToothDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    private JohnLennonDTO application;

    private Set<AlLexFergDTO> articles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    public Set<AlLexFergDTO> getArticles() {
        return articles;
    }

    public void setArticles(Set<AlLexFergDTO> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlBestToothDTO)) {
            return false;
        }

        AlBestToothDTO alBestToothDTO = (AlBestToothDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alBestToothDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlBestToothDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", application=" + getApplication() +
            ", articles=" + getArticles() +
            "}";
    }
}
