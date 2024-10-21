package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.ShipmentMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
public interface ShipmentMiService {
    /**
     * Save a shipmentMi.
     *
     * @param shipmentMi the entity to save.
     * @return the persisted entity.
     */
    ShipmentMi save(ShipmentMi shipmentMi);

    /**
     * Updates a shipmentMi.
     *
     * @param shipmentMi the entity to update.
     * @return the persisted entity.
     */
    ShipmentMi update(ShipmentMi shipmentMi);

    /**
     * Partially updates a shipmentMi.
     *
     * @param shipmentMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentMi> partialUpdate(ShipmentMi shipmentMi);

    /**
     * Get all the shipmentMis.
     *
     * @return the list of entities.
     */
    List<ShipmentMi> findAll();

    /**
     * Get the "id" shipmentMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentMi> findOne(Long id);

    /**
     * Delete the "id" shipmentMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
