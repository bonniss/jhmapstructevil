package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.ShipmentMiMiService;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
@Service
@Transactional
public class ShipmentMiMiServiceImpl implements ShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiServiceImpl.class);

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    private final ShipmentMiMiMapper shipmentMiMiMapper;

    public ShipmentMiMiServiceImpl(ShipmentMiMiRepository shipmentMiMiRepository, ShipmentMiMiMapper shipmentMiMiMapper) {
        this.shipmentMiMiRepository = shipmentMiMiRepository;
        this.shipmentMiMiMapper = shipmentMiMiMapper;
    }

    @Override
    public ShipmentMiMiDTO save(ShipmentMiMiDTO shipmentMiMiDTO) {
        LOG.debug("Request to save ShipmentMiMi : {}", shipmentMiMiDTO);
        ShipmentMiMi shipmentMiMi = shipmentMiMiMapper.toEntity(shipmentMiMiDTO);
        shipmentMiMi = shipmentMiMiRepository.save(shipmentMiMi);
        return shipmentMiMiMapper.toDto(shipmentMiMi);
    }

    @Override
    public ShipmentMiMiDTO update(ShipmentMiMiDTO shipmentMiMiDTO) {
        LOG.debug("Request to update ShipmentMiMi : {}", shipmentMiMiDTO);
        ShipmentMiMi shipmentMiMi = shipmentMiMiMapper.toEntity(shipmentMiMiDTO);
        shipmentMiMi = shipmentMiMiRepository.save(shipmentMiMi);
        return shipmentMiMiMapper.toDto(shipmentMiMi);
    }

    @Override
    public Optional<ShipmentMiMiDTO> partialUpdate(ShipmentMiMiDTO shipmentMiMiDTO) {
        LOG.debug("Request to partially update ShipmentMiMi : {}", shipmentMiMiDTO);

        return shipmentMiMiRepository
            .findById(shipmentMiMiDTO.getId())
            .map(existingShipmentMiMi -> {
                shipmentMiMiMapper.partialUpdate(existingShipmentMiMi, shipmentMiMiDTO);

                return existingShipmentMiMi;
            })
            .map(shipmentMiMiRepository::save)
            .map(shipmentMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentMiMiDTO> findAll() {
        LOG.debug("Request to get all ShipmentMiMis");
        return shipmentMiMiRepository.findAll().stream().map(shipmentMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShipmentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentMiMi : {}", id);
        return shipmentMiMiRepository.findById(id).map(shipmentMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMiMi : {}", id);
        shipmentMiMiRepository.deleteById(id);
    }
}
