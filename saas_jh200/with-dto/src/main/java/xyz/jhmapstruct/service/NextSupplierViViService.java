package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.repository.NextSupplierViViRepository;
import xyz.jhmapstruct.service.dto.NextSupplierViViDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierViVi}.
 */
@Service
@Transactional
public class NextSupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViViService.class);

    private final NextSupplierViViRepository nextSupplierViViRepository;

    private final NextSupplierViViMapper nextSupplierViViMapper;

    public NextSupplierViViService(NextSupplierViViRepository nextSupplierViViRepository, NextSupplierViViMapper nextSupplierViViMapper) {
        this.nextSupplierViViRepository = nextSupplierViViRepository;
        this.nextSupplierViViMapper = nextSupplierViViMapper;
    }

    /**
     * Save a nextSupplierViVi.
     *
     * @param nextSupplierViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViViDTO save(NextSupplierViViDTO nextSupplierViViDTO) {
        LOG.debug("Request to save NextSupplierViVi : {}", nextSupplierViViDTO);
        NextSupplierViVi nextSupplierViVi = nextSupplierViViMapper.toEntity(nextSupplierViViDTO);
        nextSupplierViVi = nextSupplierViViRepository.save(nextSupplierViVi);
        return nextSupplierViViMapper.toDto(nextSupplierViVi);
    }

    /**
     * Update a nextSupplierViVi.
     *
     * @param nextSupplierViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViViDTO update(NextSupplierViViDTO nextSupplierViViDTO) {
        LOG.debug("Request to update NextSupplierViVi : {}", nextSupplierViViDTO);
        NextSupplierViVi nextSupplierViVi = nextSupplierViViMapper.toEntity(nextSupplierViViDTO);
        nextSupplierViVi = nextSupplierViViRepository.save(nextSupplierViVi);
        return nextSupplierViViMapper.toDto(nextSupplierViVi);
    }

    /**
     * Partially update a nextSupplierViVi.
     *
     * @param nextSupplierViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierViViDTO> partialUpdate(NextSupplierViViDTO nextSupplierViViDTO) {
        LOG.debug("Request to partially update NextSupplierViVi : {}", nextSupplierViViDTO);

        return nextSupplierViViRepository
            .findById(nextSupplierViViDTO.getId())
            .map(existingNextSupplierViVi -> {
                nextSupplierViViMapper.partialUpdate(existingNextSupplierViVi, nextSupplierViViDTO);

                return existingNextSupplierViVi;
            })
            .map(nextSupplierViViRepository::save)
            .map(nextSupplierViViMapper::toDto);
    }

    /**
     * Get all the nextSupplierViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierViViRepository.findAllWithEagerRelationships(pageable).map(nextSupplierViViMapper::toDto);
    }

    /**
     * Get one nextSupplierViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierViVi : {}", id);
        return nextSupplierViViRepository.findOneWithEagerRelationships(id).map(nextSupplierViViMapper::toDto);
    }

    /**
     * Delete the nextSupplierViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierViVi : {}", id);
        nextSupplierViViRepository.deleteById(id);
    }
}
