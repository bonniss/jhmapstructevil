package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.OrderMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
public interface OrderMiMiService {
    /**
     * Save a orderMiMi.
     *
     * @param orderMiMi the entity to save.
     * @return the persisted entity.
     */
    OrderMiMi save(OrderMiMi orderMiMi);

    /**
     * Updates a orderMiMi.
     *
     * @param orderMiMi the entity to update.
     * @return the persisted entity.
     */
    OrderMiMi update(OrderMiMi orderMiMi);

    /**
     * Partially updates a orderMiMi.
     *
     * @param orderMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderMiMi> partialUpdate(OrderMiMi orderMiMi);

    /**
     * Get all the orderMiMis.
     *
     * @return the list of entities.
     */
    List<OrderMiMi> findAll();

    /**
     * Get all the orderMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderMiMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderMiMi> findOne(Long id);

    /**
     * Delete the "id" orderMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
