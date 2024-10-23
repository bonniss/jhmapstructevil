package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.mapper.MasterTenantMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
@Service
@Transactional
public class MasterTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantService.class);

    private final MasterTenantRepository masterTenantRepository;

    private final MasterTenantMapper masterTenantMapper;

    public MasterTenantService(MasterTenantRepository masterTenantRepository, MasterTenantMapper masterTenantMapper) {
        this.masterTenantRepository = masterTenantRepository;
        this.masterTenantMapper = masterTenantMapper;
    }

    /**
     * Save a masterTenant.
     *
     * @param masterTenantDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterTenantDTO save(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to save MasterTenant : {}", masterTenantDTO);
        MasterTenant masterTenant = masterTenantMapper.toEntity(masterTenantDTO);
        masterTenant = masterTenantRepository.save(masterTenant);
        return masterTenantMapper.toDto(masterTenant);
    }

    /**
     * Update a masterTenant.
     *
     * @param masterTenantDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterTenantDTO update(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to update MasterTenant : {}", masterTenantDTO);
        MasterTenant masterTenant = masterTenantMapper.toEntity(masterTenantDTO);
        masterTenant = masterTenantRepository.save(masterTenant);
        return masterTenantMapper.toDto(masterTenant);
    }

    /**
     * Partially update a masterTenant.
     *
     * @param masterTenantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MasterTenantDTO> partialUpdate(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to partially update MasterTenant : {}", masterTenantDTO);

        return masterTenantRepository
            .findById(masterTenantDTO.getId())
            .map(existingMasterTenant -> {
                masterTenantMapper.partialUpdate(existingMasterTenant, masterTenantDTO);

                return existingMasterTenant;
            })
            .map(masterTenantRepository::save)
            .map(masterTenantMapper::toDto);
    }

    /**
     * Get one masterTenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MasterTenantDTO> findOne(Long id) {
        LOG.debug("Request to get MasterTenant : {}", id);
        return masterTenantRepository.findById(id).map(masterTenantMapper::toDto);
    }

    /**
     * Delete the masterTenant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MasterTenant : {}", id);
        masterTenantRepository.deleteById(id);
    }
}
