package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
public interface SupplierViViService {
    /**
     * Save a supplierViVi.
     *
     * @param supplierViViDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierViViDTO save(SupplierViViDTO supplierViViDTO);

    /**
     * Updates a supplierViVi.
     *
     * @param supplierViViDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierViViDTO update(SupplierViViDTO supplierViViDTO);

    /**
     * Partially updates a supplierViVi.
     *
     * @param supplierViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierViViDTO> partialUpdate(SupplierViViDTO supplierViViDTO);

    /**
     * Get all the supplierViVis.
     *
     * @return the list of entities.
     */
    List<SupplierViViDTO> findAll();

    /**
     * Get all the supplierViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierViViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierViViDTO> findOne(Long id);

    /**
     * Delete the "id" supplierViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
