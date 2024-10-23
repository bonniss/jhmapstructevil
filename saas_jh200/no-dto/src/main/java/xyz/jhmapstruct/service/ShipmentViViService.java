package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
@Service
@Transactional
public class ShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViViService.class);

    private final ShipmentViViRepository shipmentViViRepository;

    public ShipmentViViService(ShipmentViViRepository shipmentViViRepository) {
        this.shipmentViViRepository = shipmentViViRepository;
    }

    /**
     * Save a shipmentViVi.
     *
     * @param shipmentViVi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViVi save(ShipmentViVi shipmentViVi) {
        LOG.debug("Request to save ShipmentViVi : {}", shipmentViVi);
        return shipmentViViRepository.save(shipmentViVi);
    }

    /**
     * Update a shipmentViVi.
     *
     * @param shipmentViVi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViVi update(ShipmentViVi shipmentViVi) {
        LOG.debug("Request to update ShipmentViVi : {}", shipmentViVi);
        return shipmentViViRepository.save(shipmentViVi);
    }

    /**
     * Partially update a shipmentViVi.
     *
     * @param shipmentViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentViVi> partialUpdate(ShipmentViVi shipmentViVi) {
        LOG.debug("Request to partially update ShipmentViVi : {}", shipmentViVi);

        return shipmentViViRepository
            .findById(shipmentViVi.getId())
            .map(existingShipmentViVi -> {
                if (shipmentViVi.getTrackingNumber() != null) {
                    existingShipmentViVi.setTrackingNumber(shipmentViVi.getTrackingNumber());
                }
                if (shipmentViVi.getShippedDate() != null) {
                    existingShipmentViVi.setShippedDate(shipmentViVi.getShippedDate());
                }
                if (shipmentViVi.getDeliveryDate() != null) {
                    existingShipmentViVi.setDeliveryDate(shipmentViVi.getDeliveryDate());
                }

                return existingShipmentViVi;
            })
            .map(shipmentViViRepository::save);
    }

    /**
     * Get one shipmentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentViVi> findOne(Long id) {
        LOG.debug("Request to get ShipmentViVi : {}", id);
        return shipmentViViRepository.findById(id);
    }

    /**
     * Delete the shipmentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentViVi : {}", id);
        shipmentViViRepository.deleteById(id);
    }
}
