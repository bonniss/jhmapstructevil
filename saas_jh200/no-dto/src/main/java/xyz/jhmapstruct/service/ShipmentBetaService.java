package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.repository.ShipmentBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentBeta}.
 */
@Service
@Transactional
public class ShipmentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentBetaService.class);

    private final ShipmentBetaRepository shipmentBetaRepository;

    public ShipmentBetaService(ShipmentBetaRepository shipmentBetaRepository) {
        this.shipmentBetaRepository = shipmentBetaRepository;
    }

    /**
     * Save a shipmentBeta.
     *
     * @param shipmentBeta the entity to save.
     * @return the persisted entity.
     */
    public ShipmentBeta save(ShipmentBeta shipmentBeta) {
        LOG.debug("Request to save ShipmentBeta : {}", shipmentBeta);
        return shipmentBetaRepository.save(shipmentBeta);
    }

    /**
     * Update a shipmentBeta.
     *
     * @param shipmentBeta the entity to save.
     * @return the persisted entity.
     */
    public ShipmentBeta update(ShipmentBeta shipmentBeta) {
        LOG.debug("Request to update ShipmentBeta : {}", shipmentBeta);
        return shipmentBetaRepository.save(shipmentBeta);
    }

    /**
     * Partially update a shipmentBeta.
     *
     * @param shipmentBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentBeta> partialUpdate(ShipmentBeta shipmentBeta) {
        LOG.debug("Request to partially update ShipmentBeta : {}", shipmentBeta);

        return shipmentBetaRepository
            .findById(shipmentBeta.getId())
            .map(existingShipmentBeta -> {
                if (shipmentBeta.getTrackingNumber() != null) {
                    existingShipmentBeta.setTrackingNumber(shipmentBeta.getTrackingNumber());
                }
                if (shipmentBeta.getShippedDate() != null) {
                    existingShipmentBeta.setShippedDate(shipmentBeta.getShippedDate());
                }
                if (shipmentBeta.getDeliveryDate() != null) {
                    existingShipmentBeta.setDeliveryDate(shipmentBeta.getDeliveryDate());
                }

                return existingShipmentBeta;
            })
            .map(shipmentBetaRepository::save);
    }

    /**
     * Get one shipmentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentBeta> findOne(Long id) {
        LOG.debug("Request to get ShipmentBeta : {}", id);
        return shipmentBetaRepository.findById(id);
    }

    /**
     * Delete the shipmentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentBeta : {}", id);
        shipmentBetaRepository.deleteById(id);
    }
}
