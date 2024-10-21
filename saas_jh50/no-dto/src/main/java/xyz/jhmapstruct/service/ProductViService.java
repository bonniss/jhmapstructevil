package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ProductVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
public interface ProductViService {
    /**
     * Save a productVi.
     *
     * @param productVi the entity to save.
     * @return the persisted entity.
     */
    ProductVi save(ProductVi productVi);

    /**
     * Updates a productVi.
     *
     * @param productVi the entity to update.
     * @return the persisted entity.
     */
    ProductVi update(ProductVi productVi);

    /**
     * Partially updates a productVi.
     *
     * @param productVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductVi> partialUpdate(ProductVi productVi);

    /**
     * Get all the productVis.
     *
     * @return the list of entities.
     */
    List<ProductVi> findAll();

    /**
     * Get all the productVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductVi> findOne(Long id);

    /**
     * Delete the "id" productVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
