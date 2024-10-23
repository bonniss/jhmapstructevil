package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.repository.OrderAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderAlpha}.
 */
@Service
@Transactional
public class OrderAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAlphaService.class);

    private final OrderAlphaRepository orderAlphaRepository;

    public OrderAlphaService(OrderAlphaRepository orderAlphaRepository) {
        this.orderAlphaRepository = orderAlphaRepository;
    }

    /**
     * Save a orderAlpha.
     *
     * @param orderAlpha the entity to save.
     * @return the persisted entity.
     */
    public OrderAlpha save(OrderAlpha orderAlpha) {
        LOG.debug("Request to save OrderAlpha : {}", orderAlpha);
        return orderAlphaRepository.save(orderAlpha);
    }

    /**
     * Update a orderAlpha.
     *
     * @param orderAlpha the entity to save.
     * @return the persisted entity.
     */
    public OrderAlpha update(OrderAlpha orderAlpha) {
        LOG.debug("Request to update OrderAlpha : {}", orderAlpha);
        return orderAlphaRepository.save(orderAlpha);
    }

    /**
     * Partially update a orderAlpha.
     *
     * @param orderAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderAlpha> partialUpdate(OrderAlpha orderAlpha) {
        LOG.debug("Request to partially update OrderAlpha : {}", orderAlpha);

        return orderAlphaRepository
            .findById(orderAlpha.getId())
            .map(existingOrderAlpha -> {
                if (orderAlpha.getOrderDate() != null) {
                    existingOrderAlpha.setOrderDate(orderAlpha.getOrderDate());
                }
                if (orderAlpha.getTotalPrice() != null) {
                    existingOrderAlpha.setTotalPrice(orderAlpha.getTotalPrice());
                }
                if (orderAlpha.getStatus() != null) {
                    existingOrderAlpha.setStatus(orderAlpha.getStatus());
                }

                return existingOrderAlpha;
            })
            .map(orderAlphaRepository::save);
    }

    /**
     * Get all the orderAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return orderAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderAlpha> findOne(Long id) {
        LOG.debug("Request to get OrderAlpha : {}", id);
        return orderAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderAlpha : {}", id);
        orderAlphaRepository.deleteById(id);
    }
}
