package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Review;
import xyz.jhmapstruct.repository.ReviewRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Review}.
 */
@Service
@Transactional
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Save a review.
     *
     * @param review the entity to save.
     * @return the persisted entity.
     */
    public Review save(Review review) {
        LOG.debug("Request to save Review : {}", review);
        return reviewRepository.save(review);
    }

    /**
     * Update a review.
     *
     * @param review the entity to save.
     * @return the persisted entity.
     */
    public Review update(Review review) {
        LOG.debug("Request to update Review : {}", review);
        return reviewRepository.save(review);
    }

    /**
     * Partially update a review.
     *
     * @param review the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Review> partialUpdate(Review review) {
        LOG.debug("Request to partially update Review : {}", review);

        return reviewRepository
            .findById(review.getId())
            .map(existingReview -> {
                if (review.getRating() != null) {
                    existingReview.setRating(review.getRating());
                }
                if (review.getComment() != null) {
                    existingReview.setComment(review.getComment());
                }
                if (review.getReviewDate() != null) {
                    existingReview.setReviewDate(review.getReviewDate());
                }

                return existingReview;
            })
            .map(reviewRepository::save);
    }

    /**
     * Get all the reviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Review> findAllWithEagerRelationships(Pageable pageable) {
        return reviewRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one review by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Review> findOne(Long id) {
        LOG.debug("Request to get Review : {}", id);
        return reviewRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the review by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
    }
}
