package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.OrderMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
public interface OrderMiService {
    /**
     * Save a orderMi.
     *
     * @param orderMiDTO the entity to save.
     * @return the persisted entity.
     */
    OrderMiDTO save(OrderMiDTO orderMiDTO);

    /**
     * Updates a orderMi.
     *
     * @param orderMiDTO the entity to update.
     * @return the persisted entity.
     */
    OrderMiDTO update(OrderMiDTO orderMiDTO);

    /**
     * Partially updates a orderMi.
     *
     * @param orderMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderMiDTO> partialUpdate(OrderMiDTO orderMiDTO);

    /**
     * Get all the orderMis.
     *
     * @return the list of entities.
     */
    List<OrderMiDTO> findAll();

    /**
     * Get all the orderMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderMiDTO> findOne(Long id);

    /**
     * Delete the "id" orderMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
