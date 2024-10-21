package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.SupplierMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
public interface SupplierMiService {
    /**
     * Save a supplierMi.
     *
     * @param supplierMi the entity to save.
     * @return the persisted entity.
     */
    SupplierMi save(SupplierMi supplierMi);

    /**
     * Updates a supplierMi.
     *
     * @param supplierMi the entity to update.
     * @return the persisted entity.
     */
    SupplierMi update(SupplierMi supplierMi);

    /**
     * Partially updates a supplierMi.
     *
     * @param supplierMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierMi> partialUpdate(SupplierMi supplierMi);

    /**
     * Get all the supplierMis.
     *
     * @return the list of entities.
     */
    List<SupplierMi> findAll();

    /**
     * Get all the supplierMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierMi> findOne(Long id);

    /**
     * Delete the "id" supplierMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
