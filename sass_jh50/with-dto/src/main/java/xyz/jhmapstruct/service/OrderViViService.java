package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.OrderViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
public interface OrderViViService {
    /**
     * Save a orderViVi.
     *
     * @param orderViViDTO the entity to save.
     * @return the persisted entity.
     */
    OrderViViDTO save(OrderViViDTO orderViViDTO);

    /**
     * Updates a orderViVi.
     *
     * @param orderViViDTO the entity to update.
     * @return the persisted entity.
     */
    OrderViViDTO update(OrderViViDTO orderViViDTO);

    /**
     * Partially updates a orderViVi.
     *
     * @param orderViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderViViDTO> partialUpdate(OrderViViDTO orderViViDTO);

    /**
     * Get all the orderViVis.
     *
     * @return the list of entities.
     */
    List<OrderViViDTO> findAll();

    /**
     * Get all the orderViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderViViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderViViDTO> findOne(Long id);

    /**
     * Delete the "id" orderViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
