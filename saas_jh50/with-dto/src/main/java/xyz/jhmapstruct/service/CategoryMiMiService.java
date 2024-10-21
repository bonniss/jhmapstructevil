package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
public interface CategoryMiMiService {
    /**
     * Save a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryMiMiDTO save(CategoryMiMiDTO categoryMiMiDTO);

    /**
     * Updates a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryMiMiDTO update(CategoryMiMiDTO categoryMiMiDTO);

    /**
     * Partially updates a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryMiMiDTO> partialUpdate(CategoryMiMiDTO categoryMiMiDTO);

    /**
     * Get all the categoryMiMis.
     *
     * @return the list of entities.
     */
    List<CategoryMiMiDTO> findAll();

    /**
     * Get the "id" categoryMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" categoryMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
