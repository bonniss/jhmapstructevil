package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewAlpha;
import xyz.jhmapstruct.repository.ReviewAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewAlpha}.
 */
@Service
@Transactional
public class ReviewAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewAlphaService.class);

    private final ReviewAlphaRepository reviewAlphaRepository;

    public ReviewAlphaService(ReviewAlphaRepository reviewAlphaRepository) {
        this.reviewAlphaRepository = reviewAlphaRepository;
    }

    /**
     * Save a reviewAlpha.
     *
     * @param reviewAlpha the entity to save.
     * @return the persisted entity.
     */
    public ReviewAlpha save(ReviewAlpha reviewAlpha) {
        LOG.debug("Request to save ReviewAlpha : {}", reviewAlpha);
        return reviewAlphaRepository.save(reviewAlpha);
    }

    /**
     * Update a reviewAlpha.
     *
     * @param reviewAlpha the entity to save.
     * @return the persisted entity.
     */
    public ReviewAlpha update(ReviewAlpha reviewAlpha) {
        LOG.debug("Request to update ReviewAlpha : {}", reviewAlpha);
        return reviewAlphaRepository.save(reviewAlpha);
    }

    /**
     * Partially update a reviewAlpha.
     *
     * @param reviewAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewAlpha> partialUpdate(ReviewAlpha reviewAlpha) {
        LOG.debug("Request to partially update ReviewAlpha : {}", reviewAlpha);

        return reviewAlphaRepository
            .findById(reviewAlpha.getId())
            .map(existingReviewAlpha -> {
                if (reviewAlpha.getRating() != null) {
                    existingReviewAlpha.setRating(reviewAlpha.getRating());
                }
                if (reviewAlpha.getComment() != null) {
                    existingReviewAlpha.setComment(reviewAlpha.getComment());
                }
                if (reviewAlpha.getReviewDate() != null) {
                    existingReviewAlpha.setReviewDate(reviewAlpha.getReviewDate());
                }

                return existingReviewAlpha;
            })
            .map(reviewAlphaRepository::save);
    }

    /**
     * Get all the reviewAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return reviewAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewAlpha> findOne(Long id) {
        LOG.debug("Request to get ReviewAlpha : {}", id);
        return reviewAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewAlpha : {}", id);
        reviewAlphaRepository.deleteById(id);
    }
}
