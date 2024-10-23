package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.repository.SupplierGammaRepository;
import xyz.jhmapstruct.service.dto.SupplierGammaDTO;
import xyz.jhmapstruct.service.mapper.SupplierGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierGamma}.
 */
@Service
@Transactional
public class SupplierGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierGammaService.class);

    private final SupplierGammaRepository supplierGammaRepository;

    private final SupplierGammaMapper supplierGammaMapper;

    public SupplierGammaService(SupplierGammaRepository supplierGammaRepository, SupplierGammaMapper supplierGammaMapper) {
        this.supplierGammaRepository = supplierGammaRepository;
        this.supplierGammaMapper = supplierGammaMapper;
    }

    /**
     * Save a supplierGamma.
     *
     * @param supplierGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierGammaDTO save(SupplierGammaDTO supplierGammaDTO) {
        LOG.debug("Request to save SupplierGamma : {}", supplierGammaDTO);
        SupplierGamma supplierGamma = supplierGammaMapper.toEntity(supplierGammaDTO);
        supplierGamma = supplierGammaRepository.save(supplierGamma);
        return supplierGammaMapper.toDto(supplierGamma);
    }

    /**
     * Update a supplierGamma.
     *
     * @param supplierGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierGammaDTO update(SupplierGammaDTO supplierGammaDTO) {
        LOG.debug("Request to update SupplierGamma : {}", supplierGammaDTO);
        SupplierGamma supplierGamma = supplierGammaMapper.toEntity(supplierGammaDTO);
        supplierGamma = supplierGammaRepository.save(supplierGamma);
        return supplierGammaMapper.toDto(supplierGamma);
    }

    /**
     * Partially update a supplierGamma.
     *
     * @param supplierGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierGammaDTO> partialUpdate(SupplierGammaDTO supplierGammaDTO) {
        LOG.debug("Request to partially update SupplierGamma : {}", supplierGammaDTO);

        return supplierGammaRepository
            .findById(supplierGammaDTO.getId())
            .map(existingSupplierGamma -> {
                supplierGammaMapper.partialUpdate(existingSupplierGamma, supplierGammaDTO);

                return existingSupplierGamma;
            })
            .map(supplierGammaRepository::save)
            .map(supplierGammaMapper::toDto);
    }

    /**
     * Get all the supplierGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierGammaRepository.findAllWithEagerRelationships(pageable).map(supplierGammaMapper::toDto);
    }

    /**
     * Get one supplierGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierGammaDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierGamma : {}", id);
        return supplierGammaRepository.findOneWithEagerRelationships(id).map(supplierGammaMapper::toDto);
    }

    /**
     * Delete the supplierGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierGamma : {}", id);
        supplierGammaRepository.deleteById(id);
    }
}
