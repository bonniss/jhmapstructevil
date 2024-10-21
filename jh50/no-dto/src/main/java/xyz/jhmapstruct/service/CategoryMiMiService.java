package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.CategoryMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
public interface CategoryMiMiService {
    /**
     * Save a categoryMiMi.
     *
     * @param categoryMiMi the entity to save.
     * @return the persisted entity.
     */
    CategoryMiMi save(CategoryMiMi categoryMiMi);

    /**
     * Updates a categoryMiMi.
     *
     * @param categoryMiMi the entity to update.
     * @return the persisted entity.
     */
    CategoryMiMi update(CategoryMiMi categoryMiMi);

    /**
     * Partially updates a categoryMiMi.
     *
     * @param categoryMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryMiMi> partialUpdate(CategoryMiMi categoryMiMi);

    /**
     * Get all the categoryMiMis.
     *
     * @return the list of entities.
     */
    List<CategoryMiMi> findAll();

    /**
     * Get the "id" categoryMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryMiMi> findOne(Long id);

    /**
     * Delete the "id" categoryMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
