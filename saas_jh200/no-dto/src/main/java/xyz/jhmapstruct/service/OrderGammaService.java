package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.repository.OrderGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderGamma}.
 */
@Service
@Transactional
public class OrderGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderGammaService.class);

    private final OrderGammaRepository orderGammaRepository;

    public OrderGammaService(OrderGammaRepository orderGammaRepository) {
        this.orderGammaRepository = orderGammaRepository;
    }

    /**
     * Save a orderGamma.
     *
     * @param orderGamma the entity to save.
     * @return the persisted entity.
     */
    public OrderGamma save(OrderGamma orderGamma) {
        LOG.debug("Request to save OrderGamma : {}", orderGamma);
        return orderGammaRepository.save(orderGamma);
    }

    /**
     * Update a orderGamma.
     *
     * @param orderGamma the entity to save.
     * @return the persisted entity.
     */
    public OrderGamma update(OrderGamma orderGamma) {
        LOG.debug("Request to update OrderGamma : {}", orderGamma);
        return orderGammaRepository.save(orderGamma);
    }

    /**
     * Partially update a orderGamma.
     *
     * @param orderGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderGamma> partialUpdate(OrderGamma orderGamma) {
        LOG.debug("Request to partially update OrderGamma : {}", orderGamma);

        return orderGammaRepository
            .findById(orderGamma.getId())
            .map(existingOrderGamma -> {
                if (orderGamma.getOrderDate() != null) {
                    existingOrderGamma.setOrderDate(orderGamma.getOrderDate());
                }
                if (orderGamma.getTotalPrice() != null) {
                    existingOrderGamma.setTotalPrice(orderGamma.getTotalPrice());
                }
                if (orderGamma.getStatus() != null) {
                    existingOrderGamma.setStatus(orderGamma.getStatus());
                }

                return existingOrderGamma;
            })
            .map(orderGammaRepository::save);
    }

    /**
     * Get all the orderGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderGamma> findAllWithEagerRelationships(Pageable pageable) {
        return orderGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderGamma> findOne(Long id) {
        LOG.debug("Request to get OrderGamma : {}", id);
        return orderGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderGamma : {}", id);
        orderGammaRepository.deleteById(id);
    }
}
