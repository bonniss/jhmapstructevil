package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.ShipmentMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
public interface ShipmentMiMiService {
    /**
     * Save a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to save.
     * @return the persisted entity.
     */
    ShipmentMiMi save(ShipmentMiMi shipmentMiMi);

    /**
     * Updates a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to update.
     * @return the persisted entity.
     */
    ShipmentMiMi update(ShipmentMiMi shipmentMiMi);

    /**
     * Partially updates a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentMiMi> partialUpdate(ShipmentMiMi shipmentMiMi);

    /**
     * Get all the shipmentMiMis.
     *
     * @return the list of entities.
     */
    List<ShipmentMiMi> findAll();

    /**
     * Get the "id" shipmentMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentMiMi> findOne(Long id);

    /**
     * Delete the "id" shipmentMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
