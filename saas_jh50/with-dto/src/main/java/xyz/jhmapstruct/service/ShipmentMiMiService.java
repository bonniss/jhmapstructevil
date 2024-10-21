package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
public interface ShipmentMiMiService {
    /**
     * Save a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    ShipmentMiMiDTO save(ShipmentMiMiDTO shipmentMiMiDTO);

    /**
     * Updates a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    ShipmentMiMiDTO update(ShipmentMiMiDTO shipmentMiMiDTO);

    /**
     * Partially updates a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentMiMiDTO> partialUpdate(ShipmentMiMiDTO shipmentMiMiDTO);

    /**
     * Get all the shipmentMiMis.
     *
     * @return the list of entities.
     */
    List<ShipmentMiMiDTO> findAll();

    /**
     * Get the "id" shipmentMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" shipmentMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
