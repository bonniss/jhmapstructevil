package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.SupplierMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
public interface SupplierMiMiService {
    /**
     * Save a supplierMiMi.
     *
     * @param supplierMiMi the entity to save.
     * @return the persisted entity.
     */
    SupplierMiMi save(SupplierMiMi supplierMiMi);

    /**
     * Updates a supplierMiMi.
     *
     * @param supplierMiMi the entity to update.
     * @return the persisted entity.
     */
    SupplierMiMi update(SupplierMiMi supplierMiMi);

    /**
     * Partially updates a supplierMiMi.
     *
     * @param supplierMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierMiMi> partialUpdate(SupplierMiMi supplierMiMi);

    /**
     * Get all the supplierMiMis.
     *
     * @return the list of entities.
     */
    List<SupplierMiMi> findAll();

    /**
     * Get all the supplierMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierMiMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierMiMi> findOne(Long id);

    /**
     * Delete the "id" supplierMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
