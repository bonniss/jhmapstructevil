package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderMiMi}.
 */
@Service
@Transactional
public class NextOrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiMiService.class);

    private final NextOrderMiMiRepository nextOrderMiMiRepository;

    public NextOrderMiMiService(NextOrderMiMiRepository nextOrderMiMiRepository) {
        this.nextOrderMiMiRepository = nextOrderMiMiRepository;
    }

    /**
     * Save a nextOrderMiMi.
     *
     * @param nextOrderMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiMi save(NextOrderMiMi nextOrderMiMi) {
        LOG.debug("Request to save NextOrderMiMi : {}", nextOrderMiMi);
        return nextOrderMiMiRepository.save(nextOrderMiMi);
    }

    /**
     * Update a nextOrderMiMi.
     *
     * @param nextOrderMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiMi update(NextOrderMiMi nextOrderMiMi) {
        LOG.debug("Request to update NextOrderMiMi : {}", nextOrderMiMi);
        return nextOrderMiMiRepository.save(nextOrderMiMi);
    }

    /**
     * Partially update a nextOrderMiMi.
     *
     * @param nextOrderMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderMiMi> partialUpdate(NextOrderMiMi nextOrderMiMi) {
        LOG.debug("Request to partially update NextOrderMiMi : {}", nextOrderMiMi);

        return nextOrderMiMiRepository
            .findById(nextOrderMiMi.getId())
            .map(existingNextOrderMiMi -> {
                if (nextOrderMiMi.getOrderDate() != null) {
                    existingNextOrderMiMi.setOrderDate(nextOrderMiMi.getOrderDate());
                }
                if (nextOrderMiMi.getTotalPrice() != null) {
                    existingNextOrderMiMi.setTotalPrice(nextOrderMiMi.getTotalPrice());
                }
                if (nextOrderMiMi.getStatus() != null) {
                    existingNextOrderMiMi.setStatus(nextOrderMiMi.getStatus());
                }

                return existingNextOrderMiMi;
            })
            .map(nextOrderMiMiRepository::save);
    }

    /**
     * Get all the nextOrderMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderMiMi> findOne(Long id) {
        LOG.debug("Request to get NextOrderMiMi : {}", id);
        return nextOrderMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderMiMi : {}", id);
        nextOrderMiMiRepository.deleteById(id);
    }
}
