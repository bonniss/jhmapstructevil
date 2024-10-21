package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.OrderMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
public interface OrderMiService {
    /**
     * Save a orderMi.
     *
     * @param orderMi the entity to save.
     * @return the persisted entity.
     */
    OrderMi save(OrderMi orderMi);

    /**
     * Updates a orderMi.
     *
     * @param orderMi the entity to update.
     * @return the persisted entity.
     */
    OrderMi update(OrderMi orderMi);

    /**
     * Partially updates a orderMi.
     *
     * @param orderMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderMi> partialUpdate(OrderMi orderMi);

    /**
     * Get all the orderMis.
     *
     * @return the list of entities.
     */
    List<OrderMi> findAll();

    /**
     * Get all the orderMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderMi> findOne(Long id);

    /**
     * Delete the "id" orderMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
