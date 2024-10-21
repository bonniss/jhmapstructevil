package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CustomerMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
public interface CustomerMiService {
    /**
     * Save a customerMi.
     *
     * @param customerMi the entity to save.
     * @return the persisted entity.
     */
    CustomerMi save(CustomerMi customerMi);

    /**
     * Updates a customerMi.
     *
     * @param customerMi the entity to update.
     * @return the persisted entity.
     */
    CustomerMi update(CustomerMi customerMi);

    /**
     * Partially updates a customerMi.
     *
     * @param customerMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerMi> partialUpdate(CustomerMi customerMi);

    /**
     * Get all the customerMis.
     *
     * @return the list of entities.
     */
    List<CustomerMi> findAll();

    /**
     * Get the "id" customerMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerMi> findOne(Long id);

    /**
     * Delete the "id" customerMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
