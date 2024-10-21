package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPyuThomasWayneVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuThomasWayneViDTO implements Serializable {

    private Long id;

    private Integer rating;

    @Size(max = 65535)
    private String comment;

    private AlPyuJokerViDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AlPyuJokerViDTO getBooking() {
        return booking;
    }

    public void setBooking(AlPyuJokerViDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPyuThomasWayneViDTO)) {
            return false;
        }

        AlPyuThomasWayneViDTO alPyuThomasWayneViDTO = (AlPyuThomasWayneViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPyuThomasWayneViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuThomasWayneViDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}
