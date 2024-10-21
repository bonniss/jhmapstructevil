package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.ShipmentViViService;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.ShipmentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
@Service
@Transactional
public class ShipmentViViServiceImpl implements ShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViViServiceImpl.class);

    private final ShipmentViViRepository shipmentViViRepository;

    private final ShipmentViViMapper shipmentViViMapper;

    public ShipmentViViServiceImpl(ShipmentViViRepository shipmentViViRepository, ShipmentViViMapper shipmentViViMapper) {
        this.shipmentViViRepository = shipmentViViRepository;
        this.shipmentViViMapper = shipmentViViMapper;
    }

    @Override
    public ShipmentViViDTO save(ShipmentViViDTO shipmentViViDTO) {
        LOG.debug("Request to save ShipmentViVi : {}", shipmentViViDTO);
        ShipmentViVi shipmentViVi = shipmentViViMapper.toEntity(shipmentViViDTO);
        shipmentViVi = shipmentViViRepository.save(shipmentViVi);
        return shipmentViViMapper.toDto(shipmentViVi);
    }

    @Override
    public ShipmentViViDTO update(ShipmentViViDTO shipmentViViDTO) {
        LOG.debug("Request to update ShipmentViVi : {}", shipmentViViDTO);
        ShipmentViVi shipmentViVi = shipmentViViMapper.toEntity(shipmentViViDTO);
        shipmentViVi = shipmentViViRepository.save(shipmentViVi);
        return shipmentViViMapper.toDto(shipmentViVi);
    }

    @Override
    public Optional<ShipmentViViDTO> partialUpdate(ShipmentViViDTO shipmentViViDTO) {
        LOG.debug("Request to partially update ShipmentViVi : {}", shipmentViViDTO);

        return shipmentViViRepository
            .findById(shipmentViViDTO.getId())
            .map(existingShipmentViVi -> {
                shipmentViViMapper.partialUpdate(existingShipmentViVi, shipmentViViDTO);

                return existingShipmentViVi;
            })
            .map(shipmentViViRepository::save)
            .map(shipmentViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentViViDTO> findAll() {
        LOG.debug("Request to get all ShipmentViVis");
        return shipmentViViRepository.findAll().stream().map(shipmentViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentViViDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentViVi : {}", id);
        return shipmentViViRepository.findById(id).map(shipmentViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentViVi : {}", id);
        shipmentViViRepository.deleteById(id);
    }
}
