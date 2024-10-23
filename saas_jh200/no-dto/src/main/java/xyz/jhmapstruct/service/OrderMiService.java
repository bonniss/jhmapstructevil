package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
@Service
@Transactional
public class OrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiService.class);

    private final OrderMiRepository orderMiRepository;

    public OrderMiService(OrderMiRepository orderMiRepository) {
        this.orderMiRepository = orderMiRepository;
    }

    /**
     * Save a orderMi.
     *
     * @param orderMi the entity to save.
     * @return the persisted entity.
     */
    public OrderMi save(OrderMi orderMi) {
        LOG.debug("Request to save OrderMi : {}", orderMi);
        return orderMiRepository.save(orderMi);
    }

    /**
     * Update a orderMi.
     *
     * @param orderMi the entity to save.
     * @return the persisted entity.
     */
    public OrderMi update(OrderMi orderMi) {
        LOG.debug("Request to update OrderMi : {}", orderMi);
        return orderMiRepository.save(orderMi);
    }

    /**
     * Partially update a orderMi.
     *
     * @param orderMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderMi> partialUpdate(OrderMi orderMi) {
        LOG.debug("Request to partially update OrderMi : {}", orderMi);

        return orderMiRepository
            .findById(orderMi.getId())
            .map(existingOrderMi -> {
                if (orderMi.getOrderDate() != null) {
                    existingOrderMi.setOrderDate(orderMi.getOrderDate());
                }
                if (orderMi.getTotalPrice() != null) {
                    existingOrderMi.setTotalPrice(orderMi.getTotalPrice());
                }
                if (orderMi.getStatus() != null) {
                    existingOrderMi.setStatus(orderMi.getStatus());
                }

                return existingOrderMi;
            })
            .map(orderMiRepository::save);
    }

    /**
     * Get all the orderMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderMi> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderMi> findOne(Long id) {
        LOG.debug("Request to get OrderMi : {}", id);
        return orderMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMi : {}", id);
        orderMiRepository.deleteById(id);
    }
}
