package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.OrderViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
public interface OrderViService {
    /**
     * Save a orderVi.
     *
     * @param orderViDTO the entity to save.
     * @return the persisted entity.
     */
    OrderViDTO save(OrderViDTO orderViDTO);

    /**
     * Updates a orderVi.
     *
     * @param orderViDTO the entity to update.
     * @return the persisted entity.
     */
    OrderViDTO update(OrderViDTO orderViDTO);

    /**
     * Partially updates a orderVi.
     *
     * @param orderViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderViDTO> partialUpdate(OrderViDTO orderViDTO);

    /**
     * Get all the orderVis.
     *
     * @return the list of entities.
     */
    List<OrderViDTO> findAll();

    /**
     * Get all the orderVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderViDTO> findOne(Long id);

    /**
     * Delete the "id" orderVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
