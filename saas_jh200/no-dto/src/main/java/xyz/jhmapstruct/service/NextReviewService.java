package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.repository.NextReviewRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReview}.
 */
@Service
@Transactional
public class NextReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewService.class);

    private final NextReviewRepository nextReviewRepository;

    public NextReviewService(NextReviewRepository nextReviewRepository) {
        this.nextReviewRepository = nextReviewRepository;
    }

    /**
     * Save a nextReview.
     *
     * @param nextReview the entity to save.
     * @return the persisted entity.
     */
    public NextReview save(NextReview nextReview) {
        LOG.debug("Request to save NextReview : {}", nextReview);
        return nextReviewRepository.save(nextReview);
    }

    /**
     * Update a nextReview.
     *
     * @param nextReview the entity to save.
     * @return the persisted entity.
     */
    public NextReview update(NextReview nextReview) {
        LOG.debug("Request to update NextReview : {}", nextReview);
        return nextReviewRepository.save(nextReview);
    }

    /**
     * Partially update a nextReview.
     *
     * @param nextReview the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReview> partialUpdate(NextReview nextReview) {
        LOG.debug("Request to partially update NextReview : {}", nextReview);

        return nextReviewRepository
            .findById(nextReview.getId())
            .map(existingNextReview -> {
                if (nextReview.getRating() != null) {
                    existingNextReview.setRating(nextReview.getRating());
                }
                if (nextReview.getComment() != null) {
                    existingNextReview.setComment(nextReview.getComment());
                }
                if (nextReview.getReviewDate() != null) {
                    existingNextReview.setReviewDate(nextReview.getReviewDate());
                }

                return existingNextReview;
            })
            .map(nextReviewRepository::save);
    }

    /**
     * Get all the nextReviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReview> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReview> findOne(Long id) {
        LOG.debug("Request to get NextReview : {}", id);
        return nextReviewRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReview : {}", id);
        nextReviewRepository.deleteById(id);
    }
}
