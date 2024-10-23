package xyz.jhmapstruct.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReviewSigma.
 */
@Entity
@Table(name = "review_sigma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewSigma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "review_date", nullable = false)
    private Instant reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category", "tenant", "order", "suppliers" }, allowSetters = true)
    private ProductSigma product;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterTenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReviewSigma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public ReviewSigma rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public ReviewSigma comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getReviewDate() {
        return this.reviewDate;
    }

    public ReviewSigma reviewDate(Instant reviewDate) {
        this.setReviewDate(reviewDate);
        return this;
    }

    public void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    public ProductSigma getProduct() {
        return this.product;
    }

    public void setProduct(ProductSigma productSigma) {
        this.product = productSigma;
    }

    public ReviewSigma product(ProductSigma productSigma) {
        this.setProduct(productSigma);
        return this;
    }

    public MasterTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(MasterTenant masterTenant) {
        this.tenant = masterTenant;
    }

    public ReviewSigma tenant(MasterTenant masterTenant) {
        this.setTenant(masterTenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReviewSigma)) {
            return false;
        }
        return getId() != null && getId().equals(((ReviewSigma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewSigma{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            "}";
    }
}
