package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.repository.NextSupplierViRepository;
import xyz.jhmapstruct.service.dto.NextSupplierViDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierVi}.
 */
@Service
@Transactional
public class NextSupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViService.class);

    private final NextSupplierViRepository nextSupplierViRepository;

    private final NextSupplierViMapper nextSupplierViMapper;

    public NextSupplierViService(NextSupplierViRepository nextSupplierViRepository, NextSupplierViMapper nextSupplierViMapper) {
        this.nextSupplierViRepository = nextSupplierViRepository;
        this.nextSupplierViMapper = nextSupplierViMapper;
    }

    /**
     * Save a nextSupplierVi.
     *
     * @param nextSupplierViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViDTO save(NextSupplierViDTO nextSupplierViDTO) {
        LOG.debug("Request to save NextSupplierVi : {}", nextSupplierViDTO);
        NextSupplierVi nextSupplierVi = nextSupplierViMapper.toEntity(nextSupplierViDTO);
        nextSupplierVi = nextSupplierViRepository.save(nextSupplierVi);
        return nextSupplierViMapper.toDto(nextSupplierVi);
    }

    /**
     * Update a nextSupplierVi.
     *
     * @param nextSupplierViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViDTO update(NextSupplierViDTO nextSupplierViDTO) {
        LOG.debug("Request to update NextSupplierVi : {}", nextSupplierViDTO);
        NextSupplierVi nextSupplierVi = nextSupplierViMapper.toEntity(nextSupplierViDTO);
        nextSupplierVi = nextSupplierViRepository.save(nextSupplierVi);
        return nextSupplierViMapper.toDto(nextSupplierVi);
    }

    /**
     * Partially update a nextSupplierVi.
     *
     * @param nextSupplierViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierViDTO> partialUpdate(NextSupplierViDTO nextSupplierViDTO) {
        LOG.debug("Request to partially update NextSupplierVi : {}", nextSupplierViDTO);

        return nextSupplierViRepository
            .findById(nextSupplierViDTO.getId())
            .map(existingNextSupplierVi -> {
                nextSupplierViMapper.partialUpdate(existingNextSupplierVi, nextSupplierViDTO);

                return existingNextSupplierVi;
            })
            .map(nextSupplierViRepository::save)
            .map(nextSupplierViMapper::toDto);
    }

    /**
     * Get all the nextSupplierVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierViRepository.findAllWithEagerRelationships(pageable).map(nextSupplierViMapper::toDto);
    }

    /**
     * Get one nextSupplierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierViDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierVi : {}", id);
        return nextSupplierViRepository.findOneWithEagerRelationships(id).map(nextSupplierViMapper::toDto);
    }

    /**
     * Delete the nextSupplierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierVi : {}", id);
        nextSupplierViRepository.deleteById(id);
    }
}
