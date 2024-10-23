package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.SupplierThetaRepository;
import xyz.jhmapstruct.service.dto.SupplierThetaDTO;
import xyz.jhmapstruct.service.mapper.SupplierThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierTheta}.
 */
@Service
@Transactional
public class SupplierThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierThetaService.class);

    private final SupplierThetaRepository supplierThetaRepository;

    private final SupplierThetaMapper supplierThetaMapper;

    public SupplierThetaService(SupplierThetaRepository supplierThetaRepository, SupplierThetaMapper supplierThetaMapper) {
        this.supplierThetaRepository = supplierThetaRepository;
        this.supplierThetaMapper = supplierThetaMapper;
    }

    /**
     * Save a supplierTheta.
     *
     * @param supplierThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierThetaDTO save(SupplierThetaDTO supplierThetaDTO) {
        LOG.debug("Request to save SupplierTheta : {}", supplierThetaDTO);
        SupplierTheta supplierTheta = supplierThetaMapper.toEntity(supplierThetaDTO);
        supplierTheta = supplierThetaRepository.save(supplierTheta);
        return supplierThetaMapper.toDto(supplierTheta);
    }

    /**
     * Update a supplierTheta.
     *
     * @param supplierThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierThetaDTO update(SupplierThetaDTO supplierThetaDTO) {
        LOG.debug("Request to update SupplierTheta : {}", supplierThetaDTO);
        SupplierTheta supplierTheta = supplierThetaMapper.toEntity(supplierThetaDTO);
        supplierTheta = supplierThetaRepository.save(supplierTheta);
        return supplierThetaMapper.toDto(supplierTheta);
    }

    /**
     * Partially update a supplierTheta.
     *
     * @param supplierThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierThetaDTO> partialUpdate(SupplierThetaDTO supplierThetaDTO) {
        LOG.debug("Request to partially update SupplierTheta : {}", supplierThetaDTO);

        return supplierThetaRepository
            .findById(supplierThetaDTO.getId())
            .map(existingSupplierTheta -> {
                supplierThetaMapper.partialUpdate(existingSupplierTheta, supplierThetaDTO);

                return existingSupplierTheta;
            })
            .map(supplierThetaRepository::save)
            .map(supplierThetaMapper::toDto);
    }

    /**
     * Get all the supplierThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierThetaRepository.findAllWithEagerRelationships(pageable).map(supplierThetaMapper::toDto);
    }

    /**
     * Get one supplierTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierThetaDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierTheta : {}", id);
        return supplierThetaRepository.findOneWithEagerRelationships(id).map(supplierThetaMapper::toDto);
    }

    /**
     * Delete the supplierTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierTheta : {}", id);
        supplierThetaRepository.deleteById(id);
    }
}
