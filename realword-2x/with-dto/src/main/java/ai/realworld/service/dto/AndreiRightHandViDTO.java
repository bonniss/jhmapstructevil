package ai.realworld.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AndreiRightHandVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AndreiRightHandViDTO implements Serializable {

    private Long id;

    private String details;

    private Float lat;

    private Float lon;

    private AntonioBanderasViDTO country;

    private AntonioBanderasViDTO province;

    private AntonioBanderasViDTO district;

    private AntonioBanderasViDTO ward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public AntonioBanderasViDTO getCountry() {
        return country;
    }

    public void setCountry(AntonioBanderasViDTO country) {
        this.country = country;
    }

    public AntonioBanderasViDTO getProvince() {
        return province;
    }

    public void setProvince(AntonioBanderasViDTO province) {
        this.province = province;
    }

    public AntonioBanderasViDTO getDistrict() {
        return district;
    }

    public void setDistrict(AntonioBanderasViDTO district) {
        this.district = district;
    }

    public AntonioBanderasViDTO getWard() {
        return ward;
    }

    public void setWard(AntonioBanderasViDTO ward) {
        this.ward = ward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AndreiRightHandViDTO)) {
            return false;
        }

        AndreiRightHandViDTO andreiRightHandViDTO = (AndreiRightHandViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, andreiRightHandViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AndreiRightHandViDTO{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", country=" + getCountry() +
            ", province=" + getProvince() +
            ", district=" + getDistrict() +
            ", ward=" + getWard() +
            "}";
    }
}
