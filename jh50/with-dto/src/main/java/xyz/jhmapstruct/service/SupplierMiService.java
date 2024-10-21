package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
public interface SupplierMiService {
    /**
     * Save a supplierMi.
     *
     * @param supplierMiDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierMiDTO save(SupplierMiDTO supplierMiDTO);

    /**
     * Updates a supplierMi.
     *
     * @param supplierMiDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierMiDTO update(SupplierMiDTO supplierMiDTO);

    /**
     * Partially updates a supplierMi.
     *
     * @param supplierMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierMiDTO> partialUpdate(SupplierMiDTO supplierMiDTO);

    /**
     * Get all the supplierMis.
     *
     * @return the list of entities.
     */
    List<SupplierMiDTO> findAll();

    /**
     * Get all the supplierMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierMiDTO> findOne(Long id);

    /**
     * Delete the "id" supplierMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
