package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.ShipmentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
@Service
@Transactional
public class ShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViViService.class);

    private final ShipmentViViRepository shipmentViViRepository;

    private final ShipmentViViMapper shipmentViViMapper;

    public ShipmentViViService(ShipmentViViRepository shipmentViViRepository, ShipmentViViMapper shipmentViViMapper) {
        this.shipmentViViRepository = shipmentViViRepository;
        this.shipmentViViMapper = shipmentViViMapper;
    }

    /**
     * Save a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViViDTO save(ShipmentViViDTO shipmentViViDTO) {
        LOG.debug("Request to save ShipmentViVi : {}", shipmentViViDTO);
        ShipmentViVi shipmentViVi = shipmentViViMapper.toEntity(shipmentViViDTO);
        shipmentViVi = shipmentViViRepository.save(shipmentViVi);
        return shipmentViViMapper.toDto(shipmentViVi);
    }

    /**
     * Update a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViViDTO update(ShipmentViViDTO shipmentViViDTO) {
        LOG.debug("Request to update ShipmentViVi : {}", shipmentViViDTO);
        ShipmentViVi shipmentViVi = shipmentViViMapper.toEntity(shipmentViViDTO);
        shipmentViVi = shipmentViViRepository.save(shipmentViVi);
        return shipmentViViMapper.toDto(shipmentViVi);
    }

    /**
     * Partially update a shipmentViVi.
     *
     * @param shipmentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one shipmentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentViViDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentViVi : {}", id);
        return shipmentViViRepository.findById(id).map(shipmentViViMapper::toDto);
    }

    /**
     * Delete the shipmentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentViVi : {}", id);
        shipmentViViRepository.deleteById(id);
    }
}
