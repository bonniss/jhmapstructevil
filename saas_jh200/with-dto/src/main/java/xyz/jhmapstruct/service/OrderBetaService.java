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
import xyz.jhmapstruct.service.dto.OrderBetaDTO;
import xyz.jhmapstruct.service.mapper.OrderBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderBeta}.
 */
@Service
@Transactional
public class OrderBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBetaService.class);

    private final OrderBetaRepository orderBetaRepository;

    private final OrderBetaMapper orderBetaMapper;

    public OrderBetaService(OrderBetaRepository orderBetaRepository, OrderBetaMapper orderBetaMapper) {
        this.orderBetaRepository = orderBetaRepository;
        this.orderBetaMapper = orderBetaMapper;
    }

    /**
     * Save a orderBeta.
     *
     * @param orderBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderBetaDTO save(OrderBetaDTO orderBetaDTO) {
        LOG.debug("Request to save OrderBeta : {}", orderBetaDTO);
        OrderBeta orderBeta = orderBetaMapper.toEntity(orderBetaDTO);
        orderBeta = orderBetaRepository.save(orderBeta);
        return orderBetaMapper.toDto(orderBeta);
    }

    /**
     * Update a orderBeta.
     *
     * @param orderBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderBetaDTO update(OrderBetaDTO orderBetaDTO) {
        LOG.debug("Request to update OrderBeta : {}", orderBetaDTO);
        OrderBeta orderBeta = orderBetaMapper.toEntity(orderBetaDTO);
        orderBeta = orderBetaRepository.save(orderBeta);
        return orderBetaMapper.toDto(orderBeta);
    }

    /**
     * Partially update a orderBeta.
     *
     * @param orderBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderBetaDTO> partialUpdate(OrderBetaDTO orderBetaDTO) {
        LOG.debug("Request to partially update OrderBeta : {}", orderBetaDTO);

        return orderBetaRepository
            .findById(orderBetaDTO.getId())
            .map(existingOrderBeta -> {
                orderBetaMapper.partialUpdate(existingOrderBeta, orderBetaDTO);

                return existingOrderBeta;
            })
            .map(orderBetaRepository::save)
            .map(orderBetaMapper::toDto);
    }

    /**
     * Get all the orderBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderBetaRepository.findAllWithEagerRelationships(pageable).map(orderBetaMapper::toDto);
    }

    /**
     * Get one orderBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderBetaDTO> findOne(Long id) {
        LOG.debug("Request to get OrderBeta : {}", id);
        return orderBetaRepository.findOneWithEagerRelationships(id).map(orderBetaMapper::toDto);
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
