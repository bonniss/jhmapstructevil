package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.Supplier;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    Supplier save(Supplier supplier);

    /**
     * Updates a supplier.
     *
     * @param supplier the entity to update.
     * @return the persisted entity.
     */
    Supplier update(Supplier supplier);

    /**
     * Partially updates a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Supplier> partialUpdate(Supplier supplier);

    /**
     * Get all the suppliers.
     *
     * @return the list of entities.
     */
    List<Supplier> findAll();

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Supplier> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Supplier> findOne(Long id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
