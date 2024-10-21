package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AntonioBanderasVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntonioBanderasViDTO implements Serializable {

    private Long id;

    private Integer level;

    @NotNull
    @Size(min = 2, max = 160)
    private String code;

    private String name;

    private String fullName;

    private String nativeName;

    private String officialCode;

    private String divisionTerm;

    private Boolean isDeleted;

    private AntonioBanderasViDTO current;

    private AntonioBanderasViDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getOfficialCode() {
        return officialCode;
    }

    public void setOfficialCode(String officialCode) {
        this.officialCode = officialCode;
    }

    public String getDivisionTerm() {
        return divisionTerm;
    }

    public void setDivisionTerm(String divisionTerm) {
        this.divisionTerm = divisionTerm;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public AntonioBanderasViDTO getCurrent() {
        return current;
    }

    public void setCurrent(AntonioBanderasViDTO current) {
        this.current = current;
    }

    public AntonioBanderasViDTO getParent() {
        return parent;
    }

    public void setParent(AntonioBanderasViDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AntonioBanderasViDTO)) {
            return false;
        }

        AntonioBanderasViDTO antonioBanderasViDTO = (AntonioBanderasViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, antonioBanderasViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntonioBanderasViDTO{" +
            "id=" + getId() +
            ", level=" + getLevel() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", nativeName='" + getNativeName() + "'" +
            ", officialCode='" + getOfficialCode() + "'" +
            ", divisionTerm='" + getDivisionTerm() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", current=" + getCurrent() +
            ", parent=" + getParent() +
            "}";
    }
}
