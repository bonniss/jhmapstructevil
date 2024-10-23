package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.repository.NextProductViViRepository;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductViVi}.
 */
@Service
@Transactional
public class NextProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViViService.class);

    private final NextProductViViRepository nextProductViViRepository;

    private final NextProductViViMapper nextProductViViMapper;

    public NextProductViViService(NextProductViViRepository nextProductViViRepository, NextProductViViMapper nextProductViViMapper) {
        this.nextProductViViRepository = nextProductViViRepository;
        this.nextProductViViMapper = nextProductViViMapper;
    }

    /**
     * Save a nextProductViVi.
     *
     * @param nextProductViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductViViDTO save(NextProductViViDTO nextProductViViDTO) {
        LOG.debug("Request to save NextProductViVi : {}", nextProductViViDTO);
        NextProductViVi nextProductViVi = nextProductViViMapper.toEntity(nextProductViViDTO);
        nextProductViVi = nextProductViViRepository.save(nextProductViVi);
        return nextProductViViMapper.toDto(nextProductViVi);
    }

    /**
     * Update a nextProductViVi.
     *
     * @param nextProductViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductViViDTO update(NextProductViViDTO nextProductViViDTO) {
        LOG.debug("Request to update NextProductViVi : {}", nextProductViViDTO);
        NextProductViVi nextProductViVi = nextProductViViMapper.toEntity(nextProductViViDTO);
        nextProductViVi = nextProductViViRepository.save(nextProductViVi);
        return nextProductViViMapper.toDto(nextProductViVi);
    }

    /**
     * Partially update a nextProductViVi.
     *
     * @param nextProductViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductViViDTO> partialUpdate(NextProductViViDTO nextProductViViDTO) {
        LOG.debug("Request to partially update NextProductViVi : {}", nextProductViViDTO);

        return nextProductViViRepository
            .findById(nextProductViViDTO.getId())
            .map(existingNextProductViVi -> {
                nextProductViViMapper.partialUpdate(existingNextProductViVi, nextProductViViDTO);

                return existingNextProductViVi;
            })
            .map(nextProductViViRepository::save)
            .map(nextProductViViMapper::toDto);
    }

    /**
     * Get all the nextProductViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductViViRepository.findAllWithEagerRelationships(pageable).map(nextProductViViMapper::toDto);
    }

    /**
     * Get one nextProductViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductViVi : {}", id);
        return nextProductViViRepository.findOneWithEagerRelationships(id).map(nextProductViViMapper::toDto);
    }

    /**
     * Delete the nextProductViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductViVi : {}", id);
        nextProductViViRepository.deleteById(id);
    }
}
