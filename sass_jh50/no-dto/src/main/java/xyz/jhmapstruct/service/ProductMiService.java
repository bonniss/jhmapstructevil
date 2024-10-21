package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ProductMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
public interface ProductMiService {
    /**
     * Save a productMi.
     *
     * @param productMi the entity to save.
     * @return the persisted entity.
     */
    ProductMi save(ProductMi productMi);

    /**
     * Updates a productMi.
     *
     * @param productMi the entity to update.
     * @return the persisted entity.
     */
    ProductMi update(ProductMi productMi);

    /**
     * Partially updates a productMi.
     *
     * @param productMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductMi> partialUpdate(ProductMi productMi);

    /**
     * Get all the productMis.
     *
     * @return the list of entities.
     */
    List<ProductMi> findAll();

    /**
     * Get all the productMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductMi> findOne(Long id);

    /**
     * Delete the "id" productMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
