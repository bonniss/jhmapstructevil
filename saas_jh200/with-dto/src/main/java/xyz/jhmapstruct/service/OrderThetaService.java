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
import xyz.jhmapstruct.service.dto.OrderThetaDTO;
import xyz.jhmapstruct.service.mapper.OrderThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderTheta}.
 */
@Service
@Transactional
public class OrderThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderThetaService.class);

    private final OrderThetaRepository orderThetaRepository;

    private final OrderThetaMapper orderThetaMapper;

    public OrderThetaService(OrderThetaRepository orderThetaRepository, OrderThetaMapper orderThetaMapper) {
        this.orderThetaRepository = orderThetaRepository;
        this.orderThetaMapper = orderThetaMapper;
    }

    /**
     * Save a orderTheta.
     *
     * @param orderThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderThetaDTO save(OrderThetaDTO orderThetaDTO) {
        LOG.debug("Request to save OrderTheta : {}", orderThetaDTO);
        OrderTheta orderTheta = orderThetaMapper.toEntity(orderThetaDTO);
        orderTheta = orderThetaRepository.save(orderTheta);
        return orderThetaMapper.toDto(orderTheta);
    }

    /**
     * Update a orderTheta.
     *
     * @param orderThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderThetaDTO update(OrderThetaDTO orderThetaDTO) {
        LOG.debug("Request to update OrderTheta : {}", orderThetaDTO);
        OrderTheta orderTheta = orderThetaMapper.toEntity(orderThetaDTO);
        orderTheta = orderThetaRepository.save(orderTheta);
        return orderThetaMapper.toDto(orderTheta);
    }

    /**
     * Partially update a orderTheta.
     *
     * @param orderThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderThetaDTO> partialUpdate(OrderThetaDTO orderThetaDTO) {
        LOG.debug("Request to partially update OrderTheta : {}", orderThetaDTO);

        return orderThetaRepository
            .findById(orderThetaDTO.getId())
            .map(existingOrderTheta -> {
                orderThetaMapper.partialUpdate(existingOrderTheta, orderThetaDTO);

                return existingOrderTheta;
            })
            .map(orderThetaRepository::save)
            .map(orderThetaMapper::toDto);
    }

    /**
     * Get all the orderThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderThetaRepository.findAllWithEagerRelationships(pageable).map(orderThetaMapper::toDto);
    }

    /**
     * Get one orderTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderThetaDTO> findOne(Long id) {
        LOG.debug("Request to get OrderTheta : {}", id);
        return orderThetaRepository.findOneWithEagerRelationships(id).map(orderThetaMapper::toDto);
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
