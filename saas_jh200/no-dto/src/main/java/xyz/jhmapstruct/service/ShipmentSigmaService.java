package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.repository.ShipmentSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentSigma}.
 */
@Service
@Transactional
public class ShipmentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentSigmaService.class);

    private final ShipmentSigmaRepository shipmentSigmaRepository;

    public ShipmentSigmaService(ShipmentSigmaRepository shipmentSigmaRepository) {
        this.shipmentSigmaRepository = shipmentSigmaRepository;
    }

    /**
     * Save a shipmentSigma.
     *
     * @param shipmentSigma the entity to save.
     * @return the persisted entity.
     */
    public ShipmentSigma save(ShipmentSigma shipmentSigma) {
        LOG.debug("Request to save ShipmentSigma : {}", shipmentSigma);
        return shipmentSigmaRepository.save(shipmentSigma);
    }

    /**
     * Update a shipmentSigma.
     *
     * @param shipmentSigma the entity to save.
     * @return the persisted entity.
     */
    public ShipmentSigma update(ShipmentSigma shipmentSigma) {
        LOG.debug("Request to update ShipmentSigma : {}", shipmentSigma);
        return shipmentSigmaRepository.save(shipmentSigma);
    }

    /**
     * Partially update a shipmentSigma.
     *
     * @param shipmentSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentSigma> partialUpdate(ShipmentSigma shipmentSigma) {
        LOG.debug("Request to partially update ShipmentSigma : {}", shipmentSigma);

        return shipmentSigmaRepository
            .findById(shipmentSigma.getId())
            .map(existingShipmentSigma -> {
                if (shipmentSigma.getTrackingNumber() != null) {
                    existingShipmentSigma.setTrackingNumber(shipmentSigma.getTrackingNumber());
                }
                if (shipmentSigma.getShippedDate() != null) {
                    existingShipmentSigma.setShippedDate(shipmentSigma.getShippedDate());
                }
                if (shipmentSigma.getDeliveryDate() != null) {
                    existingShipmentSigma.setDeliveryDate(shipmentSigma.getDeliveryDate());
                }

                return existingShipmentSigma;
            })
            .map(shipmentSigmaRepository::save);
    }

    /**
     * Get one shipmentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentSigma> findOne(Long id) {
        LOG.debug("Request to get ShipmentSigma : {}", id);
        return shipmentSigmaRepository.findById(id);
    }

    /**
     * Delete the shipmentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentSigma : {}", id);
        shipmentSigmaRepository.deleteById(id);
    }
}
