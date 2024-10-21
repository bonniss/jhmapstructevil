package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
public interface ProductMiMiService {
    /**
     * Save a productMiMi.
     *
     * @param productMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    ProductMiMiDTO save(ProductMiMiDTO productMiMiDTO);

    /**
     * Updates a productMiMi.
     *
     * @param productMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    ProductMiMiDTO update(ProductMiMiDTO productMiMiDTO);

    /**
     * Partially updates a productMiMi.
     *
     * @param productMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductMiMiDTO> partialUpdate(ProductMiMiDTO productMiMiDTO);

    /**
     * Get all the productMiMis.
     *
     * @return the list of entities.
     */
    List<ProductMiMiDTO> findAll();

    /**
     * Get all the productMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductMiMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" productMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
