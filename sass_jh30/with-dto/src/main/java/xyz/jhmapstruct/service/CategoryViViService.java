package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
public interface CategoryViViService {
    /**
     * Save a categoryViVi.
     *
     * @param categoryViViDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryViViDTO save(CategoryViViDTO categoryViViDTO);

    /**
     * Updates a categoryViVi.
     *
     * @param categoryViViDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryViViDTO update(CategoryViViDTO categoryViViDTO);

    /**
     * Partially updates a categoryViVi.
     *
     * @param categoryViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryViViDTO> partialUpdate(CategoryViViDTO categoryViViDTO);

    /**
     * Get all the categoryViVis.
     *
     * @return the list of entities.
     */
    List<CategoryViViDTO> findAll();

    /**
     * Get the "id" categoryViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryViViDTO> findOne(Long id);

    /**
     * Delete the "id" categoryViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
