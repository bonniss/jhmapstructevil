package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ProductViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
public interface ProductViViService {
    /**
     * Save a productViVi.
     *
     * @param productViViDTO the entity to save.
     * @return the persisted entity.
     */
    ProductViViDTO save(ProductViViDTO productViViDTO);

    /**
     * Updates a productViVi.
     *
     * @param productViViDTO the entity to update.
     * @return the persisted entity.
     */
    ProductViViDTO update(ProductViViDTO productViViDTO);

    /**
     * Partially updates a productViVi.
     *
     * @param productViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductViViDTO> partialUpdate(ProductViViDTO productViViDTO);

    /**
     * Get all the productViVis.
     *
     * @return the list of entities.
     */
    List<ProductViViDTO> findAll();

    /**
     * Get all the productViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductViViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductViViDTO> findOne(Long id);

    /**
     * Delete the "id" productViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
