package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
@Service
@Transactional
public class ShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiService.class);

    private final ShipmentMiRepository shipmentMiRepository;

    public ShipmentMiService(ShipmentMiRepository shipmentMiRepository) {
        this.shipmentMiRepository = shipmentMiRepository;
    }

    /**
     * Save a shipmentMi.
     *
     * @param shipmentMi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMi save(ShipmentMi shipmentMi) {
        LOG.debug("Request to save ShipmentMi : {}", shipmentMi);
        return shipmentMiRepository.save(shipmentMi);
    }

    /**
     * Update a shipmentMi.
     *
     * @param shipmentMi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMi update(ShipmentMi shipmentMi) {
        LOG.debug("Request to update ShipmentMi : {}", shipmentMi);
        return shipmentMiRepository.save(shipmentMi);
    }

    /**
     * Partially update a shipmentMi.
     *
     * @param shipmentMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentMi> partialUpdate(ShipmentMi shipmentMi) {
        LOG.debug("Request to partially update ShipmentMi : {}", shipmentMi);

        return shipmentMiRepository
            .findById(shipmentMi.getId())
            .map(existingShipmentMi -> {
                if (shipmentMi.getTrackingNumber() != null) {
                    existingShipmentMi.setTrackingNumber(shipmentMi.getTrackingNumber());
                }
                if (shipmentMi.getShippedDate() != null) {
                    existingShipmentMi.setShippedDate(shipmentMi.getShippedDate());
                }
                if (shipmentMi.getDeliveryDate() != null) {
                    existingShipmentMi.setDeliveryDate(shipmentMi.getDeliveryDate());
                }

                return existingShipmentMi;
            })
            .map(shipmentMiRepository::save);
    }

    /**
     * Get one shipmentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentMi> findOne(Long id) {
        LOG.debug("Request to get ShipmentMi : {}", id);
        return shipmentMiRepository.findById(id);
    }

    /**
     * Delete the shipmentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMi : {}", id);
        shipmentMiRepository.deleteById(id);
    }
}
