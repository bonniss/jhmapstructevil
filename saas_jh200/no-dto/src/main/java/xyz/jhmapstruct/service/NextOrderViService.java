package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.repository.NextOrderViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderVi}.
 */
@Service
@Transactional
public class NextOrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViService.class);

    private final NextOrderViRepository nextOrderViRepository;

    public NextOrderViService(NextOrderViRepository nextOrderViRepository) {
        this.nextOrderViRepository = nextOrderViRepository;
    }

    /**
     * Save a nextOrderVi.
     *
     * @param nextOrderVi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderVi save(NextOrderVi nextOrderVi) {
        LOG.debug("Request to save NextOrderVi : {}", nextOrderVi);
        return nextOrderViRepository.save(nextOrderVi);
    }

    /**
     * Update a nextOrderVi.
     *
     * @param nextOrderVi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderVi update(NextOrderVi nextOrderVi) {
        LOG.debug("Request to update NextOrderVi : {}", nextOrderVi);
        return nextOrderViRepository.save(nextOrderVi);
    }

    /**
     * Partially update a nextOrderVi.
     *
     * @param nextOrderVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderVi> partialUpdate(NextOrderVi nextOrderVi) {
        LOG.debug("Request to partially update NextOrderVi : {}", nextOrderVi);

        return nextOrderViRepository
            .findById(nextOrderVi.getId())
            .map(existingNextOrderVi -> {
                if (nextOrderVi.getOrderDate() != null) {
                    existingNextOrderVi.setOrderDate(nextOrderVi.getOrderDate());
                }
                if (nextOrderVi.getTotalPrice() != null) {
                    existingNextOrderVi.setTotalPrice(nextOrderVi.getTotalPrice());
                }
                if (nextOrderVi.getStatus() != null) {
                    existingNextOrderVi.setStatus(nextOrderVi.getStatus());
                }

                return existingNextOrderVi;
            })
            .map(nextOrderViRepository::save);
    }

    /**
     * Get all the nextOrderVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderVi> findOne(Long id) {
        LOG.debug("Request to get NextOrderVi : {}", id);
        return nextOrderViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderVi : {}", id);
        nextOrderViRepository.deleteById(id);
    }
}
