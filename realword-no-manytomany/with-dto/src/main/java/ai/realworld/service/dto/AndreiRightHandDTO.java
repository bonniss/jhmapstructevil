package ai.realworld.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AndreiRightHand} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AndreiRightHandDTO implements Serializable {

    private Long id;

    private String details;

    private Float lat;

    private Float lon;

    private AntonioBanderasDTO country;

    private AntonioBanderasDTO province;

    private AntonioBanderasDTO district;

    private AntonioBanderasDTO ward;

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

    public AntonioBanderasDTO getCountry() {
        return country;
    }

    public void setCountry(AntonioBanderasDTO country) {
        this.country = country;
    }

    public AntonioBanderasDTO getProvince() {
        return province;
    }

    public void setProvince(AntonioBanderasDTO province) {
        this.province = province;
    }

    public AntonioBanderasDTO getDistrict() {
        return district;
    }

    public void setDistrict(AntonioBanderasDTO district) {
        this.district = district;
    }

    public AntonioBanderasDTO getWard() {
        return ward;
    }

    public void setWard(AntonioBanderasDTO ward) {
        this.ward = ward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AndreiRightHandDTO)) {
            return false;
        }

        AndreiRightHandDTO andreiRightHandDTO = (AndreiRightHandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, andreiRightHandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AndreiRightHandDTO{" +
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
