package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AndreiRightHand.
 */
@Entity
@Table(name = "andrei_right_hand")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AndreiRightHand implements Serializable {

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
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private AntonioBanderas country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private AntonioBanderas province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private AntonioBanderas district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "current", "parent", "children", "antonioBanderas" }, allowSetters = true)
    private AntonioBanderas ward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AndreiRightHand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return this.details;
    }

    public AndreiRightHand details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Float getLat() {
        return this.lat;
    }

    public AndreiRightHand lat(Float lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return this.lon;
    }

    public AndreiRightHand lon(Float lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public AntonioBanderas getCountry() {
        return this.country;
    }

    public void setCountry(AntonioBanderas antonioBanderas) {
        this.country = antonioBanderas;
    }

    public AndreiRightHand country(AntonioBanderas antonioBanderas) {
        this.setCountry(antonioBanderas);
        return this;
    }

    public AntonioBanderas getProvince() {
        return this.province;
    }

    public void setProvince(AntonioBanderas antonioBanderas) {
        this.province = antonioBanderas;
    }

    public AndreiRightHand province(AntonioBanderas antonioBanderas) {
        this.setProvince(antonioBanderas);
        return this;
    }

    public AntonioBanderas getDistrict() {
        return this.district;
    }

    public void setDistrict(AntonioBanderas antonioBanderas) {
        this.district = antonioBanderas;
    }

    public AndreiRightHand district(AntonioBanderas antonioBanderas) {
        this.setDistrict(antonioBanderas);
        return this;
    }

    public AntonioBanderas getWard() {
        return this.ward;
    }

    public void setWard(AntonioBanderas antonioBanderas) {
        this.ward = antonioBanderas;
    }

    public AndreiRightHand ward(AntonioBanderas antonioBanderas) {
        this.setWard(antonioBanderas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AndreiRightHand)) {
            return false;
        }
        return getId() != null && getId().equals(((AndreiRightHand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AndreiRightHand{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            "}";
    }
}
