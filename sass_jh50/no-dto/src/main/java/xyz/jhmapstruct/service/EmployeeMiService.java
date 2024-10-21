package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.EmployeeMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
public interface EmployeeMiService {
    /**
     * Save a employeeMi.
     *
     * @param employeeMi the entity to save.
     * @return the persisted entity.
     */
    EmployeeMi save(EmployeeMi employeeMi);

    /**
     * Updates a employeeMi.
     *
     * @param employeeMi the entity to update.
     * @return the persisted entity.
     */
    EmployeeMi update(EmployeeMi employeeMi);

    /**
     * Partially updates a employeeMi.
     *
     * @param employeeMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeMi> partialUpdate(EmployeeMi employeeMi);

    /**
     * Get all the employeeMis.
     *
     * @return the list of entities.
     */
    List<EmployeeMi> findAll();

    /**
     * Get the "id" employeeMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeMi> findOne(Long id);

    /**
     * Delete the "id" employeeMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
