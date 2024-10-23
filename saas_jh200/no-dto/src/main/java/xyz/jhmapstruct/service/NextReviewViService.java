package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewVi;
import xyz.jhmapstruct.repository.NextReviewViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewVi}.
 */
@Service
@Transactional
public class NextReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViService.class);

    private final NextReviewViRepository nextReviewViRepository;

    public NextReviewViService(NextReviewViRepository nextReviewViRepository) {
        this.nextReviewViRepository = nextReviewViRepository;
    }

    /**
     * Save a nextReviewVi.
     *
     * @param nextReviewVi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewVi save(NextReviewVi nextReviewVi) {
        LOG.debug("Request to save NextReviewVi : {}", nextReviewVi);
        return nextReviewViRepository.save(nextReviewVi);
    }

    /**
     * Update a nextReviewVi.
     *
     * @param nextReviewVi the entity to save.
     * @return the persisted entity.
     */
    public NextReviewVi update(NextReviewVi nextReviewVi) {
        LOG.debug("Request to update NextReviewVi : {}", nextReviewVi);
        return nextReviewViRepository.save(nextReviewVi);
    }

    /**
     * Partially update a nextReviewVi.
     *
     * @param nextReviewVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewVi> partialUpdate(NextReviewVi nextReviewVi) {
        LOG.debug("Request to partially update NextReviewVi : {}", nextReviewVi);

        return nextReviewViRepository
            .findById(nextReviewVi.getId())
            .map(existingNextReviewVi -> {
                if (nextReviewVi.getRating() != null) {
                    existingNextReviewVi.setRating(nextReviewVi.getRating());
                }
                if (nextReviewVi.getComment() != null) {
                    existingNextReviewVi.setComment(nextReviewVi.getComment());
                }
                if (nextReviewVi.getReviewDate() != null) {
                    existingNextReviewVi.setReviewDate(nextReviewVi.getReviewDate());
                }

                return existingNextReviewVi;
            })
            .map(nextReviewViRepository::save);
    }

    /**
     * Get all the nextReviewVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewVi> findOne(Long id) {
        LOG.debug("Request to get NextReviewVi : {}", id);
        return nextReviewViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewVi : {}", id);
        nextReviewViRepository.deleteById(id);
    }
}
