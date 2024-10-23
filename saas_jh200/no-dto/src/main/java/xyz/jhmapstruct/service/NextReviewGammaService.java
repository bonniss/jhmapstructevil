package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.repository.NextReviewGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewGamma}.
 */
@Service
@Transactional
public class NextReviewGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewGammaService.class);

    private final NextReviewGammaRepository nextReviewGammaRepository;

    public NextReviewGammaService(NextReviewGammaRepository nextReviewGammaRepository) {
        this.nextReviewGammaRepository = nextReviewGammaRepository;
    }

    /**
     * Save a nextReviewGamma.
     *
     * @param nextReviewGamma the entity to save.
     * @return the persisted entity.
     */
    public NextReviewGamma save(NextReviewGamma nextReviewGamma) {
        LOG.debug("Request to save NextReviewGamma : {}", nextReviewGamma);
        return nextReviewGammaRepository.save(nextReviewGamma);
    }

    /**
     * Update a nextReviewGamma.
     *
     * @param nextReviewGamma the entity to save.
     * @return the persisted entity.
     */
    public NextReviewGamma update(NextReviewGamma nextReviewGamma) {
        LOG.debug("Request to update NextReviewGamma : {}", nextReviewGamma);
        return nextReviewGammaRepository.save(nextReviewGamma);
    }

    /**
     * Partially update a nextReviewGamma.
     *
     * @param nextReviewGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewGamma> partialUpdate(NextReviewGamma nextReviewGamma) {
        LOG.debug("Request to partially update NextReviewGamma : {}", nextReviewGamma);

        return nextReviewGammaRepository
            .findById(nextReviewGamma.getId())
            .map(existingNextReviewGamma -> {
                if (nextReviewGamma.getRating() != null) {
                    existingNextReviewGamma.setRating(nextReviewGamma.getRating());
                }
                if (nextReviewGamma.getComment() != null) {
                    existingNextReviewGamma.setComment(nextReviewGamma.getComment());
                }
                if (nextReviewGamma.getReviewDate() != null) {
                    existingNextReviewGamma.setReviewDate(nextReviewGamma.getReviewDate());
                }

                return existingNextReviewGamma;
            })
            .map(nextReviewGammaRepository::save);
    }

    /**
     * Get all the nextReviewGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewGamma> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewGamma> findOne(Long id) {
        LOG.debug("Request to get NextReviewGamma : {}", id);
        return nextReviewGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewGamma : {}", id);
        nextReviewGammaRepository.deleteById(id);
    }
}
