package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextSupplierRepository;
import xyz.jhmapstruct.service.dto.NextSupplierDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplier}.
 */
@Service
@Transactional
public class NextSupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierService.class);

    private final NextSupplierRepository nextSupplierRepository;

    private final NextSupplierMapper nextSupplierMapper;

    public NextSupplierService(NextSupplierRepository nextSupplierRepository, NextSupplierMapper nextSupplierMapper) {
        this.nextSupplierRepository = nextSupplierRepository;
        this.nextSupplierMapper = nextSupplierMapper;
    }

    /**
     * Save a nextSupplier.
     *
     * @param nextSupplierDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierDTO save(NextSupplierDTO nextSupplierDTO) {
        LOG.debug("Request to save NextSupplier : {}", nextSupplierDTO);
        NextSupplier nextSupplier = nextSupplierMapper.toEntity(nextSupplierDTO);
        nextSupplier = nextSupplierRepository.save(nextSupplier);
        return nextSupplierMapper.toDto(nextSupplier);
    }

    /**
     * Update a nextSupplier.
     *
     * @param nextSupplierDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierDTO update(NextSupplierDTO nextSupplierDTO) {
        LOG.debug("Request to update NextSupplier : {}", nextSupplierDTO);
        NextSupplier nextSupplier = nextSupplierMapper.toEntity(nextSupplierDTO);
        nextSupplier = nextSupplierRepository.save(nextSupplier);
        return nextSupplierMapper.toDto(nextSupplier);
    }

    /**
     * Partially update a nextSupplier.
     *
     * @param nextSupplierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierDTO> partialUpdate(NextSupplierDTO nextSupplierDTO) {
        LOG.debug("Request to partially update NextSupplier : {}", nextSupplierDTO);

        return nextSupplierRepository
            .findById(nextSupplierDTO.getId())
            .map(existingNextSupplier -> {
                nextSupplierMapper.partialUpdate(existingNextSupplier, nextSupplierDTO);

                return existingNextSupplier;
            })
            .map(nextSupplierRepository::save)
            .map(nextSupplierMapper::toDto);
    }

    /**
     * Get all the nextSuppliers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierRepository.findAllWithEagerRelationships(pageable).map(nextSupplierMapper::toDto);
    }

    /**
     * Get one nextSupplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplier : {}", id);
        return nextSupplierRepository.findOneWithEagerRelationships(id).map(nextSupplierMapper::toDto);
    }

    /**
     * Delete the nextSupplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplier : {}", id);
        nextSupplierRepository.deleteById(id);
    }
}
