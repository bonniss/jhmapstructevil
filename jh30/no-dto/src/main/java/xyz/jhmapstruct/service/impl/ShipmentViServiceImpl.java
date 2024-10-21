package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.ShipmentViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
@Service
@Transactional
public class ShipmentViServiceImpl implements ShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViServiceImpl.class);

    private final ShipmentViRepository shipmentViRepository;

    public ShipmentViServiceImpl(ShipmentViRepository shipmentViRepository) {
        this.shipmentViRepository = shipmentViRepository;
    }

    @Override
    public ShipmentVi save(ShipmentVi shipmentVi) {
        LOG.debug("Request to save ShipmentVi : {}", shipmentVi);
        return shipmentViRepository.save(shipmentVi);
    }

    @Override
    public ShipmentVi update(ShipmentVi shipmentVi) {
        LOG.debug("Request to update ShipmentVi : {}", shipmentVi);
        return shipmentViRepository.save(shipmentVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentVi> findAll() {
        LOG.debug("Request to get all ShipmentVis");
        return shipmentViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentVi> findOne(Long id) {
        LOG.debug("Request to get ShipmentVi : {}", id);
        return shipmentViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentVi : {}", id);
        shipmentViRepository.deleteById(id);
    }
}
