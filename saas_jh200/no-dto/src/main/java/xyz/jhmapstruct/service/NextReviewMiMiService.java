package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewMiMi;
import xyz.jhmapstruct.repository.NextReviewMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewMiMi}.
 */
@Service
@Transactional
public class NextReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiMiService.class);

    private final NextReviewMiMiRepository nextReviewMiMiRepository;

    public NextReviewMiMiService(NextReviewMiMiRepository nextReviewMiMiRepository) {
        this.nextReviewMiMiRepository = nextReviewMiMiRepository;
    }

    /**
     * Save a nextReviewMiMi.
     *
     * @param nextReviewMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiMi save(NextReviewMiMi nextReviewMiMi) {
        LOG.debug("Request to save NextReviewMiMi : {}", nextReviewMiMi);
        return nextReviewMiMiRepository.save(nextReviewMiMi);
    }

    /**
     * Update a nextReviewMiMi.
     *
     * @param nextReviewMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiMi update(NextReviewMiMi nextReviewMiMi) {
        LOG.debug("Request to update NextReviewMiMi : {}", nextReviewMiMi);
        return nextReviewMiMiRepository.save(nextReviewMiMi);
    }

    /**
     * Partially update a nextReviewMiMi.
     *
     * @param nextReviewMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewMiMi> partialUpdate(NextReviewMiMi nextReviewMiMi) {
        LOG.debug("Request to partially update NextReviewMiMi : {}", nextReviewMiMi);

        return nextReviewMiMiRepository
            .findById(nextReviewMiMi.getId())
            .map(existingNextReviewMiMi -> {
                if (nextReviewMiMi.getRating() != null) {
                    existingNextReviewMiMi.setRating(nextReviewMiMi.getRating());
                }
                if (nextReviewMiMi.getComment() != null) {
                    existingNextReviewMiMi.setComment(nextReviewMiMi.getComment());
                }
                if (nextReviewMiMi.getReviewDate() != null) {
                    existingNextReviewMiMi.setReviewDate(nextReviewMiMi.getReviewDate());
                }

                return existingNextReviewMiMi;
            })
            .map(nextReviewMiMiRepository::save);
    }

    /**
     * Get all the nextReviewMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewMiMi> findOne(Long id) {
        LOG.debug("Request to get NextReviewMiMi : {}", id);
        return nextReviewMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewMiMi : {}", id);
        nextReviewMiMiRepository.deleteById(id);
    }
}
