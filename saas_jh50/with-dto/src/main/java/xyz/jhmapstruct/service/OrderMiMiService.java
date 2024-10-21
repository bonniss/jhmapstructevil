package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
public interface OrderMiMiService {
    /**
     * Save a orderMiMi.
     *
     * @param orderMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    OrderMiMiDTO save(OrderMiMiDTO orderMiMiDTO);

    /**
     * Updates a orderMiMi.
     *
     * @param orderMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    OrderMiMiDTO update(OrderMiMiDTO orderMiMiDTO);

    /**
     * Partially updates a orderMiMi.
     *
     * @param orderMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderMiMiDTO> partialUpdate(OrderMiMiDTO orderMiMiDTO);

    /**
     * Get all the orderMiMis.
     *
     * @return the list of entities.
     */
    List<OrderMiMiDTO> findAll();

    /**
     * Get all the orderMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderMiMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" orderMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
