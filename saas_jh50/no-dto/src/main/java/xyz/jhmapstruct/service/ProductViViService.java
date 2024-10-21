package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ProductViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
public interface ProductViViService {
    /**
     * Save a productViVi.
     *
     * @param productViVi the entity to save.
     * @return the persisted entity.
     */
    ProductViVi save(ProductViVi productViVi);

    /**
     * Updates a productViVi.
     *
     * @param productViVi the entity to update.
     * @return the persisted entity.
     */
    ProductViVi update(ProductViVi productViVi);

    /**
     * Partially updates a productViVi.
     *
     * @param productViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductViVi> partialUpdate(ProductViVi productViVi);

    /**
     * Get all the productViVis.
     *
     * @return the list of entities.
     */
    List<ProductViVi> findAll();

    /**
     * Get all the productViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductViVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductViVi> findOne(Long id);

    /**
     * Delete the "id" productViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
