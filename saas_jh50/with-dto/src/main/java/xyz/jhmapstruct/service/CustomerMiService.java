package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
public interface CustomerMiService {
    /**
     * Save a customerMi.
     *
     * @param customerMiDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerMiDTO save(CustomerMiDTO customerMiDTO);

    /**
     * Updates a customerMi.
     *
     * @param customerMiDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerMiDTO update(CustomerMiDTO customerMiDTO);

    /**
     * Partially updates a customerMi.
     *
     * @param customerMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerMiDTO> partialUpdate(CustomerMiDTO customerMiDTO);

    /**
     * Get all the customerMis.
     *
     * @return the list of entities.
     */
    List<CustomerMiDTO> findAll();

    /**
     * Get the "id" customerMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerMiDTO> findOne(Long id);

    /**
     * Delete the "id" customerMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
