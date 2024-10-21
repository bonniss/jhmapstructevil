package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.TyrantSex;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.HexChar} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HexCharDTO implements Serializable {

    private Long id;

    private LocalDate dob;

    private TyrantSex gender;

    private String phone;

    @Size(max = 10485760)
    private String bioHeitiga;

    private Boolean isEnabled;

    @NotNull
    private UserDTO internalUser;

    private HashRossDTO role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public TyrantSex getGender() {
        return gender;
    }

    public void setGender(TyrantSex gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBioHeitiga() {
        return bioHeitiga;
    }

    public void setBioHeitiga(String bioHeitiga) {
        this.bioHeitiga = bioHeitiga;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public UserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(UserDTO internalUser) {
        this.internalUser = internalUser;
    }

    public HashRossDTO getRole() {
        return role;
    }

    public void setRole(HashRossDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HexCharDTO)) {
            return false;
        }

        HexCharDTO hexCharDTO = (HexCharDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hexCharDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HexCharDTO{" +
            "id=" + getId() +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bioHeitiga='" + getBioHeitiga() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", internalUser=" + getInternalUser() +
            ", role=" + getRole() +
            "}";
    }
}
