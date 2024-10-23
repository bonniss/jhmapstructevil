package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.repository.NextOrderViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderViVi}.
 */
@Service
@Transactional
public class NextOrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViViService.class);

    private final NextOrderViViRepository nextOrderViViRepository;

    public NextOrderViViService(NextOrderViViRepository nextOrderViViRepository) {
        this.nextOrderViViRepository = nextOrderViViRepository;
    }

    /**
     * Save a nextOrderViVi.
     *
     * @param nextOrderViVi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViVi save(NextOrderViVi nextOrderViVi) {
        LOG.debug("Request to save NextOrderViVi : {}", nextOrderViVi);
        return nextOrderViViRepository.save(nextOrderViVi);
    }

    /**
     * Update a nextOrderViVi.
     *
     * @param nextOrderViVi the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViVi update(NextOrderViVi nextOrderViVi) {
        LOG.debug("Request to update NextOrderViVi : {}", nextOrderViVi);
        return nextOrderViViRepository.save(nextOrderViVi);
    }

    /**
     * Partially update a nextOrderViVi.
     *
     * @param nextOrderViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderViVi> partialUpdate(NextOrderViVi nextOrderViVi) {
        LOG.debug("Request to partially update NextOrderViVi : {}", nextOrderViVi);

        return nextOrderViViRepository
            .findById(nextOrderViVi.getId())
            .map(existingNextOrderViVi -> {
                if (nextOrderViVi.getOrderDate() != null) {
                    existingNextOrderViVi.setOrderDate(nextOrderViVi.getOrderDate());
                }
                if (nextOrderViVi.getTotalPrice() != null) {
                    existingNextOrderViVi.setTotalPrice(nextOrderViVi.getTotalPrice());
                }
                if (nextOrderViVi.getStatus() != null) {
                    existingNextOrderViVi.setStatus(nextOrderViVi.getStatus());
                }

                return existingNextOrderViVi;
            })
            .map(nextOrderViViRepository::save);
    }

    /**
     * Get all the nextOrderViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderViVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderViVi> findOne(Long id) {
        LOG.debug("Request to get NextOrderViVi : {}", id);
        return nextOrderViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderViVi : {}", id);
        nextOrderViViRepository.deleteById(id);
    }
}
