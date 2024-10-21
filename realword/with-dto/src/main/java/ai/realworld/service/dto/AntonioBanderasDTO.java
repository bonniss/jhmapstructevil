package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AntonioBanderas} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntonioBanderasDTO implements Serializable {

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

    private AntonioBanderasDTO current;

    private AntonioBanderasDTO parent;

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

    public AntonioBanderasDTO getCurrent() {
        return current;
    }

    public void setCurrent(AntonioBanderasDTO current) {
        this.current = current;
    }

    public AntonioBanderasDTO getParent() {
        return parent;
    }

    public void setParent(AntonioBanderasDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AntonioBanderasDTO)) {
            return false;
        }

        AntonioBanderasDTO antonioBanderasDTO = (AntonioBanderasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, antonioBanderasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntonioBanderasDTO{" +
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
