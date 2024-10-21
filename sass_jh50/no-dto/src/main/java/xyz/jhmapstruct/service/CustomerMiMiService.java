package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CustomerMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
public interface CustomerMiMiService {
    /**
     * Save a customerMiMi.
     *
     * @param customerMiMi the entity to save.
     * @return the persisted entity.
     */
    CustomerMiMi save(CustomerMiMi customerMiMi);

    /**
     * Updates a customerMiMi.
     *
     * @param customerMiMi the entity to update.
     * @return the persisted entity.
     */
    CustomerMiMi update(CustomerMiMi customerMiMi);

    /**
     * Partially updates a customerMiMi.
     *
     * @param customerMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerMiMi> partialUpdate(CustomerMiMi customerMiMi);

    /**
     * Get all the customerMiMis.
     *
     * @return the list of entities.
     */
    List<CustomerMiMi> findAll();

    /**
     * Get the "id" customerMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerMiMi> findOne(Long id);

    /**
     * Delete the "id" customerMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
