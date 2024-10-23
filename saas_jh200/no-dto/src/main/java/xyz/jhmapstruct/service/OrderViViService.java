package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.repository.OrderViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
@Service
@Transactional
public class OrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViService.class);

    private final OrderViViRepository orderViViRepository;

    public OrderViViService(OrderViViRepository orderViViRepository) {
        this.orderViViRepository = orderViViRepository;
    }

    /**
     * Save a orderViVi.
     *
     * @param orderViVi the entity to save.
     * @return the persisted entity.
     */
    public OrderViVi save(OrderViVi orderViVi) {
        LOG.debug("Request to save OrderViVi : {}", orderViVi);
        return orderViViRepository.save(orderViVi);
    }

    /**
     * Update a orderViVi.
     *
     * @param orderViVi the entity to save.
     * @return the persisted entity.
     */
    public OrderViVi update(OrderViVi orderViVi) {
        LOG.debug("Request to update OrderViVi : {}", orderViVi);
        return orderViViRepository.save(orderViVi);
    }

    /**
     * Partially update a orderViVi.
     *
     * @param orderViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderViVi> partialUpdate(OrderViVi orderViVi) {
        LOG.debug("Request to partially update OrderViVi : {}", orderViVi);

        return orderViViRepository
            .findById(orderViVi.getId())
            .map(existingOrderViVi -> {
                if (orderViVi.getOrderDate() != null) {
                    existingOrderViVi.setOrderDate(orderViVi.getOrderDate());
                }
                if (orderViVi.getTotalPrice() != null) {
                    existingOrderViVi.setTotalPrice(orderViVi.getTotalPrice());
                }
                if (orderViVi.getStatus() != null) {
                    existingOrderViVi.setStatus(orderViVi.getStatus());
                }

                return existingOrderViVi;
            })
            .map(orderViViRepository::save);
    }

    /**
     * Get all the orderViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderViVi> findAllWithEagerRelationships(Pageable pageable) {
        return orderViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderViVi> findOne(Long id) {
        LOG.debug("Request to get OrderViVi : {}", id);
        return orderViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderViVi : {}", id);
        orderViViRepository.deleteById(id);
    }
}
