package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewViVi;
import xyz.jhmapstruct.repository.NextReviewViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewViVi}.
 */
@Service
@Transactional
public class NextReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViViService.class);

    private final NextReviewViViRepository nextReviewViViRepository;

    public NextReviewViViService(NextReviewViViRepository nextReviewViViRepository) {
        this.nextReviewViViRepository = nextReviewViViRepository;
    }

    /**
     * Save a nextReviewViVi.
     *
     * @param nextReviewViVi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViVi save(NextReviewViVi nextReviewViVi) {
        LOG.debug("Request to save NextReviewViVi : {}", nextReviewViVi);
        return nextReviewViViRepository.save(nextReviewViVi);
    }

    /**
     * Update a nextReviewViVi.
     *
     * @param nextReviewViVi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViVi update(NextReviewViVi nextReviewViVi) {
        LOG.debug("Request to update NextReviewViVi : {}", nextReviewViVi);
        return nextReviewViViRepository.save(nextReviewViVi);
    }

    /**
     * Partially update a nextReviewViVi.
     *
     * @param nextReviewViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewViVi> partialUpdate(NextReviewViVi nextReviewViVi) {
        LOG.debug("Request to partially update NextReviewViVi : {}", nextReviewViVi);

        return nextReviewViViRepository
            .findById(nextReviewViVi.getId())
            .map(existingNextReviewViVi -> {
                if (nextReviewViVi.getRating() != null) {
                    existingNextReviewViVi.setRating(nextReviewViVi.getRating());
                }
                if (nextReviewViVi.getComment() != null) {
                    existingNextReviewViVi.setComment(nextReviewViVi.getComment());
                }
                if (nextReviewViVi.getReviewDate() != null) {
                    existingNextReviewViVi.setReviewDate(nextReviewViVi.getReviewDate());
                }

                return existingNextReviewViVi;
            })
            .map(nextReviewViViRepository::save);
    }

    /**
     * Get all the nextReviewViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewViVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewViVi> findOne(Long id) {
        LOG.debug("Request to get NextReviewViVi : {}", id);
        return nextReviewViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewViVi : {}", id);
        nextReviewViViRepository.deleteById(id);
    }
}
