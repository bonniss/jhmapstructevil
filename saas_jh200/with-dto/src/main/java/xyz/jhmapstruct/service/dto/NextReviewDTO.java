package xyz.jhmapstruct.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link xyz.jhmapstruct.domain.NextReview} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextReviewDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    @Lob
    private String comment;

    @NotNull
    private Instant reviewDate;

    private NextProductDTO product;

    private MasterTenantDTO tenant;

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

    public Instant getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    public NextProductDTO getProduct() {
        return product;
    }

    public void setProduct(NextProductDTO product) {
        this.product = product;
    }

    public MasterTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(MasterTenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextReviewDTO)) {
            return false;
        }

        NextReviewDTO nextReviewDTO = (NextReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextReviewDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", product=" + getProduct() +
            ", tenant=" + getTenant() +
            "}";
    }
}
