package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.repository.NextOrderRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrder}.
 */
@Service
@Transactional
public class NextOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderService.class);

    private final NextOrderRepository nextOrderRepository;

    public NextOrderService(NextOrderRepository nextOrderRepository) {
        this.nextOrderRepository = nextOrderRepository;
    }

    /**
     * Save a nextOrder.
     *
     * @param nextOrder the entity to save.
     * @return the persisted entity.
     */
    public NextOrder save(NextOrder nextOrder) {
        LOG.debug("Request to save NextOrder : {}", nextOrder);
        return nextOrderRepository.save(nextOrder);
    }

    /**
     * Update a nextOrder.
     *
     * @param nextOrder the entity to save.
     * @return the persisted entity.
     */
    public NextOrder update(NextOrder nextOrder) {
        LOG.debug("Request to update NextOrder : {}", nextOrder);
        return nextOrderRepository.save(nextOrder);
    }

    /**
     * Partially update a nextOrder.
     *
     * @param nextOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrder> partialUpdate(NextOrder nextOrder) {
        LOG.debug("Request to partially update NextOrder : {}", nextOrder);

        return nextOrderRepository
            .findById(nextOrder.getId())
            .map(existingNextOrder -> {
                if (nextOrder.getOrderDate() != null) {
                    existingNextOrder.setOrderDate(nextOrder.getOrderDate());
                }
                if (nextOrder.getTotalPrice() != null) {
                    existingNextOrder.setTotalPrice(nextOrder.getTotalPrice());
                }
                if (nextOrder.getStatus() != null) {
                    existingNextOrder.setStatus(nextOrder.getStatus());
                }

                return existingNextOrder;
            })
            .map(nextOrderRepository::save);
    }

    /**
     * Get all the nextOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrder> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrder> findOne(Long id) {
        LOG.debug("Request to get NextOrder : {}", id);
        return nextOrderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrder : {}", id);
        nextOrderRepository.deleteById(id);
    }
}
