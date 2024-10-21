package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.ShipmentMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
@Service
@Transactional
public class ShipmentMiServiceImpl implements ShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiServiceImpl.class);

    private final ShipmentMiRepository shipmentMiRepository;

    public ShipmentMiServiceImpl(ShipmentMiRepository shipmentMiRepository) {
        this.shipmentMiRepository = shipmentMiRepository;
    }

    @Override
    public ShipmentMi save(ShipmentMi shipmentMi) {
        LOG.debug("Request to save ShipmentMi : {}", shipmentMi);
        return shipmentMiRepository.save(shipmentMi);
    }

    @Override
    public ShipmentMi update(ShipmentMi shipmentMi) {
        LOG.debug("Request to update ShipmentMi : {}", shipmentMi);
        return shipmentMiRepository.save(shipmentMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentMi> findAll() {
        LOG.debug("Request to get all ShipmentMis");
        return shipmentMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentMi> findOne(Long id) {
        LOG.debug("Request to get ShipmentMi : {}", id);
        return shipmentMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMi : {}", id);
        shipmentMiRepository.deleteById(id);
    }
}
