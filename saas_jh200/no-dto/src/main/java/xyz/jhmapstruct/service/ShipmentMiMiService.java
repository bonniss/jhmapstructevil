package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
@Service
@Transactional
public class ShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiService.class);

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    public ShipmentMiMiService(ShipmentMiMiRepository shipmentMiMiRepository) {
        this.shipmentMiMiRepository = shipmentMiMiRepository;
    }

    /**
     * Save a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiMi save(ShipmentMiMi shipmentMiMi) {
        LOG.debug("Request to save ShipmentMiMi : {}", shipmentMiMi);
        return shipmentMiMiRepository.save(shipmentMiMi);
    }

    /**
     * Update a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiMi update(ShipmentMiMi shipmentMiMi) {
        LOG.debug("Request to update ShipmentMiMi : {}", shipmentMiMi);
        return shipmentMiMiRepository.save(shipmentMiMi);
    }

    /**
     * Partially update a shipmentMiMi.
     *
     * @param shipmentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentMiMi> partialUpdate(ShipmentMiMi shipmentMiMi) {
        LOG.debug("Request to partially update ShipmentMiMi : {}", shipmentMiMi);

        return shipmentMiMiRepository
            .findById(shipmentMiMi.getId())
            .map(existingShipmentMiMi -> {
                if (shipmentMiMi.getTrackingNumber() != null) {
                    existingShipmentMiMi.setTrackingNumber(shipmentMiMi.getTrackingNumber());
                }
                if (shipmentMiMi.getShippedDate() != null) {
                    existingShipmentMiMi.setShippedDate(shipmentMiMi.getShippedDate());
                }
                if (shipmentMiMi.getDeliveryDate() != null) {
                    existingShipmentMiMi.setDeliveryDate(shipmentMiMi.getDeliveryDate());
                }

                return existingShipmentMiMi;
            })
            .map(shipmentMiMiRepository::save);
    }

    /**
     * Get one shipmentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentMiMi> findOne(Long id) {
        LOG.debug("Request to get ShipmentMiMi : {}", id);
        return shipmentMiMiRepository.findById(id);
    }

    /**
     * Delete the shipmentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMiMi : {}", id);
        shipmentMiMiRepository.deleteById(id);
    }
}
