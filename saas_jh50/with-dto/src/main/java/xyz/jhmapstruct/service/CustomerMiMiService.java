package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
public interface CustomerMiMiService {
    /**
     * Save a customerMiMi.
     *
     * @param customerMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerMiMiDTO save(CustomerMiMiDTO customerMiMiDTO);

    /**
     * Updates a customerMiMi.
     *
     * @param customerMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerMiMiDTO update(CustomerMiMiDTO customerMiMiDTO);

    /**
     * Partially updates a customerMiMi.
     *
     * @param customerMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerMiMiDTO> partialUpdate(CustomerMiMiDTO customerMiMiDTO);

    /**
     * Get all the customerMiMis.
     *
     * @return the list of entities.
     */
    List<CustomerMiMiDTO> findAll();

    /**
     * Get the "id" customerMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" customerMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
