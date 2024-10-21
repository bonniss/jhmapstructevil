package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.EmployeeVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
public interface EmployeeViService {
    /**
     * Save a employeeVi.
     *
     * @param employeeVi the entity to save.
     * @return the persisted entity.
     */
    EmployeeVi save(EmployeeVi employeeVi);

    /**
     * Updates a employeeVi.
     *
     * @param employeeVi the entity to update.
     * @return the persisted entity.
     */
    EmployeeVi update(EmployeeVi employeeVi);

    /**
     * Partially updates a employeeVi.
     *
     * @param employeeVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeVi> partialUpdate(EmployeeVi employeeVi);

    /**
     * Get all the employeeVis.
     *
     * @return the list of entities.
     */
    List<EmployeeVi> findAll();

    /**
     * Get the "id" employeeVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeVi> findOne(Long id);

    /**
     * Delete the "id" employeeVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
