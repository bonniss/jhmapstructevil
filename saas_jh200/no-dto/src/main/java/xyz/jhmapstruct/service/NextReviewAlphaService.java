package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewAlpha;
import xyz.jhmapstruct.repository.NextReviewAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewAlpha}.
 */
@Service
@Transactional
public class NextReviewAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewAlphaService.class);

    private final NextReviewAlphaRepository nextReviewAlphaRepository;

    public NextReviewAlphaService(NextReviewAlphaRepository nextReviewAlphaRepository) {
        this.nextReviewAlphaRepository = nextReviewAlphaRepository;
    }

    /**
     * Save a nextReviewAlpha.
     *
     * @param nextReviewAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextReviewAlpha save(NextReviewAlpha nextReviewAlpha) {
        LOG.debug("Request to save NextReviewAlpha : {}", nextReviewAlpha);
        return nextReviewAlphaRepository.save(nextReviewAlpha);
    }

    /**
     * Update a nextReviewAlpha.
     *
     * @param nextReviewAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextReviewAlpha update(NextReviewAlpha nextReviewAlpha) {
        LOG.debug("Request to update NextReviewAlpha : {}", nextReviewAlpha);
        return nextReviewAlphaRepository.save(nextReviewAlpha);
    }

    /**
     * Partially update a nextReviewAlpha.
     *
     * @param nextReviewAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewAlpha> partialUpdate(NextReviewAlpha nextReviewAlpha) {
        LOG.debug("Request to partially update NextReviewAlpha : {}", nextReviewAlpha);

        return nextReviewAlphaRepository
            .findById(nextReviewAlpha.getId())
            .map(existingNextReviewAlpha -> {
                if (nextReviewAlpha.getRating() != null) {
                    existingNextReviewAlpha.setRating(nextReviewAlpha.getRating());
                }
                if (nextReviewAlpha.getComment() != null) {
                    existingNextReviewAlpha.setComment(nextReviewAlpha.getComment());
                }
                if (nextReviewAlpha.getReviewDate() != null) {
                    existingNextReviewAlpha.setReviewDate(nextReviewAlpha.getReviewDate());
                }

                return existingNextReviewAlpha;
            })
            .map(nextReviewAlphaRepository::save);
    }

    /**
     * Get all the nextReviewAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewAlpha> findOne(Long id) {
        LOG.debug("Request to get NextReviewAlpha : {}", id);
        return nextReviewAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewAlpha : {}", id);
        nextReviewAlphaRepository.deleteById(id);
    }
}
