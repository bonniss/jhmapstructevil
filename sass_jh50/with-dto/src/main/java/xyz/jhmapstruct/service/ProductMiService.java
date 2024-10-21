package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ProductMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
public interface ProductMiService {
    /**
     * Save a productMi.
     *
     * @param productMiDTO the entity to save.
     * @return the persisted entity.
     */
    ProductMiDTO save(ProductMiDTO productMiDTO);

    /**
     * Updates a productMi.
     *
     * @param productMiDTO the entity to update.
     * @return the persisted entity.
     */
    ProductMiDTO update(ProductMiDTO productMiDTO);

    /**
     * Partially updates a productMi.
     *
     * @param productMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductMiDTO> partialUpdate(ProductMiDTO productMiDTO);

    /**
     * Get all the productMis.
     *
     * @return the list of entities.
     */
    List<ProductMiDTO> findAll();

    /**
     * Get all the productMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductMiDTO> findOne(Long id);

    /**
     * Delete the "id" productMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
