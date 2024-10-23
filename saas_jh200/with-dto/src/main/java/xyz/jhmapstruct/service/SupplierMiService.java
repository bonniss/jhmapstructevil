package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;
import xyz.jhmapstruct.service.mapper.SupplierMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
@Service
@Transactional
public class SupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiService.class);

    private final SupplierMiRepository supplierMiRepository;

    private final SupplierMiMapper supplierMiMapper;

    public SupplierMiService(SupplierMiRepository supplierMiRepository, SupplierMiMapper supplierMiMapper) {
        this.supplierMiRepository = supplierMiRepository;
        this.supplierMiMapper = supplierMiMapper;
    }

    /**
     * Save a supplierMi.
     *
     * @param supplierMiDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiDTO save(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to save SupplierMi : {}", supplierMiDTO);
        SupplierMi supplierMi = supplierMiMapper.toEntity(supplierMiDTO);
        supplierMi = supplierMiRepository.save(supplierMi);
        return supplierMiMapper.toDto(supplierMi);
    }

    /**
     * Update a supplierMi.
     *
     * @param supplierMiDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiDTO update(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to update SupplierMi : {}", supplierMiDTO);
        SupplierMi supplierMi = supplierMiMapper.toEntity(supplierMiDTO);
        supplierMi = supplierMiRepository.save(supplierMi);
        return supplierMiMapper.toDto(supplierMi);
    }

    /**
     * Partially update a supplierMi.
     *
     * @param supplierMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierMiDTO> partialUpdate(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to partially update SupplierMi : {}", supplierMiDTO);

        return supplierMiRepository
            .findById(supplierMiDTO.getId())
            .map(existingSupplierMi -> {
                supplierMiMapper.partialUpdate(existingSupplierMi, supplierMiDTO);

                return existingSupplierMi;
            })
            .map(supplierMiRepository::save)
            .map(supplierMiMapper::toDto);
    }

    /**
     * Get all the supplierMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiRepository.findAllWithEagerRelationships(pageable).map(supplierMiMapper::toDto);
    }

    /**
     * Get one supplierMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierMiDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierMi : {}", id);
        return supplierMiRepository.findOneWithEagerRelationships(id).map(supplierMiMapper::toDto);
    }

    /**
     * Delete the supplierMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMi : {}", id);
        supplierMiRepository.deleteById(id);
    }
}
