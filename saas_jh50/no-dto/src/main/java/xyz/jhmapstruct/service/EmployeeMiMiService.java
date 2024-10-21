package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.EmployeeMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
public interface EmployeeMiMiService {
    /**
     * Save a employeeMiMi.
     *
     * @param employeeMiMi the entity to save.
     * @return the persisted entity.
     */
    EmployeeMiMi save(EmployeeMiMi employeeMiMi);

    /**
     * Updates a employeeMiMi.
     *
     * @param employeeMiMi the entity to update.
     * @return the persisted entity.
     */
    EmployeeMiMi update(EmployeeMiMi employeeMiMi);

    /**
     * Partially updates a employeeMiMi.
     *
     * @param employeeMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeMiMi> partialUpdate(EmployeeMiMi employeeMiMi);

    /**
     * Get all the employeeMiMis.
     *
     * @return the list of entities.
     */
    List<EmployeeMiMi> findAll();

    /**
     * Get the "id" employeeMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeMiMi> findOne(Long id);

    /**
     * Delete the "id" employeeMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
