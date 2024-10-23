package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.repository.NextOrderRepository;
import xyz.jhmapstruct.service.dto.NextOrderDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrder}.
 */
@Service
@Transactional
public class NextOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderService.class);

    private final NextOrderRepository nextOrderRepository;

    private final NextOrderMapper nextOrderMapper;

    public NextOrderService(NextOrderRepository nextOrderRepository, NextOrderMapper nextOrderMapper) {
        this.nextOrderRepository = nextOrderRepository;
        this.nextOrderMapper = nextOrderMapper;
    }

    /**
     * Save a nextOrder.
     *
     * @param nextOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderDTO save(NextOrderDTO nextOrderDTO) {
        LOG.debug("Request to save NextOrder : {}", nextOrderDTO);
        NextOrder nextOrder = nextOrderMapper.toEntity(nextOrderDTO);
        nextOrder = nextOrderRepository.save(nextOrder);
        return nextOrderMapper.toDto(nextOrder);
    }

    /**
     * Update a nextOrder.
     *
     * @param nextOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderDTO update(NextOrderDTO nextOrderDTO) {
        LOG.debug("Request to update NextOrder : {}", nextOrderDTO);
        NextOrder nextOrder = nextOrderMapper.toEntity(nextOrderDTO);
        nextOrder = nextOrderRepository.save(nextOrder);
        return nextOrderMapper.toDto(nextOrder);
    }

    /**
     * Partially update a nextOrder.
     *
     * @param nextOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderDTO> partialUpdate(NextOrderDTO nextOrderDTO) {
        LOG.debug("Request to partially update NextOrder : {}", nextOrderDTO);

        return nextOrderRepository
            .findById(nextOrderDTO.getId())
            .map(existingNextOrder -> {
                nextOrderMapper.partialUpdate(existingNextOrder, nextOrderDTO);

                return existingNextOrder;
            })
            .map(nextOrderRepository::save)
            .map(nextOrderMapper::toDto);
    }

    /**
     * Get all the nextOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderRepository.findAllWithEagerRelationships(pageable).map(nextOrderMapper::toDto);
    }

    /**
     * Get one nextOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrder : {}", id);
        return nextOrderRepository.findOneWithEagerRelationships(id).map(nextOrderMapper::toDto);
    }

    /**
     * Delete the nextOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrder : {}", id);
        nextOrderRepository.deleteById(id);
    }
}
