package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.EmployeeMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
public interface EmployeeMiMiService {
    /**
     * Save a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeMiMiDTO save(EmployeeMiMiDTO employeeMiMiDTO);

    /**
     * Updates a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeMiMiDTO update(EmployeeMiMiDTO employeeMiMiDTO);

    /**
     * Partially updates a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeMiMiDTO> partialUpdate(EmployeeMiMiDTO employeeMiMiDTO);

    /**
     * Get all the employeeMiMis.
     *
     * @return the list of entities.
     */
    List<EmployeeMiMiDTO> findAll();

    /**
     * Get the "id" employeeMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" employeeMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
