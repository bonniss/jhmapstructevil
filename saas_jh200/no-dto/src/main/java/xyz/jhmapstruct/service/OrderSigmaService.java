package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.repository.OrderSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderSigma}.
 */
@Service
@Transactional
public class OrderSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderSigmaService.class);

    private final OrderSigmaRepository orderSigmaRepository;

    public OrderSigmaService(OrderSigmaRepository orderSigmaRepository) {
        this.orderSigmaRepository = orderSigmaRepository;
    }

    /**
     * Save a orderSigma.
     *
     * @param orderSigma the entity to save.
     * @return the persisted entity.
     */
    public OrderSigma save(OrderSigma orderSigma) {
        LOG.debug("Request to save OrderSigma : {}", orderSigma);
        return orderSigmaRepository.save(orderSigma);
    }

    /**
     * Update a orderSigma.
     *
     * @param orderSigma the entity to save.
     * @return the persisted entity.
     */
    public OrderSigma update(OrderSigma orderSigma) {
        LOG.debug("Request to update OrderSigma : {}", orderSigma);
        return orderSigmaRepository.save(orderSigma);
    }

    /**
     * Partially update a orderSigma.
     *
     * @param orderSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderSigma> partialUpdate(OrderSigma orderSigma) {
        LOG.debug("Request to partially update OrderSigma : {}", orderSigma);

        return orderSigmaRepository
            .findById(orderSigma.getId())
            .map(existingOrderSigma -> {
                if (orderSigma.getOrderDate() != null) {
                    existingOrderSigma.setOrderDate(orderSigma.getOrderDate());
                }
                if (orderSigma.getTotalPrice() != null) {
                    existingOrderSigma.setTotalPrice(orderSigma.getTotalPrice());
                }
                if (orderSigma.getStatus() != null) {
                    existingOrderSigma.setStatus(orderSigma.getStatus());
                }

                return existingOrderSigma;
            })
            .map(orderSigmaRepository::save);
    }

    /**
     * Get all the orderSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderSigma> findAllWithEagerRelationships(Pageable pageable) {
        return orderSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderSigma> findOne(Long id) {
        LOG.debug("Request to get OrderSigma : {}", id);
        return orderSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderSigma : {}", id);
        orderSigmaRepository.deleteById(id);
    }
}
