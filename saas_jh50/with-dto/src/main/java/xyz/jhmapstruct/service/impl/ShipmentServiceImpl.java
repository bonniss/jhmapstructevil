package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.repository.ShipmentRepository;
import xyz.jhmapstruct.service.ShipmentService;
import xyz.jhmapstruct.service.dto.ShipmentDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    private final ShipmentRepository shipmentRepository;

    private final ShipmentMapper shipmentMapper;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    public ShipmentDTO save(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to save Shipment : {}", shipmentDTO);
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);
        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }

    @Override
    public ShipmentDTO update(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to update Shipment : {}", shipmentDTO);
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);
        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }

    @Override
    public Optional<ShipmentDTO> partialUpdate(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to partially update Shipment : {}", shipmentDTO);

        return shipmentRepository
            .findById(shipmentDTO.getId())
            .map(existingShipment -> {
                shipmentMapper.partialUpdate(existingShipment, shipmentDTO);

                return existingShipment;
            })
            .map(shipmentRepository::save)
            .map(shipmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentDTO> findAll() {
        LOG.debug("Request to get all Shipments");
        return shipmentRepository.findAll().stream().map(shipmentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentDTO> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id).map(shipmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
    }
}
