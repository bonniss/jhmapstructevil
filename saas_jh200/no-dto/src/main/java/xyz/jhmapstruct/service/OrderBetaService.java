package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.repository.OrderBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderBeta}.
 */
@Service
@Transactional
public class OrderBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBetaService.class);

    private final OrderBetaRepository orderBetaRepository;

    public OrderBetaService(OrderBetaRepository orderBetaRepository) {
        this.orderBetaRepository = orderBetaRepository;
    }

    /**
     * Save a orderBeta.
     *
     * @param orderBeta the entity to save.
     * @return the persisted entity.
     */
    public OrderBeta save(OrderBeta orderBeta) {
        LOG.debug("Request to save OrderBeta : {}", orderBeta);
        return orderBetaRepository.save(orderBeta);
    }

    /**
     * Update a orderBeta.
     *
     * @param orderBeta the entity to save.
     * @return the persisted entity.
     */
    public OrderBeta update(OrderBeta orderBeta) {
        LOG.debug("Request to update OrderBeta : {}", orderBeta);
        return orderBetaRepository.save(orderBeta);
    }

    /**
     * Partially update a orderBeta.
     *
     * @param orderBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderBeta> partialUpdate(OrderBeta orderBeta) {
        LOG.debug("Request to partially update OrderBeta : {}", orderBeta);

        return orderBetaRepository
            .findById(orderBeta.getId())
            .map(existingOrderBeta -> {
                if (orderBeta.getOrderDate() != null) {
                    existingOrderBeta.setOrderDate(orderBeta.getOrderDate());
                }
                if (orderBeta.getTotalPrice() != null) {
                    existingOrderBeta.setTotalPrice(orderBeta.getTotalPrice());
                }
                if (orderBeta.getStatus() != null) {
                    existingOrderBeta.setStatus(orderBeta.getStatus());
                }

                return existingOrderBeta;
            })
            .map(orderBetaRepository::save);
    }

    /**
     * Get all the orderBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderBeta> findAllWithEagerRelationships(Pageable pageable) {
        return orderBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderBeta> findOne(Long id) {
        LOG.debug("Request to get OrderBeta : {}", id);
        return orderBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderBeta : {}", id);
        orderBetaRepository.deleteById(id);
    }
}
