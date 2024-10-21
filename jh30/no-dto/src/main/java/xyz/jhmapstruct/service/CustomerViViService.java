package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CustomerViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
public interface CustomerViViService {
    /**
     * Save a customerViVi.
     *
     * @param customerViVi the entity to save.
     * @return the persisted entity.
     */
    CustomerViVi save(CustomerViVi customerViVi);

    /**
     * Updates a customerViVi.
     *
     * @param customerViVi the entity to update.
     * @return the persisted entity.
     */
    CustomerViVi update(CustomerViVi customerViVi);

    /**
     * Partially updates a customerViVi.
     *
     * @param customerViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerViVi> partialUpdate(CustomerViVi customerViVi);

    /**
     * Get all the customerViVis.
     *
     * @return the list of entities.
     */
    List<CustomerViVi> findAll();

    /**
     * Get the "id" customerViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerViVi> findOne(Long id);

    /**
     * Delete the "id" customerViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
