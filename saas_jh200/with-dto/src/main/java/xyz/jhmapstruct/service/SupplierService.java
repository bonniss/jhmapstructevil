package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Supplier;
import xyz.jhmapstruct.repository.SupplierRepository;
import xyz.jhmapstruct.service.dto.SupplierDTO;
import xyz.jhmapstruct.service.mapper.SupplierMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Supplier}.
 */
@Service
@Transactional
public class SupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierDTO save(SupplierDTO supplierDTO) {
        LOG.debug("Request to save Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    /**
     * Update a supplier.
     *
     * @param supplierDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierDTO update(SupplierDTO supplierDTO) {
        LOG.debug("Request to update Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    /**
     * Partially update a supplier.
     *
     * @param supplierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO) {
        LOG.debug("Request to partially update Supplier : {}", supplierDTO);

        return supplierRepository
            .findById(supplierDTO.getId())
            .map(existingSupplier -> {
                supplierMapper.partialUpdate(existingSupplier, supplierDTO);

                return existingSupplier;
            })
            .map(supplierRepository::save)
            .map(supplierMapper::toDto);
    }

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierRepository.findAllWithEagerRelationships(pageable).map(supplierMapper::toDto);
    }

    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierDTO> findOne(Long id) {
        LOG.debug("Request to get Supplier : {}", id);
        return supplierRepository.findOneWithEagerRelationships(id).map(supplierMapper::toDto);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
