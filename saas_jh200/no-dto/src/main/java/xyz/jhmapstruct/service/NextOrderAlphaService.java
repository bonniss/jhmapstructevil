package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.repository.NextOrderAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderAlpha}.
 */
@Service
@Transactional
public class NextOrderAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderAlphaService.class);

    private final NextOrderAlphaRepository nextOrderAlphaRepository;

    public NextOrderAlphaService(NextOrderAlphaRepository nextOrderAlphaRepository) {
        this.nextOrderAlphaRepository = nextOrderAlphaRepository;
    }

    /**
     * Save a nextOrderAlpha.
     *
     * @param nextOrderAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextOrderAlpha save(NextOrderAlpha nextOrderAlpha) {
        LOG.debug("Request to save NextOrderAlpha : {}", nextOrderAlpha);
        return nextOrderAlphaRepository.save(nextOrderAlpha);
    }

    /**
     * Update a nextOrderAlpha.
     *
     * @param nextOrderAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextOrderAlpha update(NextOrderAlpha nextOrderAlpha) {
        LOG.debug("Request to update NextOrderAlpha : {}", nextOrderAlpha);
        return nextOrderAlphaRepository.save(nextOrderAlpha);
    }

    /**
     * Partially update a nextOrderAlpha.
     *
     * @param nextOrderAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderAlpha> partialUpdate(NextOrderAlpha nextOrderAlpha) {
        LOG.debug("Request to partially update NextOrderAlpha : {}", nextOrderAlpha);

        return nextOrderAlphaRepository
            .findById(nextOrderAlpha.getId())
            .map(existingNextOrderAlpha -> {
                if (nextOrderAlpha.getOrderDate() != null) {
                    existingNextOrderAlpha.setOrderDate(nextOrderAlpha.getOrderDate());
                }
                if (nextOrderAlpha.getTotalPrice() != null) {
                    existingNextOrderAlpha.setTotalPrice(nextOrderAlpha.getTotalPrice());
                }
                if (nextOrderAlpha.getStatus() != null) {
                    existingNextOrderAlpha.setStatus(nextOrderAlpha.getStatus());
                }

                return existingNextOrderAlpha;
            })
            .map(nextOrderAlphaRepository::save);
    }

    /**
     * Get all the nextOrderAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderAlpha> findOne(Long id) {
        LOG.debug("Request to get NextOrderAlpha : {}", id);
        return nextOrderAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderAlpha : {}", id);
        nextOrderAlphaRepository.deleteById(id);
    }
}
