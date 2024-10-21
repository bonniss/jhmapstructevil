package ai.realworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPyuThomasWayneVi.
 */
@Entity
@Table(name = "al_pyu_thomas_wayne_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuThomasWayneVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @Size(max = 65535)
    @Column(name = "comment", length = 65535)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customer", "personInCharge", "application" }, allowSetters = true)
    private AlPyuJokerVi booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlPyuThomasWayneVi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public AlPyuThomasWayneVi rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public AlPyuThomasWayneVi comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AlPyuJokerVi getBooking() {
        return this.booking;
    }

    public void setBooking(AlPyuJokerVi alPyuJokerVi) {
        this.booking = alPyuJokerVi;
    }

    public AlPyuThomasWayneVi booking(AlPyuJokerVi alPyuJokerVi) {
        this.setBooking(alPyuJokerVi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPyuThomasWayneVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPyuThomasWayneVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuThomasWayneVi{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
