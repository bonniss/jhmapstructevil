package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.dto.SupplierViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@Service
@Transactional
public class SupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViService.class);

    private final SupplierViRepository supplierViRepository;

    private final SupplierViMapper supplierViMapper;

    public SupplierViService(SupplierViRepository supplierViRepository, SupplierViMapper supplierViMapper) {
        this.supplierViRepository = supplierViRepository;
        this.supplierViMapper = supplierViMapper;
    }

    /**
     * Save a supplierVi.
     *
     * @param supplierViDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierViDTO save(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to save SupplierVi : {}", supplierViDTO);
        SupplierVi supplierVi = supplierViMapper.toEntity(supplierViDTO);
        supplierVi = supplierViRepository.save(supplierVi);
        return supplierViMapper.toDto(supplierVi);
    }

    /**
     * Update a supplierVi.
     *
     * @param supplierViDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierViDTO update(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to update SupplierVi : {}", supplierViDTO);
        SupplierVi supplierVi = supplierViMapper.toEntity(supplierViDTO);
        supplierVi = supplierViRepository.save(supplierVi);
        return supplierViMapper.toDto(supplierVi);
    }

    /**
     * Partially update a supplierVi.
     *
     * @param supplierViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierViDTO> partialUpdate(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to partially update SupplierVi : {}", supplierViDTO);

        return supplierViRepository
            .findById(supplierViDTO.getId())
            .map(existingSupplierVi -> {
                supplierViMapper.partialUpdate(existingSupplierVi, supplierViDTO);

                return existingSupplierVi;
            })
            .map(supplierViRepository::save)
            .map(supplierViMapper::toDto);
    }

    /**
     * Get all the supplierVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViRepository.findAllWithEagerRelationships(pageable).map(supplierViMapper::toDto);
    }

    /**
     * Get one supplierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierViDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierVi : {}", id);
        return supplierViRepository.findOneWithEagerRelationships(id).map(supplierViMapper::toDto);
    }

    /**
     * Delete the supplierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierVi : {}", id);
        supplierViRepository.deleteById(id);
    }
}
