package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
import xyz.jhmapstruct.service.mapper.OrderMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
@Service
@Transactional
public class OrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiService.class);

    private final OrderMiRepository orderMiRepository;

    private final OrderMiMapper orderMiMapper;

    public OrderMiService(OrderMiRepository orderMiRepository, OrderMiMapper orderMiMapper) {
        this.orderMiRepository = orderMiRepository;
        this.orderMiMapper = orderMiMapper;
    }

    /**
     * Save a orderMi.
     *
     * @param orderMiDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderMiDTO save(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to save OrderMi : {}", orderMiDTO);
        OrderMi orderMi = orderMiMapper.toEntity(orderMiDTO);
        orderMi = orderMiRepository.save(orderMi);
        return orderMiMapper.toDto(orderMi);
    }

    /**
     * Update a orderMi.
     *
     * @param orderMiDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderMiDTO update(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to update OrderMi : {}", orderMiDTO);
        OrderMi orderMi = orderMiMapper.toEntity(orderMiDTO);
        orderMi = orderMiRepository.save(orderMi);
        return orderMiMapper.toDto(orderMi);
    }

    /**
     * Partially update a orderMi.
     *
     * @param orderMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderMiDTO> partialUpdate(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to partially update OrderMi : {}", orderMiDTO);

        return orderMiRepository
            .findById(orderMiDTO.getId())
            .map(existingOrderMi -> {
                orderMiMapper.partialUpdate(existingOrderMi, orderMiDTO);

                return existingOrderMi;
            })
            .map(orderMiRepository::save)
            .map(orderMiMapper::toDto);
    }

    /**
     * Get all the orderMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiRepository.findAllWithEagerRelationships(pageable).map(orderMiMapper::toDto);
    }

    /**
     * Get one orderMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderMiDTO> findOne(Long id) {
        LOG.debug("Request to get OrderMi : {}", id);
        return orderMiRepository.findOneWithEagerRelationships(id).map(orderMiMapper::toDto);
    }

    /**
     * Delete the orderMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMi : {}", id);
        orderMiRepository.deleteById(id);
    }
}
