package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.ShipmentVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
public interface ShipmentViService {
    /**
     * Save a shipmentVi.
     *
     * @param shipmentVi the entity to save.
     * @return the persisted entity.
     */
    ShipmentVi save(ShipmentVi shipmentVi);

    /**
     * Updates a shipmentVi.
     *
     * @param shipmentVi the entity to update.
     * @return the persisted entity.
     */
    ShipmentVi update(ShipmentVi shipmentVi);

    /**
     * Partially updates a shipmentVi.
     *
     * @param shipmentVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentVi> partialUpdate(ShipmentVi shipmentVi);

    /**
     * Get all the shipmentVis.
     *
     * @return the list of entities.
     */
    List<ShipmentVi> findAll();

    /**
     * Get the "id" shipmentVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentVi> findOne(Long id);

    /**
     * Delete the "id" shipmentVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
