package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.SupplierAlphaRepository;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;
import xyz.jhmapstruct.service.mapper.SupplierAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierAlpha}.
 */
@Service
@Transactional
public class SupplierAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierAlphaService.class);

    private final SupplierAlphaRepository supplierAlphaRepository;

    private final SupplierAlphaMapper supplierAlphaMapper;

    public SupplierAlphaService(SupplierAlphaRepository supplierAlphaRepository, SupplierAlphaMapper supplierAlphaMapper) {
        this.supplierAlphaRepository = supplierAlphaRepository;
        this.supplierAlphaMapper = supplierAlphaMapper;
    }

    /**
     * Save a supplierAlpha.
     *
     * @param supplierAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierAlphaDTO save(SupplierAlphaDTO supplierAlphaDTO) {
        LOG.debug("Request to save SupplierAlpha : {}", supplierAlphaDTO);
        SupplierAlpha supplierAlpha = supplierAlphaMapper.toEntity(supplierAlphaDTO);
        supplierAlpha = supplierAlphaRepository.save(supplierAlpha);
        return supplierAlphaMapper.toDto(supplierAlpha);
    }

    /**
     * Update a supplierAlpha.
     *
     * @param supplierAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierAlphaDTO update(SupplierAlphaDTO supplierAlphaDTO) {
        LOG.debug("Request to update SupplierAlpha : {}", supplierAlphaDTO);
        SupplierAlpha supplierAlpha = supplierAlphaMapper.toEntity(supplierAlphaDTO);
        supplierAlpha = supplierAlphaRepository.save(supplierAlpha);
        return supplierAlphaMapper.toDto(supplierAlpha);
    }

    /**
     * Partially update a supplierAlpha.
     *
     * @param supplierAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierAlphaDTO> partialUpdate(SupplierAlphaDTO supplierAlphaDTO) {
        LOG.debug("Request to partially update SupplierAlpha : {}", supplierAlphaDTO);

        return supplierAlphaRepository
            .findById(supplierAlphaDTO.getId())
            .map(existingSupplierAlpha -> {
                supplierAlphaMapper.partialUpdate(existingSupplierAlpha, supplierAlphaDTO);

                return existingSupplierAlpha;
            })
            .map(supplierAlphaRepository::save)
            .map(supplierAlphaMapper::toDto);
    }

    /**
     * Get all the supplierAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierAlphaRepository.findAllWithEagerRelationships(pageable).map(supplierAlphaMapper::toDto);
    }

    /**
     * Get one supplierAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierAlpha : {}", id);
        return supplierAlphaRepository.findOneWithEagerRelationships(id).map(supplierAlphaMapper::toDto);
    }

    /**
     * Delete the supplierAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierAlpha : {}", id);
        supplierAlphaRepository.deleteById(id);
    }
}
