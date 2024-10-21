package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.ShipmentViService;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;
import xyz.jhmapstruct.service.mapper.ShipmentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
@Service
@Transactional
public class ShipmentViServiceImpl implements ShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViServiceImpl.class);

    private final ShipmentViRepository shipmentViRepository;

    private final ShipmentViMapper shipmentViMapper;

    public ShipmentViServiceImpl(ShipmentViRepository shipmentViRepository, ShipmentViMapper shipmentViMapper) {
        this.shipmentViRepository = shipmentViRepository;
        this.shipmentViMapper = shipmentViMapper;
    }

    @Override
    public ShipmentViDTO save(ShipmentViDTO shipmentViDTO) {
        LOG.debug("Request to save ShipmentVi : {}", shipmentViDTO);
        ShipmentVi shipmentVi = shipmentViMapper.toEntity(shipmentViDTO);
        shipmentVi = shipmentViRepository.save(shipmentVi);
        return shipmentViMapper.toDto(shipmentVi);
    }

    @Override
    public ShipmentViDTO update(ShipmentViDTO shipmentViDTO) {
        LOG.debug("Request to update ShipmentVi : {}", shipmentViDTO);
        ShipmentVi shipmentVi = shipmentViMapper.toEntity(shipmentViDTO);
        shipmentVi = shipmentViRepository.save(shipmentVi);
        return shipmentViMapper.toDto(shipmentVi);
    }

    @Override
    public Optional<ShipmentViDTO> partialUpdate(ShipmentViDTO shipmentViDTO) {
        LOG.debug("Request to partially update ShipmentVi : {}", shipmentViDTO);

        return shipmentViRepository
            .findById(shipmentViDTO.getId())
            .map(existingShipmentVi -> {
                shipmentViMapper.partialUpdate(existingShipmentVi, shipmentViDTO);

                return existingShipmentVi;
            })
            .map(shipmentViRepository::save)
            .map(shipmentViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentViDTO> findAll() {
        LOG.debug("Request to get all ShipmentVis");
        return shipmentViRepository.findAll().stream().map(shipmentViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentViDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentVi : {}", id);
        return shipmentViRepository.findById(id).map(shipmentViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentVi : {}", id);
        shipmentViRepository.deleteById(id);
    }
}
