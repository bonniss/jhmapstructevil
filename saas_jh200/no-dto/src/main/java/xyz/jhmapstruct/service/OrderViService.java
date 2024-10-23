package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.repository.OrderViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
@Service
@Transactional
public class OrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViService.class);

    private final OrderViRepository orderViRepository;

    public OrderViService(OrderViRepository orderViRepository) {
        this.orderViRepository = orderViRepository;
    }

    /**
     * Save a orderVi.
     *
     * @param orderVi the entity to save.
     * @return the persisted entity.
     */
    public OrderVi save(OrderVi orderVi) {
        LOG.debug("Request to save OrderVi : {}", orderVi);
        return orderViRepository.save(orderVi);
    }

    /**
     * Update a orderVi.
     *
     * @param orderVi the entity to save.
     * @return the persisted entity.
     */
    public OrderVi update(OrderVi orderVi) {
        LOG.debug("Request to update OrderVi : {}", orderVi);
        return orderViRepository.save(orderVi);
    }

    /**
     * Partially update a orderVi.
     *
     * @param orderVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderVi> partialUpdate(OrderVi orderVi) {
        LOG.debug("Request to partially update OrderVi : {}", orderVi);

        return orderViRepository
            .findById(orderVi.getId())
            .map(existingOrderVi -> {
                if (orderVi.getOrderDate() != null) {
                    existingOrderVi.setOrderDate(orderVi.getOrderDate());
                }
                if (orderVi.getTotalPrice() != null) {
                    existingOrderVi.setTotalPrice(orderVi.getTotalPrice());
                }
                if (orderVi.getStatus() != null) {
                    existingOrderVi.setStatus(orderVi.getStatus());
                }

                return existingOrderVi;
            })
            .map(orderViRepository::save);
    }

    /**
     * Get all the orderVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderVi> findAllWithEagerRelationships(Pageable pageable) {
        return orderViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderVi> findOne(Long id) {
        LOG.debug("Request to get OrderVi : {}", id);
        return orderViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderVi : {}", id);
        orderViRepository.deleteById(id);
    }
}
