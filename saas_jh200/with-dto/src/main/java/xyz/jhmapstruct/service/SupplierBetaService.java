package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.repository.SupplierBetaRepository;
import xyz.jhmapstruct.service.dto.SupplierBetaDTO;
import xyz.jhmapstruct.service.mapper.SupplierBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierBeta}.
 */
@Service
@Transactional
public class SupplierBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierBetaService.class);

    private final SupplierBetaRepository supplierBetaRepository;

    private final SupplierBetaMapper supplierBetaMapper;

    public SupplierBetaService(SupplierBetaRepository supplierBetaRepository, SupplierBetaMapper supplierBetaMapper) {
        this.supplierBetaRepository = supplierBetaRepository;
        this.supplierBetaMapper = supplierBetaMapper;
    }

    /**
     * Save a supplierBeta.
     *
     * @param supplierBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierBetaDTO save(SupplierBetaDTO supplierBetaDTO) {
        LOG.debug("Request to save SupplierBeta : {}", supplierBetaDTO);
        SupplierBeta supplierBeta = supplierBetaMapper.toEntity(supplierBetaDTO);
        supplierBeta = supplierBetaRepository.save(supplierBeta);
        return supplierBetaMapper.toDto(supplierBeta);
    }

    /**
     * Update a supplierBeta.
     *
     * @param supplierBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierBetaDTO update(SupplierBetaDTO supplierBetaDTO) {
        LOG.debug("Request to update SupplierBeta : {}", supplierBetaDTO);
        SupplierBeta supplierBeta = supplierBetaMapper.toEntity(supplierBetaDTO);
        supplierBeta = supplierBetaRepository.save(supplierBeta);
        return supplierBetaMapper.toDto(supplierBeta);
    }

    /**
     * Partially update a supplierBeta.
     *
     * @param supplierBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierBetaDTO> partialUpdate(SupplierBetaDTO supplierBetaDTO) {
        LOG.debug("Request to partially update SupplierBeta : {}", supplierBetaDTO);

        return supplierBetaRepository
            .findById(supplierBetaDTO.getId())
            .map(existingSupplierBeta -> {
                supplierBetaMapper.partialUpdate(existingSupplierBeta, supplierBetaDTO);

                return existingSupplierBeta;
            })
            .map(supplierBetaRepository::save)
            .map(supplierBetaMapper::toDto);
    }

    /**
     * Get all the supplierBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierBetaRepository.findAllWithEagerRelationships(pageable).map(supplierBetaMapper::toDto);
    }

    /**
     * Get one supplierBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierBetaDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierBeta : {}", id);
        return supplierBetaRepository.findOneWithEagerRelationships(id).map(supplierBetaMapper::toDto);
    }

    /**
     * Delete the supplierBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierBeta : {}", id);
        supplierBetaRepository.deleteById(id);
    }
}
