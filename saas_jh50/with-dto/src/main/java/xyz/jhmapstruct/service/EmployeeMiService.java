package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
public interface EmployeeMiService {
    /**
     * Save a employeeMi.
     *
     * @param employeeMiDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeMiDTO save(EmployeeMiDTO employeeMiDTO);

    /**
     * Updates a employeeMi.
     *
     * @param employeeMiDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeMiDTO update(EmployeeMiDTO employeeMiDTO);

    /**
     * Partially updates a employeeMi.
     *
     * @param employeeMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeMiDTO> partialUpdate(EmployeeMiDTO employeeMiDTO);

    /**
     * Get all the employeeMis.
     *
     * @return the list of entities.
     */
    List<EmployeeMiDTO> findAll();

    /**
     * Get the "id" employeeMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeMiDTO> findOne(Long id);

    /**
     * Delete the "id" employeeMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
