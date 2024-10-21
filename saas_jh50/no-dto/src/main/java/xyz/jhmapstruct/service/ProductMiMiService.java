package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ProductMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
public interface ProductMiMiService {
    /**
     * Save a productMiMi.
     *
     * @param productMiMi the entity to save.
     * @return the persisted entity.
     */
    ProductMiMi save(ProductMiMi productMiMi);

    /**
     * Updates a productMiMi.
     *
     * @param productMiMi the entity to update.
     * @return the persisted entity.
     */
    ProductMiMi update(ProductMiMi productMiMi);

    /**
     * Partially updates a productMiMi.
     *
     * @param productMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductMiMi> partialUpdate(ProductMiMi productMiMi);

    /**
     * Get all the productMiMis.
     *
     * @return the list of entities.
     */
    List<ProductMiMi> findAll();

    /**
     * Get all the productMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductMiMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductMiMi> findOne(Long id);

    /**
     * Delete the "id" productMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
