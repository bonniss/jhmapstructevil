package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.repository.NextOrderMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderMi}.
 */
@Service
@Transactional
public class NextOrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiService.class);

    private final NextOrderMiRepository nextOrderMiRepository;

    public NextOrderMiService(NextOrderMiRepository nextOrderMiRepository) {
        this.nextOrderMiRepository = nextOrderMiRepository;
    }

    /**
     * Save a nextOrderMi.
     *
     * @param nextOrderMi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMi save(NextOrderMi nextOrderMi) {
        LOG.debug("Request to save NextOrderMi : {}", nextOrderMi);
        return nextOrderMiRepository.save(nextOrderMi);
    }

    /**
     * Update a nextOrderMi.
     *
     * @param nextOrderMi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMi update(NextOrderMi nextOrderMi) {
        LOG.debug("Request to update NextOrderMi : {}", nextOrderMi);
        return nextOrderMiRepository.save(nextOrderMi);
    }

    /**
     * Partially update a nextOrderMi.
     *
     * @param nextOrderMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderMi> partialUpdate(NextOrderMi nextOrderMi) {
        LOG.debug("Request to partially update NextOrderMi : {}", nextOrderMi);

        return nextOrderMiRepository
            .findById(nextOrderMi.getId())
            .map(existingNextOrderMi -> {
                if (nextOrderMi.getOrderDate() != null) {
                    existingNextOrderMi.setOrderDate(nextOrderMi.getOrderDate());
                }
                if (nextOrderMi.getTotalPrice() != null) {
                    existingNextOrderMi.setTotalPrice(nextOrderMi.getTotalPrice());
                }
                if (nextOrderMi.getStatus() != null) {
                    existingNextOrderMi.setStatus(nextOrderMi.getStatus());
                }

                return existingNextOrderMi;
            })
            .map(nextOrderMiRepository::save);
    }

    /**
     * Get all the nextOrderMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderMi> findOne(Long id) {
        LOG.debug("Request to get NextOrderMi : {}", id);
        return nextOrderMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderMi : {}", id);
        nextOrderMiRepository.deleteById(id);
    }
}
