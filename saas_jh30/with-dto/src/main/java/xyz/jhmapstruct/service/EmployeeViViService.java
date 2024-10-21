package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
public interface EmployeeViViService {
    /**
     * Save a employeeViVi.
     *
     * @param employeeViViDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeViViDTO save(EmployeeViViDTO employeeViViDTO);

    /**
     * Updates a employeeViVi.
     *
     * @param employeeViViDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeViViDTO update(EmployeeViViDTO employeeViViDTO);

    /**
     * Partially updates a employeeViVi.
     *
     * @param employeeViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeViViDTO> partialUpdate(EmployeeViViDTO employeeViViDTO);

    /**
     * Get all the employeeViVis.
     *
     * @return the list of entities.
     */
    List<EmployeeViViDTO> findAll();

    /**
     * Get the "id" employeeViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeViViDTO> findOne(Long id);

    /**
     * Delete the "id" employeeViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
