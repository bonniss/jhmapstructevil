package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewSigma;
import xyz.jhmapstruct.repository.NextReviewSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewSigma}.
 */
@Service
@Transactional
public class NextReviewSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewSigmaService.class);

    private final NextReviewSigmaRepository nextReviewSigmaRepository;

    public NextReviewSigmaService(NextReviewSigmaRepository nextReviewSigmaRepository) {
        this.nextReviewSigmaRepository = nextReviewSigmaRepository;
    }

    /**
     * Save a nextReviewSigma.
     *
     * @param nextReviewSigma the entity to save.
     * @return the persisted entity.
     */
    public NextReviewSigma save(NextReviewSigma nextReviewSigma) {
        LOG.debug("Request to save NextReviewSigma : {}", nextReviewSigma);
        return nextReviewSigmaRepository.save(nextReviewSigma);
    }

    /**
     * Update a nextReviewSigma.
     *
     * @param nextReviewSigma the entity to save.
     * @return the persisted entity.
     */
    public NextReviewSigma update(NextReviewSigma nextReviewSigma) {
        LOG.debug("Request to update NextReviewSigma : {}", nextReviewSigma);
        return nextReviewSigmaRepository.save(nextReviewSigma);
    }

    /**
     * Partially update a nextReviewSigma.
     *
     * @param nextReviewSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewSigma> partialUpdate(NextReviewSigma nextReviewSigma) {
        LOG.debug("Request to partially update NextReviewSigma : {}", nextReviewSigma);

        return nextReviewSigmaRepository
            .findById(nextReviewSigma.getId())
            .map(existingNextReviewSigma -> {
                if (nextReviewSigma.getRating() != null) {
                    existingNextReviewSigma.setRating(nextReviewSigma.getRating());
                }
                if (nextReviewSigma.getComment() != null) {
                    existingNextReviewSigma.setComment(nextReviewSigma.getComment());
                }
                if (nextReviewSigma.getReviewDate() != null) {
                    existingNextReviewSigma.setReviewDate(nextReviewSigma.getReviewDate());
                }

                return existingNextReviewSigma;
            })
            .map(nextReviewSigmaRepository::save);
    }

    /**
     * Get all the nextReviewSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewSigma> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewSigma> findOne(Long id) {
        LOG.debug("Request to get NextReviewSigma : {}", id);
        return nextReviewSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewSigma : {}", id);
        nextReviewSigmaRepository.deleteById(id);
    }
}
