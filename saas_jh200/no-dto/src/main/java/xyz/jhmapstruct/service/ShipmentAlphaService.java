package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.repository.ShipmentAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentAlpha}.
 */
@Service
@Transactional
public class ShipmentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAlphaService.class);

    private final ShipmentAlphaRepository shipmentAlphaRepository;

    public ShipmentAlphaService(ShipmentAlphaRepository shipmentAlphaRepository) {
        this.shipmentAlphaRepository = shipmentAlphaRepository;
    }

    /**
     * Save a shipmentAlpha.
     *
     * @param shipmentAlpha the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAlpha save(ShipmentAlpha shipmentAlpha) {
        LOG.debug("Request to save ShipmentAlpha : {}", shipmentAlpha);
        return shipmentAlphaRepository.save(shipmentAlpha);
    }

    /**
     * Update a shipmentAlpha.
     *
     * @param shipmentAlpha the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAlpha update(ShipmentAlpha shipmentAlpha) {
        LOG.debug("Request to update ShipmentAlpha : {}", shipmentAlpha);
        return shipmentAlphaRepository.save(shipmentAlpha);
    }

    /**
     * Partially update a shipmentAlpha.
     *
     * @param shipmentAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentAlpha> partialUpdate(ShipmentAlpha shipmentAlpha) {
        LOG.debug("Request to partially update ShipmentAlpha : {}", shipmentAlpha);

        return shipmentAlphaRepository
            .findById(shipmentAlpha.getId())
            .map(existingShipmentAlpha -> {
                if (shipmentAlpha.getTrackingNumber() != null) {
                    existingShipmentAlpha.setTrackingNumber(shipmentAlpha.getTrackingNumber());
                }
                if (shipmentAlpha.getShippedDate() != null) {
                    existingShipmentAlpha.setShippedDate(shipmentAlpha.getShippedDate());
                }
                if (shipmentAlpha.getDeliveryDate() != null) {
                    existingShipmentAlpha.setDeliveryDate(shipmentAlpha.getDeliveryDate());
                }

                return existingShipmentAlpha;
            })
            .map(shipmentAlphaRepository::save);
    }

    /**
     * Get one shipmentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentAlpha> findOne(Long id) {
        LOG.debug("Request to get ShipmentAlpha : {}", id);
        return shipmentAlphaRepository.findById(id);
    }

    /**
     * Delete the shipmentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentAlpha : {}", id);
        shipmentAlphaRepository.deleteById(id);
    }
}
