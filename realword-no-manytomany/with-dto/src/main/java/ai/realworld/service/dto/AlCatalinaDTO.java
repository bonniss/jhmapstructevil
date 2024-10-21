package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlCatalina} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlCatalinaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    private Integer treeDepth;

    private AlCatalinaDTO parent;

    private MetaverseDTO avatar;

    private JohnLennonDTO application;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
    }

    public AlCatalinaDTO getParent() {
        return parent;
    }

    public void setParent(AlCatalinaDTO parent) {
        this.parent = parent;
    }

    public MetaverseDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(MetaverseDTO avatar) {
        this.avatar = avatar;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlCatalinaDTO)) {
            return false;
        }

        AlCatalinaDTO alCatalinaDTO = (AlCatalinaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alCatalinaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlCatalinaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", treeDepth=" + getTreeDepth() +
            ", parent=" + getParent() +
            ", avatar=" + getAvatar() +
            ", application=" + getApplication() +
            "}";
    }
}
