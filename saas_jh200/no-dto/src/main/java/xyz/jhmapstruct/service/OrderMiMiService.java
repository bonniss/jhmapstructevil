package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.repository.OrderMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
@Service
@Transactional
public class OrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiService.class);

    private final OrderMiMiRepository orderMiMiRepository;

    public OrderMiMiService(OrderMiMiRepository orderMiMiRepository) {
        this.orderMiMiRepository = orderMiMiRepository;
    }

    /**
     * Save a orderMiMi.
     *
     * @param orderMiMi the entity to save.
     * @return the persisted entity.
     */
    public OrderMiMi save(OrderMiMi orderMiMi) {
        LOG.debug("Request to save OrderMiMi : {}", orderMiMi);
        return orderMiMiRepository.save(orderMiMi);
    }

    /**
     * Update a orderMiMi.
     *
     * @param orderMiMi the entity to save.
     * @return the persisted entity.
     */
    public OrderMiMi update(OrderMiMi orderMiMi) {
        LOG.debug("Request to update OrderMiMi : {}", orderMiMi);
        return orderMiMiRepository.save(orderMiMi);
    }

    /**
     * Partially update a orderMiMi.
     *
     * @param orderMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderMiMi> partialUpdate(OrderMiMi orderMiMi) {
        LOG.debug("Request to partially update OrderMiMi : {}", orderMiMi);

        return orderMiMiRepository
            .findById(orderMiMi.getId())
            .map(existingOrderMiMi -> {
                if (orderMiMi.getOrderDate() != null) {
                    existingOrderMiMi.setOrderDate(orderMiMi.getOrderDate());
                }
                if (orderMiMi.getTotalPrice() != null) {
                    existingOrderMiMi.setTotalPrice(orderMiMi.getTotalPrice());
                }
                if (orderMiMi.getStatus() != null) {
                    existingOrderMiMi.setStatus(orderMiMi.getStatus());
                }

                return existingOrderMiMi;
            })
            .map(orderMiMiRepository::save);
    }

    /**
     * Get all the orderMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderMiMi> findOne(Long id) {
        LOG.debug("Request to get OrderMiMi : {}", id);
        return orderMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMiMi : {}", id);
        orderMiMiRepository.deleteById(id);
    }
}
