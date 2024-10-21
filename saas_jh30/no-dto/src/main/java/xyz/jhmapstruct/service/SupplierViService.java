package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.SupplierVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
public interface SupplierViService {
    /**
     * Save a supplierVi.
     *
     * @param supplierVi the entity to save.
     * @return the persisted entity.
     */
    SupplierVi save(SupplierVi supplierVi);

    /**
     * Updates a supplierVi.
     *
     * @param supplierVi the entity to update.
     * @return the persisted entity.
     */
    SupplierVi update(SupplierVi supplierVi);

    /**
     * Partially updates a supplierVi.
     *
     * @param supplierVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierVi> partialUpdate(SupplierVi supplierVi);

    /**
     * Get all the supplierVis.
     *
     * @return the list of entities.
     */
    List<SupplierVi> findAll();

    /**
     * Get all the supplierVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierVi> findOne(Long id);

    /**
     * Delete the "id" supplierVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
