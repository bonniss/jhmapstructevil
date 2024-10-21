package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlCatalinaVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlCatalinaViDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    private Integer treeDepth;

    private AlCatalinaViDTO parent;

    private MetaverseDTO avatar;

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

    public AlCatalinaViDTO getParent() {
        return parent;
    }

    public void setParent(AlCatalinaViDTO parent) {
        this.parent = parent;
    }

    public MetaverseDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(MetaverseDTO avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlCatalinaViDTO)) {
            return false;
        }

        AlCatalinaViDTO alCatalinaViDTO = (AlCatalinaViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alCatalinaViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlCatalinaViDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", treeDepth=" + getTreeDepth() +
            ", parent=" + getParent() +
            ", avatar=" + getAvatar() +
            "}";
    }
}
