package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.repository.OrderThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderTheta}.
 */
@Service
@Transactional
public class OrderThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderThetaService.class);

    private final OrderThetaRepository orderThetaRepository;

    public OrderThetaService(OrderThetaRepository orderThetaRepository) {
        this.orderThetaRepository = orderThetaRepository;
    }

    /**
     * Save a orderTheta.
     *
     * @param orderTheta the entity to save.
     * @return the persisted entity.
     */
    public OrderTheta save(OrderTheta orderTheta) {
        LOG.debug("Request to save OrderTheta : {}", orderTheta);
        return orderThetaRepository.save(orderTheta);
    }

    /**
     * Update a orderTheta.
     *
     * @param orderTheta the entity to save.
     * @return the persisted entity.
     */
    public OrderTheta update(OrderTheta orderTheta) {
        LOG.debug("Request to update OrderTheta : {}", orderTheta);
        return orderThetaRepository.save(orderTheta);
    }

    /**
     * Partially update a orderTheta.
     *
     * @param orderTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderTheta> partialUpdate(OrderTheta orderTheta) {
        LOG.debug("Request to partially update OrderTheta : {}", orderTheta);

        return orderThetaRepository
            .findById(orderTheta.getId())
            .map(existingOrderTheta -> {
                if (orderTheta.getOrderDate() != null) {
                    existingOrderTheta.setOrderDate(orderTheta.getOrderDate());
                }
                if (orderTheta.getTotalPrice() != null) {
                    existingOrderTheta.setTotalPrice(orderTheta.getTotalPrice());
                }
                if (orderTheta.getStatus() != null) {
                    existingOrderTheta.setStatus(orderTheta.getStatus());
                }

                return existingOrderTheta;
            })
            .map(orderThetaRepository::save);
    }

    /**
     * Get all the orderThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderTheta> findAllWithEagerRelationships(Pageable pageable) {
        return orderThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderTheta> findOne(Long id) {
        LOG.debug("Request to get OrderTheta : {}", id);
        return orderThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderTheta : {}", id);
        orderThetaRepository.deleteById(id);
    }
}
