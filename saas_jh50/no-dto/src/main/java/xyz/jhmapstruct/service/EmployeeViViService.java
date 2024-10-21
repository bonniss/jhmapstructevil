package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.EmployeeViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
public interface EmployeeViViService {
    /**
     * Save a employeeViVi.
     *
     * @param employeeViVi the entity to save.
     * @return the persisted entity.
     */
    EmployeeViVi save(EmployeeViVi employeeViVi);

    /**
     * Updates a employeeViVi.
     *
     * @param employeeViVi the entity to update.
     * @return the persisted entity.
     */
    EmployeeViVi update(EmployeeViVi employeeViVi);

    /**
     * Partially updates a employeeViVi.
     *
     * @param employeeViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeViVi> partialUpdate(EmployeeViVi employeeViVi);

    /**
     * Get all the employeeViVis.
     *
     * @return the list of entities.
     */
    List<EmployeeViVi> findAll();

    /**
     * Get the "id" employeeViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeViVi> findOne(Long id);

    /**
     * Delete the "id" employeeViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
