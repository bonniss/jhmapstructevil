package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.CategoryViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
public interface CategoryViService {
    /**
     * Save a categoryVi.
     *
     * @param categoryViDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryViDTO save(CategoryViDTO categoryViDTO);

    /**
     * Updates a categoryVi.
     *
     * @param categoryViDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryViDTO update(CategoryViDTO categoryViDTO);

    /**
     * Partially updates a categoryVi.
     *
     * @param categoryViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryViDTO> partialUpdate(CategoryViDTO categoryViDTO);

    /**
     * Get all the categoryVis.
     *
     * @return the list of entities.
     */
    List<CategoryViDTO> findAll();

    /**
     * Get the "id" categoryVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryViDTO> findOne(Long id);

    /**
     * Delete the "id" categoryVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
