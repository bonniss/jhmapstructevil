package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.repository.ShipmentRepository;
import xyz.jhmapstruct.service.ShipmentService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public Shipment save(Shipment shipment) {
        LOG.debug("Request to save Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment update(Shipment shipment) {
        LOG.debug("Request to update Shipment : {}", shipment);
        return shipmentRepository.save(shipment);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<Shipment> findAll() {
        LOG.debug("Request to get all Shipments");
        return shipmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
    }
}
