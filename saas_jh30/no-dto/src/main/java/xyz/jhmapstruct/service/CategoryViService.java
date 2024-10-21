package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CategoryVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
public interface CategoryViService {
    /**
     * Save a categoryVi.
     *
     * @param categoryVi the entity to save.
     * @return the persisted entity.
     */
    CategoryVi save(CategoryVi categoryVi);

    /**
     * Updates a categoryVi.
     *
     * @param categoryVi the entity to update.
     * @return the persisted entity.
     */
    CategoryVi update(CategoryVi categoryVi);

    /**
     * Partially updates a categoryVi.
     *
     * @param categoryVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryVi> partialUpdate(CategoryVi categoryVi);

    /**
     * Get all the categoryVis.
     *
     * @return the list of entities.
     */
    List<CategoryVi> findAll();

    /**
     * Get the "id" categoryVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryVi> findOne(Long id);

    /**
     * Delete the "id" categoryVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
