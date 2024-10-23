package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.repository.ReviewBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewBeta}.
 */
@Service
@Transactional
public class ReviewBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewBetaService.class);

    private final ReviewBetaRepository reviewBetaRepository;

    public ReviewBetaService(ReviewBetaRepository reviewBetaRepository) {
        this.reviewBetaRepository = reviewBetaRepository;
    }

    /**
     * Save a reviewBeta.
     *
     * @param reviewBeta the entity to save.
     * @return the persisted entity.
     */
    public ReviewBeta save(ReviewBeta reviewBeta) {
        LOG.debug("Request to save ReviewBeta : {}", reviewBeta);
        return reviewBetaRepository.save(reviewBeta);
    }

    /**
     * Update a reviewBeta.
     *
     * @param reviewBeta the entity to save.
     * @return the persisted entity.
     */
    public ReviewBeta update(ReviewBeta reviewBeta) {
        LOG.debug("Request to update ReviewBeta : {}", reviewBeta);
        return reviewBetaRepository.save(reviewBeta);
    }

    /**
     * Partially update a reviewBeta.
     *
     * @param reviewBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewBeta> partialUpdate(ReviewBeta reviewBeta) {
        LOG.debug("Request to partially update ReviewBeta : {}", reviewBeta);

        return reviewBetaRepository
            .findById(reviewBeta.getId())
            .map(existingReviewBeta -> {
                if (reviewBeta.getRating() != null) {
                    existingReviewBeta.setRating(reviewBeta.getRating());
                }
                if (reviewBeta.getComment() != null) {
                    existingReviewBeta.setComment(reviewBeta.getComment());
                }
                if (reviewBeta.getReviewDate() != null) {
                    existingReviewBeta.setReviewDate(reviewBeta.getReviewDate());
                }

                return existingReviewBeta;
            })
            .map(reviewBetaRepository::save);
    }

    /**
     * Get all the reviewBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewBeta> findAllWithEagerRelationships(Pageable pageable) {
        return reviewBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewBeta> findOne(Long id) {
        LOG.debug("Request to get ReviewBeta : {}", id);
        return reviewBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewBeta : {}", id);
        reviewBetaRepository.deleteById(id);
    }
}
