package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
public interface SupplierMiMiService {
    /**
     * Save a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierMiMiDTO save(SupplierMiMiDTO supplierMiMiDTO);

    /**
     * Updates a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierMiMiDTO update(SupplierMiMiDTO supplierMiMiDTO);

    /**
     * Partially updates a supplierMiMi.
     *
     * @param supplierMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierMiMiDTO> partialUpdate(SupplierMiMiDTO supplierMiMiDTO);

    /**
     * Get all the supplierMiMis.
     *
     * @return the list of entities.
     */
    List<SupplierMiMiDTO> findAll();

    /**
     * Get all the supplierMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierMiMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" supplierMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
