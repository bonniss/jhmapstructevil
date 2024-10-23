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
import xyz.jhmapstruct.service.dto.OrderSigmaDTO;
import xyz.jhmapstruct.service.mapper.OrderSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderSigma}.
 */
@Service
@Transactional
public class OrderSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderSigmaService.class);

    private final OrderSigmaRepository orderSigmaRepository;

    private final OrderSigmaMapper orderSigmaMapper;

    public OrderSigmaService(OrderSigmaRepository orderSigmaRepository, OrderSigmaMapper orderSigmaMapper) {
        this.orderSigmaRepository = orderSigmaRepository;
        this.orderSigmaMapper = orderSigmaMapper;
    }

    /**
     * Save a orderSigma.
     *
     * @param orderSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderSigmaDTO save(OrderSigmaDTO orderSigmaDTO) {
        LOG.debug("Request to save OrderSigma : {}", orderSigmaDTO);
        OrderSigma orderSigma = orderSigmaMapper.toEntity(orderSigmaDTO);
        orderSigma = orderSigmaRepository.save(orderSigma);
        return orderSigmaMapper.toDto(orderSigma);
    }

    /**
     * Update a orderSigma.
     *
     * @param orderSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderSigmaDTO update(OrderSigmaDTO orderSigmaDTO) {
        LOG.debug("Request to update OrderSigma : {}", orderSigmaDTO);
        OrderSigma orderSigma = orderSigmaMapper.toEntity(orderSigmaDTO);
        orderSigma = orderSigmaRepository.save(orderSigma);
        return orderSigmaMapper.toDto(orderSigma);
    }

    /**
     * Partially update a orderSigma.
     *
     * @param orderSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderSigmaDTO> partialUpdate(OrderSigmaDTO orderSigmaDTO) {
        LOG.debug("Request to partially update OrderSigma : {}", orderSigmaDTO);

        return orderSigmaRepository
            .findById(orderSigmaDTO.getId())
            .map(existingOrderSigma -> {
                orderSigmaMapper.partialUpdate(existingOrderSigma, orderSigmaDTO);

                return existingOrderSigma;
            })
            .map(orderSigmaRepository::save)
            .map(orderSigmaMapper::toDto);
    }

    /**
     * Get all the orderSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderSigmaRepository.findAllWithEagerRelationships(pageable).map(orderSigmaMapper::toDto);
    }

    /**
     * Get one orderSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get OrderSigma : {}", id);
        return orderSigmaRepository.findOneWithEagerRelationships(id).map(orderSigmaMapper::toDto);
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
