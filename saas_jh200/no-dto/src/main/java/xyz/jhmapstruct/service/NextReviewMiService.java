package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.repository.NextReviewMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewMi}.
 */
@Service
@Transactional
public class NextReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiService.class);

    private final NextReviewMiRepository nextReviewMiRepository;

    public NextReviewMiService(NextReviewMiRepository nextReviewMiRepository) {
        this.nextReviewMiRepository = nextReviewMiRepository;
    }

    /**
     * Save a nextReviewMi.
     *
     * @param nextReviewMi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMi save(NextReviewMi nextReviewMi) {
        LOG.debug("Request to save NextReviewMi : {}", nextReviewMi);
        return nextReviewMiRepository.save(nextReviewMi);
    }

    /**
     * Update a nextReviewMi.
     *
     * @param nextReviewMi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMi update(NextReviewMi nextReviewMi) {
        LOG.debug("Request to update NextReviewMi : {}", nextReviewMi);
        return nextReviewMiRepository.save(nextReviewMi);
    }

    /**
     * Partially update a nextReviewMi.
     *
     * @param nextReviewMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewMi> partialUpdate(NextReviewMi nextReviewMi) {
        LOG.debug("Request to partially update NextReviewMi : {}", nextReviewMi);

        return nextReviewMiRepository
            .findById(nextReviewMi.getId())
            .map(existingNextReviewMi -> {
                if (nextReviewMi.getRating() != null) {
                    existingNextReviewMi.setRating(nextReviewMi.getRating());
                }
                if (nextReviewMi.getComment() != null) {
                    existingNextReviewMi.setComment(nextReviewMi.getComment());
                }
                if (nextReviewMi.getReviewDate() != null) {
                    existingNextReviewMi.setReviewDate(nextReviewMi.getReviewDate());
                }

                return existingNextReviewMi;
            })
            .map(nextReviewMiRepository::save);
    }

    /**
     * Get all the nextReviewMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewMi> findOne(Long id) {
        LOG.debug("Request to get NextReviewMi : {}", id);
        return nextReviewMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewMi : {}", id);
        nextReviewMiRepository.deleteById(id);
    }
}
