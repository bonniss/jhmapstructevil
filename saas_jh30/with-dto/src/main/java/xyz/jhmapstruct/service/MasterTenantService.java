package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
public interface MasterTenantService {
    /**
     * Save a masterTenant.
     *
     * @param masterTenantDTO the entity to save.
     * @return the persisted entity.
     */
    MasterTenantDTO save(MasterTenantDTO masterTenantDTO);

    /**
     * Updates a masterTenant.
     *
     * @param masterTenantDTO the entity to update.
     * @return the persisted entity.
     */
    MasterTenantDTO update(MasterTenantDTO masterTenantDTO);

    /**
     * Partially updates a masterTenant.
     *
     * @param masterTenantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterTenantDTO> partialUpdate(MasterTenantDTO masterTenantDTO);

    /**
     * Get all the masterTenants.
     *
     * @return the list of entities.
     */
    List<MasterTenantDTO> findAll();

    /**
     * Get the "id" masterTenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterTenantDTO> findOne(Long id);

    /**
     * Delete the "id" masterTenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
