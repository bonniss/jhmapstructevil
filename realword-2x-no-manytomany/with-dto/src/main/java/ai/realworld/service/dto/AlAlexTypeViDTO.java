package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlAlexTypeVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAlexTypeViDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 65535)
    private String description;

    private Boolean canDoRetail;

    private Boolean isOrgDivision;

    @Size(max = 10485760)
    private String configJason;

    private Integer treeDepth;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Boolean getCanDoRetail() {
        return canDoRetail;
    }

    public void setCanDoRetail(Boolean canDoRetail) {
        this.canDoRetail = canDoRetail;
    }

    public Boolean getIsOrgDivision() {
        return isOrgDivision;
    }

    public void setIsOrgDivision(Boolean isOrgDivision) {
        this.isOrgDivision = isOrgDivision;
    }

    public String getConfigJason() {
        return configJason;
    }

    public void setConfigJason(String configJason) {
        this.configJason = configJason;
    }

    public Integer getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
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
        if (!(o instanceof AlAlexTypeViDTO)) {
            return false;
        }

        AlAlexTypeViDTO alAlexTypeViDTO = (AlAlexTypeViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alAlexTypeViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAlexTypeViDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", canDoRetail='" + getCanDoRetail() + "'" +
            ", isOrgDivision='" + getIsOrgDivision() + "'" +
            ", configJason='" + getConfigJason() + "'" +
            ", treeDepth=" + getTreeDepth() +
            ", application=" + getApplication() +
            "}";
    }
}
