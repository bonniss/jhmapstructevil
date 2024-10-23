package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;
import xyz.jhmapstruct.service.mapper.ShipmentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
@Service
@Transactional
public class ShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViService.class);

    private final ShipmentViRepository shipmentViRepository;

    private final ShipmentViMapper shipmentViMapper;

    public ShipmentViService(ShipmentViRepository shipmentViRepository, ShipmentViMapper shipmentViMapper) {
        this.shipmentViRepository = shipmentViRepository;
        this.shipmentViMapper = shipmentViMapper;
    }

    /**
     * Save a shipmentVi.
     *
     * @param shipmentViDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViDTO save(ShipmentViDTO shipmentViDTO) {
        LOG.debug("Request to save ShipmentVi : {}", shipmentViDTO);
        ShipmentVi shipmentVi = shipmentViMapper.toEntity(shipmentViDTO);
        shipmentVi = shipmentViRepository.save(shipmentVi);
        return shipmentViMapper.toDto(shipmentVi);
    }

    /**
     * Update a shipmentVi.
     *
     * @param shipmentViDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentViDTO update(ShipmentViDTO shipmentViDTO) {
        LOG.debug("Request to update ShipmentVi : {}", shipmentViDTO);
        ShipmentVi shipmentVi = shipmentViMapper.toEntity(shipmentViDTO);
        shipmentVi = shipmentViRepository.save(shipmentVi);
        return shipmentViMapper.toDto(shipmentVi);
    }

    /**
     * Partially update a shipmentVi.
     *
     * @param shipmentViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one shipmentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentViDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentVi : {}", id);
        return shipmentViRepository.findById(id).map(shipmentViMapper::toDto);
    }

    /**
     * Delete the shipmentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentVi : {}", id);
        shipmentViRepository.deleteById(id);
    }
}
