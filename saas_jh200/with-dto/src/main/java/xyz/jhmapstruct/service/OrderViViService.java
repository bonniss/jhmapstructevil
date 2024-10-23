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
import xyz.jhmapstruct.service.dto.OrderViViDTO;
import xyz.jhmapstruct.service.mapper.OrderViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
@Service
@Transactional
public class OrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViService.class);

    private final OrderViViRepository orderViViRepository;

    private final OrderViViMapper orderViViMapper;

    public OrderViViService(OrderViViRepository orderViViRepository, OrderViViMapper orderViViMapper) {
        this.orderViViRepository = orderViViRepository;
        this.orderViViMapper = orderViViMapper;
    }

    /**
     * Save a orderViVi.
     *
     * @param orderViViDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderViViDTO save(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to save OrderViVi : {}", orderViViDTO);
        OrderViVi orderViVi = orderViViMapper.toEntity(orderViViDTO);
        orderViVi = orderViViRepository.save(orderViVi);
        return orderViViMapper.toDto(orderViVi);
    }

    /**
     * Update a orderViVi.
     *
     * @param orderViViDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderViViDTO update(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to update OrderViVi : {}", orderViViDTO);
        OrderViVi orderViVi = orderViViMapper.toEntity(orderViViDTO);
        orderViVi = orderViViRepository.save(orderViVi);
        return orderViViMapper.toDto(orderViVi);
    }

    /**
     * Partially update a orderViVi.
     *
     * @param orderViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderViViDTO> partialUpdate(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to partially update OrderViVi : {}", orderViViDTO);

        return orderViViRepository
            .findById(orderViViDTO.getId())
            .map(existingOrderViVi -> {
                orderViViMapper.partialUpdate(existingOrderViVi, orderViViDTO);

                return existingOrderViVi;
            })
            .map(orderViViRepository::save)
            .map(orderViViMapper::toDto);
    }

    /**
     * Get all the orderViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderViViRepository.findAllWithEagerRelationships(pageable).map(orderViViMapper::toDto);
    }

    /**
     * Get one orderViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderViViDTO> findOne(Long id) {
        LOG.debug("Request to get OrderViVi : {}", id);
        return orderViViRepository.findOneWithEagerRelationships(id).map(orderViViMapper::toDto);
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
