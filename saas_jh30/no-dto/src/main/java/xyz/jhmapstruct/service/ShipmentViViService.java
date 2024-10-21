package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.ShipmentViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
public interface ShipmentViViService {
    /**
     * Save a shipmentViVi.
     *
     * @param shipmentViVi the entity to save.
     * @return the persisted entity.
     */
    ShipmentViVi save(ShipmentViVi shipmentViVi);

    /**
     * Updates a shipmentViVi.
     *
     * @param shipmentViVi the entity to update.
     * @return the persisted entity.
     */
    ShipmentViVi update(ShipmentViVi shipmentViVi);

    /**
     * Partially updates a shipmentViVi.
     *
     * @param shipmentViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentViVi> partialUpdate(ShipmentViVi shipmentViVi);

    /**
     * Get all the shipmentViVis.
     *
     * @return the list of entities.
     */
    List<ShipmentViVi> findAll();

    /**
     * Get the "id" shipmentViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentViVi> findOne(Long id);

    /**
     * Delete the "id" shipmentViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
