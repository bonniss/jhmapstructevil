package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.MasterTenant;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
public interface MasterTenantService {
    /**
     * Save a masterTenant.
     *
     * @param masterTenant the entity to save.
     * @return the persisted entity.
     */
    MasterTenant save(MasterTenant masterTenant);

    /**
     * Updates a masterTenant.
     *
     * @param masterTenant the entity to update.
     * @return the persisted entity.
     */
    MasterTenant update(MasterTenant masterTenant);

    /**
     * Partially updates a masterTenant.
     *
     * @param masterTenant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterTenant> partialUpdate(MasterTenant masterTenant);

    /**
     * Get all the masterTenants.
     *
     * @return the list of entities.
     */
    List<MasterTenant> findAll();

    /**
     * Get the "id" masterTenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterTenant> findOne(Long id);

    /**
     * Delete the "id" masterTenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
