package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
@Service
@Transactional
public class ReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViService.class);

    private final ReviewViRepository reviewViRepository;

    public ReviewViService(ReviewViRepository reviewViRepository) {
        this.reviewViRepository = reviewViRepository;
    }

    /**
     * Save a reviewVi.
     *
     * @param reviewVi the entity to save.
     * @return the persisted entity.
     */
    public ReviewVi save(ReviewVi reviewVi) {
        LOG.debug("Request to save ReviewVi : {}", reviewVi);
        return reviewViRepository.save(reviewVi);
    }

    /**
     * Update a reviewVi.
     *
     * @param reviewVi the entity to save.
     * @return the persisted entity.
     */
    public ReviewVi update(ReviewVi reviewVi) {
        LOG.debug("Request to update ReviewVi : {}", reviewVi);
        return reviewViRepository.save(reviewVi);
    }

    /**
     * Partially update a reviewVi.
     *
     * @param reviewVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewVi> partialUpdate(ReviewVi reviewVi) {
        LOG.debug("Request to partially update ReviewVi : {}", reviewVi);

        return reviewViRepository
            .findById(reviewVi.getId())
            .map(existingReviewVi -> {
                if (reviewVi.getRating() != null) {
                    existingReviewVi.setRating(reviewVi.getRating());
                }
                if (reviewVi.getComment() != null) {
                    existingReviewVi.setComment(reviewVi.getComment());
                }
                if (reviewVi.getReviewDate() != null) {
                    existingReviewVi.setReviewDate(reviewVi.getReviewDate());
                }

                return existingReviewVi;
            })
            .map(reviewViRepository::save);
    }

    /**
     * Get all the reviewVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewVi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewVi> findOne(Long id) {
        LOG.debug("Request to get ReviewVi : {}", id);
        return reviewViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewVi : {}", id);
        reviewViRepository.deleteById(id);
    }
}
