package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
public interface ShipmentViService {
    /**
     * Save a shipmentVi.
     *
     * @param shipmentViDTO the entity to save.
     * @return the persisted entity.
     */
    ShipmentViDTO save(ShipmentViDTO shipmentViDTO);

    /**
     * Updates a shipmentVi.
     *
     * @param shipmentViDTO the entity to update.
     * @return the persisted entity.
     */
    ShipmentViDTO update(ShipmentViDTO shipmentViDTO);

    /**
     * Partially updates a shipmentVi.
     *
     * @param shipmentViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentViDTO> partialUpdate(ShipmentViDTO shipmentViDTO);

    /**
     * Get all the shipmentVis.
     *
     * @return the list of entities.
     */
    List<ShipmentViDTO> findAll();

    /**
     * Get the "id" shipmentVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentViDTO> findOne(Long id);

    /**
     * Delete the "id" shipmentVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
