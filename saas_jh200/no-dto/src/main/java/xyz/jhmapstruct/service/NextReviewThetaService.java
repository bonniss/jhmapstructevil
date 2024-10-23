package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.repository.NextReviewThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewTheta}.
 */
@Service
@Transactional
public class NextReviewThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewThetaService.class);

    private final NextReviewThetaRepository nextReviewThetaRepository;

    public NextReviewThetaService(NextReviewThetaRepository nextReviewThetaRepository) {
        this.nextReviewThetaRepository = nextReviewThetaRepository;
    }

    /**
     * Save a nextReviewTheta.
     *
     * @param nextReviewTheta the entity to save.
     * @return the persisted entity.
     */
    public NextReviewTheta save(NextReviewTheta nextReviewTheta) {
        LOG.debug("Request to save NextReviewTheta : {}", nextReviewTheta);
        return nextReviewThetaRepository.save(nextReviewTheta);
    }

    /**
     * Update a nextReviewTheta.
     *
     * @param nextReviewTheta the entity to save.
     * @return the persisted entity.
     */
    public NextReviewTheta update(NextReviewTheta nextReviewTheta) {
        LOG.debug("Request to update NextReviewTheta : {}", nextReviewTheta);
        return nextReviewThetaRepository.save(nextReviewTheta);
    }

    /**
     * Partially update a nextReviewTheta.
     *
     * @param nextReviewTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewTheta> partialUpdate(NextReviewTheta nextReviewTheta) {
        LOG.debug("Request to partially update NextReviewTheta : {}", nextReviewTheta);

        return nextReviewThetaRepository
            .findById(nextReviewTheta.getId())
            .map(existingNextReviewTheta -> {
                if (nextReviewTheta.getRating() != null) {
                    existingNextReviewTheta.setRating(nextReviewTheta.getRating());
                }
                if (nextReviewTheta.getComment() != null) {
                    existingNextReviewTheta.setComment(nextReviewTheta.getComment());
                }
                if (nextReviewTheta.getReviewDate() != null) {
                    existingNextReviewTheta.setReviewDate(nextReviewTheta.getReviewDate());
                }

                return existingNextReviewTheta;
            })
            .map(nextReviewThetaRepository::save);
    }

    /**
     * Get all the nextReviewThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewTheta> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextReviewTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewTheta> findOne(Long id) {
        LOG.debug("Request to get NextReviewTheta : {}", id);
        return nextReviewThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextReviewTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewTheta : {}", id);
        nextReviewThetaRepository.deleteById(id);
    }
}
