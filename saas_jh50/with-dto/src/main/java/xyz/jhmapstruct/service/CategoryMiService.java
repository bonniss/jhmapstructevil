package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
public interface CategoryMiService {
    /**
     * Save a categoryMi.
     *
     * @param categoryMiDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryMiDTO save(CategoryMiDTO categoryMiDTO);

    /**
     * Updates a categoryMi.
     *
     * @param categoryMiDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryMiDTO update(CategoryMiDTO categoryMiDTO);

    /**
     * Partially updates a categoryMi.
     *
     * @param categoryMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryMiDTO> partialUpdate(CategoryMiDTO categoryMiDTO);

    /**
     * Get all the categoryMis.
     *
     * @return the list of entities.
     */
    List<CategoryMiDTO> findAll();

    /**
     * Get the "id" categoryMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryMiDTO> findOne(Long id);

    /**
     * Delete the "id" categoryMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
