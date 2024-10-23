package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.repository.NextReviewBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewBeta}.
 */
@Service
@Transactional
public class NextReviewBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewBetaService.class);

    private final NextReviewBetaRepository nextReviewBetaRepository;

    public NextReviewBetaService(NextReviewBetaRepository nextReviewBetaRepository) {
        this.nextReviewBetaRepository = nextReviewBetaRepository;
    }

    /**
     * Save a nextReviewBeta.
     *
     * @param nextReviewBeta the entity to save.
     * @return the persisted entity.
     */
    public NextReviewBeta save(NextReviewBeta nextReviewBeta) {
        LOG.debug("Request to save NextReviewBeta : {}", nextReviewBeta);
        return nextReviewBetaRepository.save(nextReviewBeta);
    }

    /**
     * Update a nextReviewBeta.
     *
     * @param nextReviewBeta the entity to save.
     * @return the persisted entity.
     */
    public NextReviewBeta update(NextReviewBeta nextReviewBeta) {
        LOG.debug("Request to update NextReviewBeta : {}", nextReviewBeta);
        return nextReviewBetaRepository.save(nextReviewBeta);
    }

    /**
     * Partially update a nextReviewBeta.
     *
     * @param nextReviewBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewBeta> partialUpdate(NextReviewBeta nextReviewBeta) {
        LOG.debug("Request to partially update NextReviewBeta : {}", nextReviewBeta);

        return nextReviewBetaRepository
            .findById(nextReviewBeta.getId())
            .map(existingNextReviewBeta -> {
                if (nextReviewBeta.getRating() != null) {
                    existingNextReviewBeta.setRating(nextReviewBeta.getRating());
                }
                if (nextReviewBeta.getComment() != null) {
                    existingNextReviewBeta.setComment(nextReviewBeta.getComment());
                }
                if (nextReviewBeta.getReviewDate() != null) {
                    existingNextReviewBeta.setReviewDate(nextReviewBeta.getReviewDate());
                }

                return existingNextReviewBeta;
            })
            .map(nextReviewBetaRepository::save);
    }

    /**
     * Get all the nextReviewBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewBeta> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewBeta> findOne(Long id) {
        LOG.debug("Request to get NextReviewBeta : {}", id);
        return nextReviewBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewBeta : {}", id);
        nextReviewBetaRepository.deleteById(id);
    }
}
