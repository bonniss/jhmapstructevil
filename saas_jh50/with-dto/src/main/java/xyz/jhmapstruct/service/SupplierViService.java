package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.SupplierViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
public interface SupplierViService {
    /**
     * Save a supplierVi.
     *
     * @param supplierViDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierViDTO save(SupplierViDTO supplierViDTO);

    /**
     * Updates a supplierVi.
     *
     * @param supplierViDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierViDTO update(SupplierViDTO supplierViDTO);

    /**
     * Partially updates a supplierVi.
     *
     * @param supplierViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierViDTO> partialUpdate(SupplierViDTO supplierViDTO);

    /**
     * Get all the supplierVis.
     *
     * @return the list of entities.
     */
    List<SupplierViDTO> findAll();

    /**
     * Get all the supplierVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierViDTO> findOne(Long id);

    /**
     * Delete the "id" supplierVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
