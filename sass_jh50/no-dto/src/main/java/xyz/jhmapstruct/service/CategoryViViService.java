package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CategoryViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
public interface CategoryViViService {
    /**
     * Save a categoryViVi.
     *
     * @param categoryViVi the entity to save.
     * @return the persisted entity.
     */
    CategoryViVi save(CategoryViVi categoryViVi);

    /**
     * Updates a categoryViVi.
     *
     * @param categoryViVi the entity to update.
     * @return the persisted entity.
     */
    CategoryViVi update(CategoryViVi categoryViVi);

    /**
     * Partially updates a categoryViVi.
     *
     * @param categoryViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryViVi> partialUpdate(CategoryViVi categoryViVi);

    /**
     * Get all the categoryViVis.
     *
     * @return the list of entities.
     */
    List<CategoryViVi> findAll();

    /**
     * Get the "id" categoryViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryViVi> findOne(Long id);

    /**
     * Delete the "id" categoryViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
