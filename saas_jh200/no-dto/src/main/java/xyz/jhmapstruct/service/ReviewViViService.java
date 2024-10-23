package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
@Service
@Transactional
public class ReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViService.class);

    private final ReviewViViRepository reviewViViRepository;

    public ReviewViViService(ReviewViViRepository reviewViViRepository) {
        this.reviewViViRepository = reviewViViRepository;
    }

    /**
     * Save a reviewViVi.
     *
     * @param reviewViVi the entity to save.
     * @return the persisted entity.
     */
    public ReviewViVi save(ReviewViVi reviewViVi) {
        LOG.debug("Request to save ReviewViVi : {}", reviewViVi);
        return reviewViViRepository.save(reviewViVi);
    }

    /**
     * Update a reviewViVi.
     *
     * @param reviewViVi the entity to save.
     * @return the persisted entity.
     */
    public ReviewViVi update(ReviewViVi reviewViVi) {
        LOG.debug("Request to update ReviewViVi : {}", reviewViVi);
        return reviewViViRepository.save(reviewViVi);
    }

    /**
     * Partially update a reviewViVi.
     *
     * @param reviewViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewViVi> partialUpdate(ReviewViVi reviewViVi) {
        LOG.debug("Request to partially update ReviewViVi : {}", reviewViVi);

        return reviewViViRepository
            .findById(reviewViVi.getId())
            .map(existingReviewViVi -> {
                if (reviewViVi.getRating() != null) {
                    existingReviewViVi.setRating(reviewViVi.getRating());
                }
                if (reviewViVi.getComment() != null) {
                    existingReviewViVi.setComment(reviewViVi.getComment());
                }
                if (reviewViVi.getReviewDate() != null) {
                    existingReviewViVi.setReviewDate(reviewViVi.getReviewDate());
                }

                return existingReviewViVi;
            })
            .map(reviewViViRepository::save);
    }

    /**
     * Get all the reviewViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewViVi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewViVi> findOne(Long id) {
        LOG.debug("Request to get ReviewViVi : {}", id);
        return reviewViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewViVi : {}", id);
        reviewViViRepository.deleteById(id);
    }
}
