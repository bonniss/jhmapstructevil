package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.repository.ShipmentGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentGamma}.
 */
@Service
@Transactional
public class ShipmentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentGammaService.class);

    private final ShipmentGammaRepository shipmentGammaRepository;

    public ShipmentGammaService(ShipmentGammaRepository shipmentGammaRepository) {
        this.shipmentGammaRepository = shipmentGammaRepository;
    }

    /**
     * Save a shipmentGamma.
     *
     * @param shipmentGamma the entity to save.
     * @return the persisted entity.
     */
    public ShipmentGamma save(ShipmentGamma shipmentGamma) {
        LOG.debug("Request to save ShipmentGamma : {}", shipmentGamma);
        return shipmentGammaRepository.save(shipmentGamma);
    }

    /**
     * Update a shipmentGamma.
     *
     * @param shipmentGamma the entity to save.
     * @return the persisted entity.
     */
    public ShipmentGamma update(ShipmentGamma shipmentGamma) {
        LOG.debug("Request to update ShipmentGamma : {}", shipmentGamma);
        return shipmentGammaRepository.save(shipmentGamma);
    }

    /**
     * Partially update a shipmentGamma.
     *
     * @param shipmentGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentGamma> partialUpdate(ShipmentGamma shipmentGamma) {
        LOG.debug("Request to partially update ShipmentGamma : {}", shipmentGamma);

        return shipmentGammaRepository
            .findById(shipmentGamma.getId())
            .map(existingShipmentGamma -> {
                if (shipmentGamma.getTrackingNumber() != null) {
                    existingShipmentGamma.setTrackingNumber(shipmentGamma.getTrackingNumber());
                }
                if (shipmentGamma.getShippedDate() != null) {
                    existingShipmentGamma.setShippedDate(shipmentGamma.getShippedDate());
                }
                if (shipmentGamma.getDeliveryDate() != null) {
                    existingShipmentGamma.setDeliveryDate(shipmentGamma.getDeliveryDate());
                }

                return existingShipmentGamma;
            })
            .map(shipmentGammaRepository::save);
    }

    /**
     * Get one shipmentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentGamma> findOne(Long id) {
        LOG.debug("Request to get ShipmentGamma : {}", id);
        return shipmentGammaRepository.findById(id);
    }

    /**
     * Delete the shipmentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentGamma : {}", id);
        shipmentGammaRepository.deleteById(id);
    }
}
