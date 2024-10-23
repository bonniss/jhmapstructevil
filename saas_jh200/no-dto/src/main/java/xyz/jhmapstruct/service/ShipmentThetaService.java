package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.repository.ShipmentThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentTheta}.
 */
@Service
@Transactional
public class ShipmentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentThetaService.class);

    private final ShipmentThetaRepository shipmentThetaRepository;

    public ShipmentThetaService(ShipmentThetaRepository shipmentThetaRepository) {
        this.shipmentThetaRepository = shipmentThetaRepository;
    }

    /**
     * Save a shipmentTheta.
     *
     * @param shipmentTheta the entity to save.
     * @return the persisted entity.
     */
    public ShipmentTheta save(ShipmentTheta shipmentTheta) {
        LOG.debug("Request to save ShipmentTheta : {}", shipmentTheta);
        return shipmentThetaRepository.save(shipmentTheta);
    }

    /**
     * Update a shipmentTheta.
     *
     * @param shipmentTheta the entity to save.
     * @return the persisted entity.
     */
    public ShipmentTheta update(ShipmentTheta shipmentTheta) {
        LOG.debug("Request to update ShipmentTheta : {}", shipmentTheta);
        return shipmentThetaRepository.save(shipmentTheta);
    }

    /**
     * Partially update a shipmentTheta.
     *
     * @param shipmentTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentTheta> partialUpdate(ShipmentTheta shipmentTheta) {
        LOG.debug("Request to partially update ShipmentTheta : {}", shipmentTheta);

        return shipmentThetaRepository
            .findById(shipmentTheta.getId())
            .map(existingShipmentTheta -> {
                if (shipmentTheta.getTrackingNumber() != null) {
                    existingShipmentTheta.setTrackingNumber(shipmentTheta.getTrackingNumber());
                }
                if (shipmentTheta.getShippedDate() != null) {
                    existingShipmentTheta.setShippedDate(shipmentTheta.getShippedDate());
                }
                if (shipmentTheta.getDeliveryDate() != null) {
                    existingShipmentTheta.setDeliveryDate(shipmentTheta.getDeliveryDate());
                }

                return existingShipmentTheta;
            })
            .map(shipmentThetaRepository::save);
    }

    /**
     * Get one shipmentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentTheta> findOne(Long id) {
        LOG.debug("Request to get ShipmentTheta : {}", id);
        return shipmentThetaRepository.findById(id);
    }

    /**
     * Delete the shipmentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentTheta : {}", id);
        shipmentThetaRepository.deleteById(id);
    }
}
