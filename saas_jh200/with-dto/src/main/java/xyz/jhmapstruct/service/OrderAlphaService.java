package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.repository.OrderAlphaRepository;
import xyz.jhmapstruct.service.dto.OrderAlphaDTO;
import xyz.jhmapstruct.service.mapper.OrderAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderAlpha}.
 */
@Service
@Transactional
public class OrderAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAlphaService.class);

    private final OrderAlphaRepository orderAlphaRepository;

    private final OrderAlphaMapper orderAlphaMapper;

    public OrderAlphaService(OrderAlphaRepository orderAlphaRepository, OrderAlphaMapper orderAlphaMapper) {
        this.orderAlphaRepository = orderAlphaRepository;
        this.orderAlphaMapper = orderAlphaMapper;
    }

    /**
     * Save a orderAlpha.
     *
     * @param orderAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderAlphaDTO save(OrderAlphaDTO orderAlphaDTO) {
        LOG.debug("Request to save OrderAlpha : {}", orderAlphaDTO);
        OrderAlpha orderAlpha = orderAlphaMapper.toEntity(orderAlphaDTO);
        orderAlpha = orderAlphaRepository.save(orderAlpha);
        return orderAlphaMapper.toDto(orderAlpha);
    }

    /**
     * Update a orderAlpha.
     *
     * @param orderAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderAlphaDTO update(OrderAlphaDTO orderAlphaDTO) {
        LOG.debug("Request to update OrderAlpha : {}", orderAlphaDTO);
        OrderAlpha orderAlpha = orderAlphaMapper.toEntity(orderAlphaDTO);
        orderAlpha = orderAlphaRepository.save(orderAlpha);
        return orderAlphaMapper.toDto(orderAlpha);
    }

    /**
     * Partially update a orderAlpha.
     *
     * @param orderAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderAlphaDTO> partialUpdate(OrderAlphaDTO orderAlphaDTO) {
        LOG.debug("Request to partially update OrderAlpha : {}", orderAlphaDTO);

        return orderAlphaRepository
            .findById(orderAlphaDTO.getId())
            .map(existingOrderAlpha -> {
                orderAlphaMapper.partialUpdate(existingOrderAlpha, orderAlphaDTO);

                return existingOrderAlpha;
            })
            .map(orderAlphaRepository::save)
            .map(orderAlphaMapper::toDto);
    }

    /**
     * Get all the orderAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderAlphaRepository.findAllWithEagerRelationships(pageable).map(orderAlphaMapper::toDto);
    }

    /**
     * Get one orderAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get OrderAlpha : {}", id);
        return orderAlphaRepository.findOneWithEagerRelationships(id).map(orderAlphaMapper::toDto);
    }

    /**
     * Delete the orderAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderAlpha : {}", id);
        orderAlphaRepository.deleteById(id);
    }
}
