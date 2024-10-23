package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.repository.ReviewSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewSigma}.
 */
@Service
@Transactional
public class ReviewSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewSigmaService.class);

    private final ReviewSigmaRepository reviewSigmaRepository;

    public ReviewSigmaService(ReviewSigmaRepository reviewSigmaRepository) {
        this.reviewSigmaRepository = reviewSigmaRepository;
    }

    /**
     * Save a reviewSigma.
     *
     * @param reviewSigma the entity to save.
     * @return the persisted entity.
     */
    public ReviewSigma save(ReviewSigma reviewSigma) {
        LOG.debug("Request to save ReviewSigma : {}", reviewSigma);
        return reviewSigmaRepository.save(reviewSigma);
    }

    /**
     * Update a reviewSigma.
     *
     * @param reviewSigma the entity to save.
     * @return the persisted entity.
     */
    public ReviewSigma update(ReviewSigma reviewSigma) {
        LOG.debug("Request to update ReviewSigma : {}", reviewSigma);
        return reviewSigmaRepository.save(reviewSigma);
    }

    /**
     * Partially update a reviewSigma.
     *
     * @param reviewSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewSigma> partialUpdate(ReviewSigma reviewSigma) {
        LOG.debug("Request to partially update ReviewSigma : {}", reviewSigma);

        return reviewSigmaRepository
            .findById(reviewSigma.getId())
            .map(existingReviewSigma -> {
                if (reviewSigma.getRating() != null) {
                    existingReviewSigma.setRating(reviewSigma.getRating());
                }
                if (reviewSigma.getComment() != null) {
                    existingReviewSigma.setComment(reviewSigma.getComment());
                }
                if (reviewSigma.getReviewDate() != null) {
                    existingReviewSigma.setReviewDate(reviewSigma.getReviewDate());
                }

                return existingReviewSigma;
            })
            .map(reviewSigmaRepository::save);
    }

    /**
     * Get all the reviewSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewSigma> findAllWithEagerRelationships(Pageable pageable) {
        return reviewSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewSigma> findOne(Long id) {
        LOG.debug("Request to get ReviewSigma : {}", id);
        return reviewSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewSigma : {}", id);
        reviewSigmaRepository.deleteById(id);
    }
}
