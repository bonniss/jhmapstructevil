package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.ShipmentMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
@Service
@Transactional
public class ShipmentMiMiServiceImpl implements ShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiServiceImpl.class);

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    public ShipmentMiMiServiceImpl(ShipmentMiMiRepository shipmentMiMiRepository) {
        this.shipmentMiMiRepository = shipmentMiMiRepository;
    }

    @Override
    public ShipmentMiMi save(ShipmentMiMi shipmentMiMi) {
        LOG.debug("Request to save ShipmentMiMi : {}", shipmentMiMi);
        return shipmentMiMiRepository.save(shipmentMiMi);
    }

    @Override
    public ShipmentMiMi update(ShipmentMiMi shipmentMiMi) {
        LOG.debug("Request to update ShipmentMiMi : {}", shipmentMiMi);
        return shipmentMiMiRepository.save(shipmentMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentMiMi> findAll() {
        LOG.debug("Request to get all ShipmentMiMis");
        return shipmentMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentMiMi> findOne(Long id) {
        LOG.debug("Request to get ShipmentMiMi : {}", id);
        return shipmentMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMiMi : {}", id);
        shipmentMiMiRepository.deleteById(id);
    }
}
