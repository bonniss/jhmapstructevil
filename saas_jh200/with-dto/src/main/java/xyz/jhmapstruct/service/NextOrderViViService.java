package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.repository.NextOrderViViRepository;
import xyz.jhmapstruct.service.dto.NextOrderViViDTO;
import xyz.jhmapstruct.service.mapper.NextOrderViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderViVi}.
 */
@Service
@Transactional
public class NextOrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViViService.class);

    private final NextOrderViViRepository nextOrderViViRepository;

    private final NextOrderViViMapper nextOrderViViMapper;

    public NextOrderViViService(NextOrderViViRepository nextOrderViViRepository, NextOrderViViMapper nextOrderViViMapper) {
        this.nextOrderViViRepository = nextOrderViViRepository;
        this.nextOrderViViMapper = nextOrderViViMapper;
    }

    /**
     * Save a nextOrderViVi.
     *
     * @param nextOrderViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViViDTO save(NextOrderViViDTO nextOrderViViDTO) {
        LOG.debug("Request to save NextOrderViVi : {}", nextOrderViViDTO);
        NextOrderViVi nextOrderViVi = nextOrderViViMapper.toEntity(nextOrderViViDTO);
        nextOrderViVi = nextOrderViViRepository.save(nextOrderViVi);
        return nextOrderViViMapper.toDto(nextOrderViVi);
    }

    /**
     * Update a nextOrderViVi.
     *
     * @param nextOrderViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderViViDTO update(NextOrderViViDTO nextOrderViViDTO) {
        LOG.debug("Request to update NextOrderViVi : {}", nextOrderViViDTO);
        NextOrderViVi nextOrderViVi = nextOrderViViMapper.toEntity(nextOrderViViDTO);
        nextOrderViVi = nextOrderViViRepository.save(nextOrderViVi);
        return nextOrderViViMapper.toDto(nextOrderViVi);
    }

    /**
     * Partially update a nextOrderViVi.
     *
     * @param nextOrderViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderViViDTO> partialUpdate(NextOrderViViDTO nextOrderViViDTO) {
        LOG.debug("Request to partially update NextOrderViVi : {}", nextOrderViViDTO);

        return nextOrderViViRepository
            .findById(nextOrderViViDTO.getId())
            .map(existingNextOrderViVi -> {
                nextOrderViViMapper.partialUpdate(existingNextOrderViVi, nextOrderViViDTO);

                return existingNextOrderViVi;
            })
            .map(nextOrderViViRepository::save)
            .map(nextOrderViViMapper::toDto);
    }

    /**
     * Get all the nextOrderViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderViViRepository.findAllWithEagerRelationships(pageable).map(nextOrderViViMapper::toDto);
    }

    /**
     * Get one nextOrderViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderViVi : {}", id);
        return nextOrderViViRepository.findOneWithEagerRelationships(id).map(nextOrderViViMapper::toDto);
    }

    /**
     * Delete the nextOrderViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderViVi : {}", id);
        nextOrderViViRepository.deleteById(id);
    }
}
