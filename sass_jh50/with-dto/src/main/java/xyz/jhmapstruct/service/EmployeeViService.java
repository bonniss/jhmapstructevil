package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
public interface EmployeeViService {
    /**
     * Save a employeeVi.
     *
     * @param employeeViDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeViDTO save(EmployeeViDTO employeeViDTO);

    /**
     * Updates a employeeVi.
     *
     * @param employeeViDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeViDTO update(EmployeeViDTO employeeViDTO);

    /**
     * Partially updates a employeeVi.
     *
     * @param employeeViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeViDTO> partialUpdate(EmployeeViDTO employeeViDTO);

    /**
     * Get all the employeeVis.
     *
     * @return the list of entities.
     */
    List<EmployeeViDTO> findAll();

    /**
     * Get the "id" employeeVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeViDTO> findOne(Long id);

    /**
     * Delete the "id" employeeVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
