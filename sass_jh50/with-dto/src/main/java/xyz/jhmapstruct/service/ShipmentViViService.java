package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
public interface ShipmentViViService {
    /**
     * Save a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to save.
     * @return the persisted entity.
     */
    ShipmentViViDTO save(ShipmentViViDTO shipmentViViDTO);

    /**
     * Updates a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to update.
     * @return the persisted entity.
     */
    ShipmentViViDTO update(ShipmentViViDTO shipmentViViDTO);

    /**
     * Partially updates a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentViViDTO> partialUpdate(ShipmentViViDTO shipmentViViDTO);

    /**
     * Get all the shipmentViVis.
     *
     * @return the list of entities.
     */
    List<ShipmentViViDTO> findAll();

    /**
     * Get the "id" shipmentViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentViViDTO> findOne(Long id);

    /**
     * Delete the "id" shipmentViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
