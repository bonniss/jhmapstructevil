package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.repository.ReviewGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewGamma}.
 */
@Service
@Transactional
public class ReviewGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewGammaService.class);

    private final ReviewGammaRepository reviewGammaRepository;

    public ReviewGammaService(ReviewGammaRepository reviewGammaRepository) {
        this.reviewGammaRepository = reviewGammaRepository;
    }

    /**
     * Save a reviewGamma.
     *
     * @param reviewGamma the entity to save.
     * @return the persisted entity.
     */
    public ReviewGamma save(ReviewGamma reviewGamma) {
        LOG.debug("Request to save ReviewGamma : {}", reviewGamma);
        return reviewGammaRepository.save(reviewGamma);
    }

    /**
     * Update a reviewGamma.
     *
     * @param reviewGamma the entity to save.
     * @return the persisted entity.
     */
    public ReviewGamma update(ReviewGamma reviewGamma) {
        LOG.debug("Request to update ReviewGamma : {}", reviewGamma);
        return reviewGammaRepository.save(reviewGamma);
    }

    /**
     * Partially update a reviewGamma.
     *
     * @param reviewGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewGamma> partialUpdate(ReviewGamma reviewGamma) {
        LOG.debug("Request to partially update ReviewGamma : {}", reviewGamma);

        return reviewGammaRepository
            .findById(reviewGamma.getId())
            .map(existingReviewGamma -> {
                if (reviewGamma.getRating() != null) {
                    existingReviewGamma.setRating(reviewGamma.getRating());
                }
                if (reviewGamma.getComment() != null) {
                    existingReviewGamma.setComment(reviewGamma.getComment());
                }
                if (reviewGamma.getReviewDate() != null) {
                    existingReviewGamma.setReviewDate(reviewGamma.getReviewDate());
                }

                return existingReviewGamma;
            })
            .map(reviewGammaRepository::save);
    }

    /**
     * Get all the reviewGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewGamma> findAllWithEagerRelationships(Pageable pageable) {
        return reviewGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewGamma> findOne(Long id) {
        LOG.debug("Request to get ReviewGamma : {}", id);
        return reviewGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewGamma : {}", id);
        reviewGammaRepository.deleteById(id);
    }
}
