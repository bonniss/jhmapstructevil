package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.repository.ReviewThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewTheta}.
 */
@Service
@Transactional
public class ReviewThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewThetaService.class);

    private final ReviewThetaRepository reviewThetaRepository;

    public ReviewThetaService(ReviewThetaRepository reviewThetaRepository) {
        this.reviewThetaRepository = reviewThetaRepository;
    }

    /**
     * Save a reviewTheta.
     *
     * @param reviewTheta the entity to save.
     * @return the persisted entity.
     */
    public ReviewTheta save(ReviewTheta reviewTheta) {
        LOG.debug("Request to save ReviewTheta : {}", reviewTheta);
        return reviewThetaRepository.save(reviewTheta);
    }

    /**
     * Update a reviewTheta.
     *
     * @param reviewTheta the entity to save.
     * @return the persisted entity.
     */
    public ReviewTheta update(ReviewTheta reviewTheta) {
        LOG.debug("Request to update ReviewTheta : {}", reviewTheta);
        return reviewThetaRepository.save(reviewTheta);
    }

    /**
     * Partially update a reviewTheta.
     *
     * @param reviewTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewTheta> partialUpdate(ReviewTheta reviewTheta) {
        LOG.debug("Request to partially update ReviewTheta : {}", reviewTheta);

        return reviewThetaRepository
            .findById(reviewTheta.getId())
            .map(existingReviewTheta -> {
                if (reviewTheta.getRating() != null) {
                    existingReviewTheta.setRating(reviewTheta.getRating());
                }
                if (reviewTheta.getComment() != null) {
                    existingReviewTheta.setComment(reviewTheta.getComment());
                }
                if (reviewTheta.getReviewDate() != null) {
                    existingReviewTheta.setReviewDate(reviewTheta.getReviewDate());
                }

                return existingReviewTheta;
            })
            .map(reviewThetaRepository::save);
    }

    /**
     * Get all the reviewThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewTheta> findAllWithEagerRelationships(Pageable pageable) {
        return reviewThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewTheta> findOne(Long id) {
        LOG.debug("Request to get ReviewTheta : {}", id);
        return reviewThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewTheta : {}", id);
        reviewThetaRepository.deleteById(id);
    }
}
