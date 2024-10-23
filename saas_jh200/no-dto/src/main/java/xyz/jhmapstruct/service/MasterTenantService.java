package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
@Service
@Transactional
public class MasterTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantService.class);

    private final MasterTenantRepository masterTenantRepository;

    public MasterTenantService(MasterTenantRepository masterTenantRepository) {
        this.masterTenantRepository = masterTenantRepository;
    }

    /**
     * Save a masterTenant.
     *
     * @param masterTenant the entity to save.
     * @return the persisted entity.
     */
    public MasterTenant save(MasterTenant masterTenant) {
        LOG.debug("Request to save MasterTenant : {}", masterTenant);
        return masterTenantRepository.save(masterTenant);
    }

    /**
     * Update a masterTenant.
     *
     * @param masterTenant the entity to save.
     * @return the persisted entity.
     */
    public MasterTenant update(MasterTenant masterTenant) {
        LOG.debug("Request to update MasterTenant : {}", masterTenant);
        return masterTenantRepository.save(masterTenant);
    }

    /**
     * Partially update a masterTenant.
     *
     * @param masterTenant the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MasterTenant> partialUpdate(MasterTenant masterTenant) {
        LOG.debug("Request to partially update MasterTenant : {}", masterTenant);

        return masterTenantRepository
            .findById(masterTenant.getId())
            .map(existingMasterTenant -> {
                if (masterTenant.getCode() != null) {
                    existingMasterTenant.setCode(masterTenant.getCode());
                }

                return existingMasterTenant;
            })
            .map(masterTenantRepository::save);
    }

    /**
     * Get one masterTenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MasterTenant> findOne(Long id) {
        LOG.debug("Request to get MasterTenant : {}", id);
        return masterTenantRepository.findById(id);
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
