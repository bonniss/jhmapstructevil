package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
@Service
@Transactional
public class ReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiService.class);

    private final ReviewMiRepository reviewMiRepository;

    public ReviewMiService(ReviewMiRepository reviewMiRepository) {
        this.reviewMiRepository = reviewMiRepository;
    }

    /**
     * Save a reviewMi.
     *
     * @param reviewMi the entity to save.
     * @return the persisted entity.
     */
    public ReviewMi save(ReviewMi reviewMi) {
        LOG.debug("Request to save ReviewMi : {}", reviewMi);
        return reviewMiRepository.save(reviewMi);
    }

    /**
     * Update a reviewMi.
     *
     * @param reviewMi the entity to save.
     * @return the persisted entity.
     */
    public ReviewMi update(ReviewMi reviewMi) {
        LOG.debug("Request to update ReviewMi : {}", reviewMi);
        return reviewMiRepository.save(reviewMi);
    }

    /**
     * Partially update a reviewMi.
     *
     * @param reviewMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewMi> partialUpdate(ReviewMi reviewMi) {
        LOG.debug("Request to partially update ReviewMi : {}", reviewMi);

        return reviewMiRepository
            .findById(reviewMi.getId())
            .map(existingReviewMi -> {
                if (reviewMi.getRating() != null) {
                    existingReviewMi.setRating(reviewMi.getRating());
                }
                if (reviewMi.getComment() != null) {
                    existingReviewMi.setComment(reviewMi.getComment());
                }
                if (reviewMi.getReviewDate() != null) {
                    existingReviewMi.setReviewDate(reviewMi.getReviewDate());
                }

                return existingReviewMi;
            })
            .map(reviewMiRepository::save);
    }

    /**
     * Get all the reviewMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewMi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewMi> findOne(Long id) {
        LOG.debug("Request to get ReviewMi : {}", id);
        return reviewMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMi : {}", id);
        reviewMiRepository.deleteById(id);
    }
}
