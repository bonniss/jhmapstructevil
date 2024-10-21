package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.EmployeeDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.Employee}.
 */
public interface EmployeeService {
    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeDTO save(EmployeeDTO employeeDTO);

    /**
     * Updates a employee.
     *
     * @param employeeDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeDTO update(EmployeeDTO employeeDTO);

    /**
     * Partially updates a employee.
     *
     * @param employeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO);

    /**
     * Get all the employees.
     *
     * @return the list of entities.
     */
    List<EmployeeDTO> findAll();

    /**
     * Get the "id" employee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeDTO> findOne(Long id);

    /**
     * Delete the "id" employee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
