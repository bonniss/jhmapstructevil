package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
@Service
@Transactional
public class ShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViService.class);

    private final ShipmentViRepository shipmentViRepository;

    public ShipmentViService(ShipmentViRepository shipmentViRepository) {
        this.shipmentViRepository = shipmentViRepository;
    }

    /**
     * Save a shipmentVi.
     *
     * @param shipmentVi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentVi save(ShipmentVi shipmentVi) {
        LOG.debug("Request to save ShipmentVi : {}", shipmentVi);
        return shipmentViRepository.save(shipmentVi);
    }

    /**
     * Update a shipmentVi.
     *
     * @param shipmentVi the entity to save.
     * @return the persisted entity.
     */
    public ShipmentVi update(ShipmentVi shipmentVi) {
        LOG.debug("Request to update ShipmentVi : {}", shipmentVi);
        return shipmentViRepository.save(shipmentVi);
    }

    /**
     * Partially update a shipmentVi.
     *
     * @param shipmentVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentVi> partialUpdate(ShipmentVi shipmentVi) {
        LOG.debug("Request to partially update ShipmentVi : {}", shipmentVi);

        return shipmentViRepository
            .findById(shipmentVi.getId())
            .map(existingShipmentVi -> {
                if (shipmentVi.getTrackingNumber() != null) {
                    existingShipmentVi.setTrackingNumber(shipmentVi.getTrackingNumber());
                }
                if (shipmentVi.getShippedDate() != null) {
                    existingShipmentVi.setShippedDate(shipmentVi.getShippedDate());
                }
                if (shipmentVi.getDeliveryDate() != null) {
                    existingShipmentVi.setDeliveryDate(shipmentVi.getDeliveryDate());
                }

                return existingShipmentVi;
            })
            .map(shipmentViRepository::save);
    }

    /**
     * Get one shipmentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentVi> findOne(Long id) {
        LOG.debug("Request to get ShipmentVi : {}", id);
        return shipmentViRepository.findById(id);
    }

    /**
     * Delete the shipmentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentVi : {}", id);
        shipmentViRepository.deleteById(id);
    }
}
