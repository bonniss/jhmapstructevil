package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
@Service
@Transactional
public class SupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViService.class);

    private final SupplierViViRepository supplierViViRepository;

    private final SupplierViViMapper supplierViViMapper;

    public SupplierViViService(SupplierViViRepository supplierViViRepository, SupplierViViMapper supplierViViMapper) {
        this.supplierViViRepository = supplierViViRepository;
        this.supplierViViMapper = supplierViViMapper;
    }

    /**
     * Save a supplierViVi.
     *
     * @param supplierViViDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierViViDTO save(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to save SupplierViVi : {}", supplierViViDTO);
        SupplierViVi supplierViVi = supplierViViMapper.toEntity(supplierViViDTO);
        supplierViVi = supplierViViRepository.save(supplierViVi);
        return supplierViViMapper.toDto(supplierViVi);
    }

    /**
     * Update a supplierViVi.
     *
     * @param supplierViViDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierViViDTO update(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to update SupplierViVi : {}", supplierViViDTO);
        SupplierViVi supplierViVi = supplierViViMapper.toEntity(supplierViViDTO);
        supplierViVi = supplierViViRepository.save(supplierViVi);
        return supplierViViMapper.toDto(supplierViVi);
    }

    /**
     * Partially update a supplierViVi.
     *
     * @param supplierViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierViViDTO> partialUpdate(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to partially update SupplierViVi : {}", supplierViViDTO);

        return supplierViViRepository
            .findById(supplierViViDTO.getId())
            .map(existingSupplierViVi -> {
                supplierViViMapper.partialUpdate(existingSupplierViVi, supplierViViDTO);

                return existingSupplierViVi;
            })
            .map(supplierViViRepository::save)
            .map(supplierViViMapper::toDto);
    }

    /**
     * Get all the supplierViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViViRepository.findAllWithEagerRelationships(pageable).map(supplierViViMapper::toDto);
    }

    /**
     * Get one supplierViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierViViDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierViVi : {}", id);
        return supplierViViRepository.findOneWithEagerRelationships(id).map(supplierViViMapper::toDto);
    }

    /**
     * Delete the supplierViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierViVi : {}", id);
        supplierViViRepository.deleteById(id);
    }
}
