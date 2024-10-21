package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Review;
import xyz.jhmapstruct.repository.ReviewRepository;
import xyz.jhmapstruct.service.ReviewService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Review}.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review save(Review review) {
        LOG.debug("Request to save Review : {}", review);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        LOG.debug("Request to update Review : {}", review);
        return reviewRepository.save(review);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAll() {
        LOG.debug("Request to get all Reviews");
        return reviewRepository.findAll();
    }

    public Page<Review> findAllWithEagerRelationships(Pageable pageable) {
        return reviewRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findOne(Long id) {
        LOG.debug("Request to get Review : {}", id);
        return reviewRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
    }
}
