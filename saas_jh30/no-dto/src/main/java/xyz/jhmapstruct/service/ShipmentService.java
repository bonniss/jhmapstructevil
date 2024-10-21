package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.Shipment;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.Shipment}.
 */
public interface ShipmentService {
    /**
     * Save a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    Shipment save(Shipment shipment);

    /**
     * Updates a shipment.
     *
     * @param shipment the entity to update.
     * @return the persisted entity.
     */
    Shipment update(Shipment shipment);

    /**
     * Partially updates a shipment.
     *
     * @param shipment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Shipment> partialUpdate(Shipment shipment);

    /**
     * Get all the shipments.
     *
     * @return the list of entities.
     */
    List<Shipment> findAll();

    /**
     * Get the "id" shipment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Shipment> findOne(Long id);

    /**
     * Delete the "id" shipment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
