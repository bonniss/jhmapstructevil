package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.SupplierViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
public interface SupplierViViService {
    /**
     * Save a supplierViVi.
     *
     * @param supplierViVi the entity to save.
     * @return the persisted entity.
     */
    SupplierViVi save(SupplierViVi supplierViVi);

    /**
     * Updates a supplierViVi.
     *
     * @param supplierViVi the entity to update.
     * @return the persisted entity.
     */
    SupplierViVi update(SupplierViVi supplierViVi);

    /**
     * Partially updates a supplierViVi.
     *
     * @param supplierViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierViVi> partialUpdate(SupplierViVi supplierViVi);

    /**
     * Get all the supplierViVis.
     *
     * @return the list of entities.
     */
    List<SupplierViVi> findAll();

    /**
     * Get all the supplierViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierViVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierViVi> findOne(Long id);

    /**
     * Delete the "id" supplierViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
