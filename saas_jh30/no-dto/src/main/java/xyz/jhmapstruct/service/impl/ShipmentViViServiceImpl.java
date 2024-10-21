package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.ShipmentViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
@Service
@Transactional
public class ShipmentViViServiceImpl implements ShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViViServiceImpl.class);

    private final ShipmentViViRepository shipmentViViRepository;

    public ShipmentViViServiceImpl(ShipmentViViRepository shipmentViViRepository) {
        this.shipmentViViRepository = shipmentViViRepository;
    }

    @Override
    public ShipmentViVi save(ShipmentViVi shipmentViVi) {
        LOG.debug("Request to save ShipmentViVi : {}", shipmentViVi);
        return shipmentViViRepository.save(shipmentViVi);
    }

    @Override
    public ShipmentViVi update(ShipmentViVi shipmentViVi) {
        LOG.debug("Request to update ShipmentViVi : {}", shipmentViVi);
        return shipmentViViRepository.save(shipmentViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentViVi> findAll() {
        LOG.debug("Request to get all ShipmentViVis");
        return shipmentViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentViVi> findOne(Long id) {
        LOG.debug("Request to get ShipmentViVi : {}", id);
        return shipmentViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentViVi : {}", id);
        shipmentViViRepository.deleteById(id);
    }
}
