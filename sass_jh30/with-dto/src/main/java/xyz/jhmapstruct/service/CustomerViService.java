package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CustomerViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
public interface CustomerViService {
    /**
     * Save a customerVi.
     *
     * @param customerViDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerViDTO save(CustomerViDTO customerViDTO);

    /**
     * Updates a customerVi.
     *
     * @param customerViDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerViDTO update(CustomerViDTO customerViDTO);

    /**
     * Partially updates a customerVi.
     *
     * @param customerViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerViDTO> partialUpdate(CustomerViDTO customerViDTO);

    /**
     * Get all the customerVis.
     *
     * @return the list of entities.
     */
    List<CustomerViDTO> findAll();

    /**
     * Get the "id" customerVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerViDTO> findOne(Long id);

    /**
     * Delete the "id" customerVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
