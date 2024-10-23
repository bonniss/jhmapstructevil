package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
@Service
@Transactional
public class ReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiService.class);

    private final ReviewMiMiRepository reviewMiMiRepository;

    public ReviewMiMiService(ReviewMiMiRepository reviewMiMiRepository) {
        this.reviewMiMiRepository = reviewMiMiRepository;
    }

    /**
     * Save a reviewMiMi.
     *
     * @param reviewMiMi the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiMi save(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to save ReviewMiMi : {}", reviewMiMi);
        return reviewMiMiRepository.save(reviewMiMi);
    }

    /**
     * Update a reviewMiMi.
     *
     * @param reviewMiMi the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiMi update(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to update ReviewMiMi : {}", reviewMiMi);
        return reviewMiMiRepository.save(reviewMiMi);
    }

    /**
     * Partially update a reviewMiMi.
     *
     * @param reviewMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewMiMi> partialUpdate(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to partially update ReviewMiMi : {}", reviewMiMi);

        return reviewMiMiRepository
            .findById(reviewMiMi.getId())
            .map(existingReviewMiMi -> {
                if (reviewMiMi.getRating() != null) {
                    existingReviewMiMi.setRating(reviewMiMi.getRating());
                }
                if (reviewMiMi.getComment() != null) {
                    existingReviewMiMi.setComment(reviewMiMi.getComment());
                }
                if (reviewMiMi.getReviewDate() != null) {
                    existingReviewMiMi.setReviewDate(reviewMiMi.getReviewDate());
                }

                return existingReviewMiMi;
            })
            .map(reviewMiMiRepository::save);
    }

    /**
     * Get all the reviewMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one reviewMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewMiMi> findOne(Long id) {
        LOG.debug("Request to get ReviewMiMi : {}", id);
        return reviewMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reviewMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMiMi : {}", id);
        reviewMiMiRepository.deleteById(id);
    }
}
