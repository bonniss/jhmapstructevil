package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.repository.ShipmentRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Save a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment save(Shipment shipment) {
        LOG.debug("Request to save Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    /**
     * Update a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment update(Shipment shipment) {
        LOG.debug("Request to update Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    /**
     * Partially update a shipment.
     *
     * @param shipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Shipment> partialUpdate(Shipment shipment) {
        LOG.debug("Request to partially update Shipment : {}", shipment);

        return shipmentRepository
            .findById(shipment.getId())
            .map(existingShipment -> {
                if (shipment.getTrackingNumber() != null) {
                    existingShipment.setTrackingNumber(shipment.getTrackingNumber());
                }
                if (shipment.getShippedDate() != null) {
                    existingShipment.setShippedDate(shipment.getShippedDate());
                }
                if (shipment.getDeliveryDate() != null) {
                    existingShipment.setDeliveryDate(shipment.getDeliveryDate());
                }

                return existingShipment;
            })
            .map(shipmentRepository::save);
    }

    /**
     * Get one shipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id);
    }

    /**
     * Delete the shipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
    }
}
