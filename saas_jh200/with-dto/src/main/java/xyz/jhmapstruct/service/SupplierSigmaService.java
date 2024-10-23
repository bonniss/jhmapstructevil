package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.repository.SupplierSigmaRepository;
import xyz.jhmapstruct.service.dto.SupplierSigmaDTO;
import xyz.jhmapstruct.service.mapper.SupplierSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierSigma}.
 */
@Service
@Transactional
public class SupplierSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierSigmaService.class);

    private final SupplierSigmaRepository supplierSigmaRepository;

    private final SupplierSigmaMapper supplierSigmaMapper;

    public SupplierSigmaService(SupplierSigmaRepository supplierSigmaRepository, SupplierSigmaMapper supplierSigmaMapper) {
        this.supplierSigmaRepository = supplierSigmaRepository;
        this.supplierSigmaMapper = supplierSigmaMapper;
    }

    /**
     * Save a supplierSigma.
     *
     * @param supplierSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierSigmaDTO save(SupplierSigmaDTO supplierSigmaDTO) {
        LOG.debug("Request to save SupplierSigma : {}", supplierSigmaDTO);
        SupplierSigma supplierSigma = supplierSigmaMapper.toEntity(supplierSigmaDTO);
        supplierSigma = supplierSigmaRepository.save(supplierSigma);
        return supplierSigmaMapper.toDto(supplierSigma);
    }

    /**
     * Update a supplierSigma.
     *
     * @param supplierSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierSigmaDTO update(SupplierSigmaDTO supplierSigmaDTO) {
        LOG.debug("Request to update SupplierSigma : {}", supplierSigmaDTO);
        SupplierSigma supplierSigma = supplierSigmaMapper.toEntity(supplierSigmaDTO);
        supplierSigma = supplierSigmaRepository.save(supplierSigma);
        return supplierSigmaMapper.toDto(supplierSigma);
    }

    /**
     * Partially update a supplierSigma.
     *
     * @param supplierSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierSigmaDTO> partialUpdate(SupplierSigmaDTO supplierSigmaDTO) {
        LOG.debug("Request to partially update SupplierSigma : {}", supplierSigmaDTO);

        return supplierSigmaRepository
            .findById(supplierSigmaDTO.getId())
            .map(existingSupplierSigma -> {
                supplierSigmaMapper.partialUpdate(existingSupplierSigma, supplierSigmaDTO);

                return existingSupplierSigma;
            })
            .map(supplierSigmaRepository::save)
            .map(supplierSigmaMapper::toDto);
    }

    /**
     * Get all the supplierSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierSigmaRepository.findAllWithEagerRelationships(pageable).map(supplierSigmaMapper::toDto);
    }

    /**
     * Get one supplierSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierSigma : {}", id);
        return supplierSigmaRepository.findOneWithEagerRelationships(id).map(supplierSigmaMapper::toDto);
    }

    /**
     * Delete the supplierSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierSigma : {}", id);
        supplierSigmaRepository.deleteById(id);
    }
}
