package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;
import xyz.jhmapstruct.service.mapper.SupplierMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
@Service
@Transactional
public class SupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiService.class);

    private final SupplierMiMiRepository supplierMiMiRepository;

    private final SupplierMiMiMapper supplierMiMiMapper;

    public SupplierMiMiService(SupplierMiMiRepository supplierMiMiRepository, SupplierMiMiMapper supplierMiMiMapper) {
        this.supplierMiMiRepository = supplierMiMiRepository;
        this.supplierMiMiMapper = supplierMiMiMapper;
    }

    /**
     * Save a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiMiDTO save(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to save SupplierMiMi : {}", supplierMiMiDTO);
        SupplierMiMi supplierMiMi = supplierMiMiMapper.toEntity(supplierMiMiDTO);
        supplierMiMi = supplierMiMiRepository.save(supplierMiMi);
        return supplierMiMiMapper.toDto(supplierMiMi);
    }

    /**
     * Update a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiMiDTO update(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to update SupplierMiMi : {}", supplierMiMiDTO);
        SupplierMiMi supplierMiMi = supplierMiMiMapper.toEntity(supplierMiMiDTO);
        supplierMiMi = supplierMiMiRepository.save(supplierMiMi);
        return supplierMiMiMapper.toDto(supplierMiMi);
    }

    /**
     * Partially update a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierMiMiDTO> partialUpdate(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to partially update SupplierMiMi : {}", supplierMiMiDTO);

        return supplierMiMiRepository
            .findById(supplierMiMiDTO.getId())
            .map(existingSupplierMiMi -> {
                supplierMiMiMapper.partialUpdate(existingSupplierMiMi, supplierMiMiDTO);

                return existingSupplierMiMi;
            })
            .map(supplierMiMiRepository::save)
            .map(supplierMiMiMapper::toDto);
    }

    /**
     * Get all the supplierMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiMiRepository.findAllWithEagerRelationships(pageable).map(supplierMiMiMapper::toDto);
    }

    /**
     * Get one supplierMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierMiMi : {}", id);
        return supplierMiMiRepository.findOneWithEagerRelationships(id).map(supplierMiMiMapper::toDto);
    }

    /**
     * Delete the supplierMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMiMi : {}", id);
        supplierMiMiRepository.deleteById(id);
    }
}
