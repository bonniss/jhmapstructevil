package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
public interface ShipmentMiService {
    /**
     * Save a shipmentMi.
     *
     * @param shipmentMiDTO the entity to save.
     * @return the persisted entity.
     */
    ShipmentMiDTO save(ShipmentMiDTO shipmentMiDTO);

    /**
     * Updates a shipmentMi.
     *
     * @param shipmentMiDTO the entity to update.
     * @return the persisted entity.
     */
    ShipmentMiDTO update(ShipmentMiDTO shipmentMiDTO);

    /**
     * Partially updates a shipmentMi.
     *
     * @param shipmentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentMiDTO> partialUpdate(ShipmentMiDTO shipmentMiDTO);

    /**
     * Get all the shipmentMis.
     *
     * @return the list of entities.
     */
    List<ShipmentMiDTO> findAll();

    /**
     * Get the "id" shipmentMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentMiDTO> findOne(Long id);

    /**
     * Delete the "id" shipmentMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
