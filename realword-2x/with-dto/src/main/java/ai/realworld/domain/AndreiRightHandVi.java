package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AndreiRightHandVi.
 */
@Entity
@Table(name = "andrei_right_hand_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AndreiRightHandVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "details")
    private String details;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private AntonioBanderasVi country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private AntonioBanderasVi province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private AntonioBanderasVi district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderasVi" }, allowSetters = true)
    private AntonioBanderasVi ward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AndreiRightHandVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return this.details;
    }

    public AndreiRightHandVi details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Float getLat() {
        return this.lat;
    }

    public AndreiRightHandVi lat(Float lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return this.lon;
    }

    public AndreiRightHandVi lon(Float lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public AntonioBanderasVi getCountry() {
        return this.country;
    }

    public void setCountry(AntonioBanderasVi antonioBanderasVi) {
        this.country = antonioBanderasVi;
    }

    public AndreiRightHandVi country(AntonioBanderasVi antonioBanderasVi) {
        this.setCountry(antonioBanderasVi);
        return this;
    }

    public AntonioBanderasVi getProvince() {
        return this.province;
    }

    public void setProvince(AntonioBanderasVi antonioBanderasVi) {
        this.province = antonioBanderasVi;
    }

    public AndreiRightHandVi province(AntonioBanderasVi antonioBanderasVi) {
        this.setProvince(antonioBanderasVi);
        return this;
    }

    public AntonioBanderasVi getDistrict() {
        return this.district;
    }

    public void setDistrict(AntonioBanderasVi antonioBanderasVi) {
        this.district = antonioBanderasVi;
    }

    public AndreiRightHandVi district(AntonioBanderasVi antonioBanderasVi) {
        this.setDistrict(antonioBanderasVi);
        return this;
    }

    public AntonioBanderasVi getWard() {
        return this.ward;
    }

    public void setWard(AntonioBanderasVi antonioBanderasVi) {
        this.ward = antonioBanderasVi;
    }

    public AndreiRightHandVi ward(AntonioBanderasVi antonioBanderasVi) {
        this.setWard(antonioBanderasVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AndreiRightHandVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AndreiRightHandVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AndreiRightHandVi{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            "}";
    }
}
