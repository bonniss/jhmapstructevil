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
import xyz.jhmapstruct.service.dto.OrderGammaDTO;
import xyz.jhmapstruct.service.mapper.OrderGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderGamma}.
 */
@Service
@Transactional
public class OrderGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderGammaService.class);

    private final OrderGammaRepository orderGammaRepository;

    private final OrderGammaMapper orderGammaMapper;

    public OrderGammaService(OrderGammaRepository orderGammaRepository, OrderGammaMapper orderGammaMapper) {
        this.orderGammaRepository = orderGammaRepository;
        this.orderGammaMapper = orderGammaMapper;
    }

    /**
     * Save a orderGamma.
     *
     * @param orderGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderGammaDTO save(OrderGammaDTO orderGammaDTO) {
        LOG.debug("Request to save OrderGamma : {}", orderGammaDTO);
        OrderGamma orderGamma = orderGammaMapper.toEntity(orderGammaDTO);
        orderGamma = orderGammaRepository.save(orderGamma);
        return orderGammaMapper.toDto(orderGamma);
    }

    /**
     * Update a orderGamma.
     *
     * @param orderGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderGammaDTO update(OrderGammaDTO orderGammaDTO) {
        LOG.debug("Request to update OrderGamma : {}", orderGammaDTO);
        OrderGamma orderGamma = orderGammaMapper.toEntity(orderGammaDTO);
        orderGamma = orderGammaRepository.save(orderGamma);
        return orderGammaMapper.toDto(orderGamma);
    }

    /**
     * Partially update a orderGamma.
     *
     * @param orderGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderGammaDTO> partialUpdate(OrderGammaDTO orderGammaDTO) {
        LOG.debug("Request to partially update OrderGamma : {}", orderGammaDTO);

        return orderGammaRepository
            .findById(orderGammaDTO.getId())
            .map(existingOrderGamma -> {
                orderGammaMapper.partialUpdate(existingOrderGamma, orderGammaDTO);

                return existingOrderGamma;
            })
            .map(orderGammaRepository::save)
            .map(orderGammaMapper::toDto);
    }

    /**
     * Get all the orderGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderGammaRepository.findAllWithEagerRelationships(pageable).map(orderGammaMapper::toDto);
    }

    /**
     * Get one orderGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderGammaDTO> findOne(Long id) {
        LOG.debug("Request to get OrderGamma : {}", id);
        return orderGammaRepository.findOneWithEagerRelationships(id).map(orderGammaMapper::toDto);
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
