package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.service.mapper.OrderViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
@Service
@Transactional
public class OrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViService.class);

    private final OrderViRepository orderViRepository;

    private final OrderViMapper orderViMapper;

    public OrderViService(OrderViRepository orderViRepository, OrderViMapper orderViMapper) {
        this.orderViRepository = orderViRepository;
        this.orderViMapper = orderViMapper;
    }

    /**
     * Save a orderVi.
     *
     * @param orderViDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderViDTO save(OrderViDTO orderViDTO) {
        LOG.debug("Request to save OrderVi : {}", orderViDTO);
        OrderVi orderVi = orderViMapper.toEntity(orderViDTO);
        orderVi = orderViRepository.save(orderVi);
        return orderViMapper.toDto(orderVi);
    }

    /**
     * Update a orderVi.
     *
     * @param orderViDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderViDTO update(OrderViDTO orderViDTO) {
        LOG.debug("Request to update OrderVi : {}", orderViDTO);
        OrderVi orderVi = orderViMapper.toEntity(orderViDTO);
        orderVi = orderViRepository.save(orderVi);
        return orderViMapper.toDto(orderVi);
    }

    /**
     * Partially update a orderVi.
     *
     * @param orderViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderViDTO> partialUpdate(OrderViDTO orderViDTO) {
        LOG.debug("Request to partially update OrderVi : {}", orderViDTO);

        return orderViRepository
            .findById(orderViDTO.getId())
            .map(existingOrderVi -> {
                orderViMapper.partialUpdate(existingOrderVi, orderViDTO);

                return existingOrderVi;
            })
            .map(orderViRepository::save)
            .map(orderViMapper::toDto);
    }

    /**
     * Get all the orderVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderViRepository.findAllWithEagerRelationships(pageable).map(orderViMapper::toDto);
    }

    /**
     * Get one orderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderViDTO> findOne(Long id) {
        LOG.debug("Request to get OrderVi : {}", id);
        return orderViRepository.findOneWithEagerRelationships(id).map(orderViMapper::toDto);
    }

    /**
     * Delete the orderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderVi : {}", id);
        orderViRepository.deleteById(id);
    }
}
