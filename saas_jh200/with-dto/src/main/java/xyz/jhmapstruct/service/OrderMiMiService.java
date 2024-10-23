package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.service.mapper.OrderMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
@Service
@Transactional
public class OrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiService.class);

    private final OrderMiMiRepository orderMiMiRepository;

    private final OrderMiMiMapper orderMiMiMapper;

    public OrderMiMiService(OrderMiMiRepository orderMiMiRepository, OrderMiMiMapper orderMiMiMapper) {
        this.orderMiMiRepository = orderMiMiRepository;
        this.orderMiMiMapper = orderMiMiMapper;
    }

    /**
     * Save a orderMiMi.
     *
     * @param orderMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderMiMiDTO save(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to save OrderMiMi : {}", orderMiMiDTO);
        OrderMiMi orderMiMi = orderMiMiMapper.toEntity(orderMiMiDTO);
        orderMiMi = orderMiMiRepository.save(orderMiMi);
        return orderMiMiMapper.toDto(orderMiMi);
    }

    /**
     * Update a orderMiMi.
     *
     * @param orderMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderMiMiDTO update(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to update OrderMiMi : {}", orderMiMiDTO);
        OrderMiMi orderMiMi = orderMiMiMapper.toEntity(orderMiMiDTO);
        orderMiMi = orderMiMiRepository.save(orderMiMi);
        return orderMiMiMapper.toDto(orderMiMi);
    }

    /**
     * Partially update a orderMiMi.
     *
     * @param orderMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderMiMiDTO> partialUpdate(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to partially update OrderMiMi : {}", orderMiMiDTO);

        return orderMiMiRepository
            .findById(orderMiMiDTO.getId())
            .map(existingOrderMiMi -> {
                orderMiMiMapper.partialUpdate(existingOrderMiMi, orderMiMiDTO);

                return existingOrderMiMi;
            })
            .map(orderMiMiRepository::save)
            .map(orderMiMiMapper::toDto);
    }

    /**
     * Get all the orderMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiMiRepository.findAllWithEagerRelationships(pageable).map(orderMiMiMapper::toDto);
    }

    /**
     * Get one orderMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get OrderMiMi : {}", id);
        return orderMiMiRepository.findOneWithEagerRelationships(id).map(orderMiMiMapper::toDto);
    }

    /**
     * Delete the orderMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMiMi : {}", id);
        orderMiMiRepository.deleteById(id);
    }
}
