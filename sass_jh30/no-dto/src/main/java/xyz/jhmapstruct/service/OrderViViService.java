package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.OrderViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
public interface OrderViViService {
    /**
     * Save a orderViVi.
     *
     * @param orderViVi the entity to save.
     * @return the persisted entity.
     */
    OrderViVi save(OrderViVi orderViVi);

    /**
     * Updates a orderViVi.
     *
     * @param orderViVi the entity to update.
     * @return the persisted entity.
     */
    OrderViVi update(OrderViVi orderViVi);

    /**
     * Partially updates a orderViVi.
     *
     * @param orderViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderViVi> partialUpdate(OrderViVi orderViVi);

    /**
     * Get all the orderViVis.
     *
     * @return the list of entities.
     */
    List<OrderViVi> findAll();

    /**
     * Get all the orderViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderViVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderViVi> findOne(Long id);

    /**
     * Delete the "id" orderViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
