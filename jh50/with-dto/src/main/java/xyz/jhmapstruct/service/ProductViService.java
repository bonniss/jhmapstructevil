package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ProductViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
public interface ProductViService {
    /**
     * Save a productVi.
     *
     * @param productViDTO the entity to save.
     * @return the persisted entity.
     */
    ProductViDTO save(ProductViDTO productViDTO);

    /**
     * Updates a productVi.
     *
     * @param productViDTO the entity to update.
     * @return the persisted entity.
     */
    ProductViDTO update(ProductViDTO productViDTO);

    /**
     * Partially updates a productVi.
     *
     * @param productViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductViDTO> partialUpdate(ProductViDTO productViDTO);

    /**
     * Get all the productVis.
     *
     * @return the list of entities.
     */
    List<ProductViDTO> findAll();

    /**
     * Get all the productVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductViDTO> findOne(Long id);

    /**
     * Delete the "id" productVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
