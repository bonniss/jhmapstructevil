package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CustomerVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
public interface CustomerViService {
    /**
     * Save a customerVi.
     *
     * @param customerVi the entity to save.
     * @return the persisted entity.
     */
    CustomerVi save(CustomerVi customerVi);

    /**
     * Updates a customerVi.
     *
     * @param customerVi the entity to update.
     * @return the persisted entity.
     */
    CustomerVi update(CustomerVi customerVi);

    /**
     * Partially updates a customerVi.
     *
     * @param customerVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerVi> partialUpdate(CustomerVi customerVi);

    /**
     * Get all the customerVis.
     *
     * @return the list of entities.
     */
    List<CustomerVi> findAll();

    /**
     * Get the "id" customerVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerVi> findOne(Long id);

    /**
     * Delete the "id" customerVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
