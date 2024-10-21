package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CategoryMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
public interface CategoryMiService {
    /**
     * Save a categoryMi.
     *
     * @param categoryMi the entity to save.
     * @return the persisted entity.
     */
    CategoryMi save(CategoryMi categoryMi);

    /**
     * Updates a categoryMi.
     *
     * @param categoryMi the entity to update.
     * @return the persisted entity.
     */
    CategoryMi update(CategoryMi categoryMi);

    /**
     * Partially updates a categoryMi.
     *
     * @param categoryMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryMi> partialUpdate(CategoryMi categoryMi);

    /**
     * Get all the categoryMis.
     *
     * @return the list of entities.
     */
    List<CategoryMi> findAll();

    /**
     * Get the "id" categoryMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryMi> findOne(Long id);

    /**
     * Delete the "id" categoryMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
