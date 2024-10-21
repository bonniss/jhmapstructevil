package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.ShipmentMiService;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
@Service
@Transactional
public class ShipmentMiServiceImpl implements ShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiServiceImpl.class);

    private final ShipmentMiRepository shipmentMiRepository;

    private final ShipmentMiMapper shipmentMiMapper;

    public ShipmentMiServiceImpl(ShipmentMiRepository shipmentMiRepository, ShipmentMiMapper shipmentMiMapper) {
        this.shipmentMiRepository = shipmentMiRepository;
        this.shipmentMiMapper = shipmentMiMapper;
    }

    @Override
    public ShipmentMiDTO save(ShipmentMiDTO shipmentMiDTO) {
        LOG.debug("Request to save ShipmentMi : {}", shipmentMiDTO);
        ShipmentMi shipmentMi = shipmentMiMapper.toEntity(shipmentMiDTO);
        shipmentMi = shipmentMiRepository.save(shipmentMi);
        return shipmentMiMapper.toDto(shipmentMi);
    }

    @Override
    public ShipmentMiDTO update(ShipmentMiDTO shipmentMiDTO) {
        LOG.debug("Request to update ShipmentMi : {}", shipmentMiDTO);
        ShipmentMi shipmentMi = shipmentMiMapper.toEntity(shipmentMiDTO);
        shipmentMi = shipmentMiRepository.save(shipmentMi);
        return shipmentMiMapper.toDto(shipmentMi);
    }

    @Override
    public Optional<ShipmentMiDTO> partialUpdate(ShipmentMiDTO shipmentMiDTO) {
        LOG.debug("Request to partially update ShipmentMi : {}", shipmentMiDTO);

        return shipmentMiRepository
            .findById(shipmentMiDTO.getId())
            .map(existingShipmentMi -> {
                shipmentMiMapper.partialUpdate(existingShipmentMi, shipmentMiDTO);

                return existingShipmentMi;
            })
            .map(shipmentMiRepository::save)
            .map(shipmentMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentMiDTO> findAll() {
        LOG.debug("Request to get all ShipmentMis");
        return shipmentMiRepository.findAll().stream().map(shipmentMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentMiDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentMi : {}", id);
        return shipmentMiRepository.findById(id).map(shipmentMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMi : {}", id);
        shipmentMiRepository.deleteById(id);
    }
}
