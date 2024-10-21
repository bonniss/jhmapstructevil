package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.OrderVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
public interface OrderViService {
    /**
     * Save a orderVi.
     *
     * @param orderVi the entity to save.
     * @return the persisted entity.
     */
    OrderVi save(OrderVi orderVi);

    /**
     * Updates a orderVi.
     *
     * @param orderVi the entity to update.
     * @return the persisted entity.
     */
    OrderVi update(OrderVi orderVi);

    /**
     * Partially updates a orderVi.
     *
     * @param orderVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderVi> partialUpdate(OrderVi orderVi);

    /**
     * Get all the orderVis.
     *
     * @return the list of entities.
     */
    List<OrderVi> findAll();

    /**
     * Get all the orderVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderVi> findOne(Long id);

    /**
     * Delete the "id" orderVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
