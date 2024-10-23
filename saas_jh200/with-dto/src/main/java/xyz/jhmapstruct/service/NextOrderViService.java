package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.repository.NextOrderViRepository;
import xyz.jhmapstruct.service.dto.NextOrderViDTO;
import xyz.jhmapstruct.service.mapper.NextOrderViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderVi}.
 */
@Service
@Transactional
public class NextOrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViService.class);

    private final NextOrderViRepository nextOrderViRepository;

    private final NextOrderViMapper nextOrderViMapper;

    public NextOrderViService(NextOrderViRepository nextOrderViRepository, NextOrderViMapper nextOrderViMapper) {
        this.nextOrderViRepository = nextOrderViRepository;
        this.nextOrderViMapper = nextOrderViMapper;
    }

    /**
     * Save a nextOrderVi.
     *
     * @param nextOrderViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViDTO save(NextOrderViDTO nextOrderViDTO) {
        LOG.debug("Request to save NextOrderVi : {}", nextOrderViDTO);
        NextOrderVi nextOrderVi = nextOrderViMapper.toEntity(nextOrderViDTO);
        nextOrderVi = nextOrderViRepository.save(nextOrderVi);
        return nextOrderViMapper.toDto(nextOrderVi);
    }

    /**
     * Update a nextOrderVi.
     *
     * @param nextOrderViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViDTO update(NextOrderViDTO nextOrderViDTO) {
        LOG.debug("Request to update NextOrderVi : {}", nextOrderViDTO);
        NextOrderVi nextOrderVi = nextOrderViMapper.toEntity(nextOrderViDTO);
        nextOrderVi = nextOrderViRepository.save(nextOrderVi);
        return nextOrderViMapper.toDto(nextOrderVi);
    }

    /**
     * Partially update a nextOrderVi.
     *
     * @param nextOrderViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderViDTO> partialUpdate(NextOrderViDTO nextOrderViDTO) {
        LOG.debug("Request to partially update NextOrderVi : {}", nextOrderViDTO);

        return nextOrderViRepository
            .findById(nextOrderViDTO.getId())
            .map(existingNextOrderVi -> {
                nextOrderViMapper.partialUpdate(existingNextOrderVi, nextOrderViDTO);

                return existingNextOrderVi;
            })
            .map(nextOrderViRepository::save)
            .map(nextOrderViMapper::toDto);
    }

    /**
     * Get all the nextOrderVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderViRepository.findAllWithEagerRelationships(pageable).map(nextOrderViMapper::toDto);
    }

    /**
     * Get one nextOrderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderViDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderVi : {}", id);
        return nextOrderViRepository.findOneWithEagerRelationships(id).map(nextOrderViMapper::toDto);
    }

    /**
     * Delete the nextOrderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderVi : {}", id);
        nextOrderViRepository.deleteById(id);
    }
}
