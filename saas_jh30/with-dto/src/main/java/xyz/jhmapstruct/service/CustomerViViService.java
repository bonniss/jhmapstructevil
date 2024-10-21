package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
public interface CustomerViViService {
    /**
     * Save a customerViVi.
     *
     * @param customerViViDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerViViDTO save(CustomerViViDTO customerViViDTO);

    /**
     * Updates a customerViVi.
     *
     * @param customerViViDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerViViDTO update(CustomerViViDTO customerViViDTO);

    /**
     * Partially updates a customerViVi.
     *
     * @param customerViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerViViDTO> partialUpdate(CustomerViViDTO customerViViDTO);

    /**
     * Get all the customerViVis.
     *
     * @return the list of entities.
     */
    List<CustomerViViDTO> findAll();

    /**
     * Get the "id" customerViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerViViDTO> findOne(Long id);

    /**
     * Delete the "id" customerViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
