package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.repository.ShipmentAlphaRepository;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentAlpha}.
 */
@Service
@Transactional
public class ShipmentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAlphaService.class);

    private final ShipmentAlphaRepository shipmentAlphaRepository;

    private final ShipmentAlphaMapper shipmentAlphaMapper;

    public ShipmentAlphaService(ShipmentAlphaRepository shipmentAlphaRepository, ShipmentAlphaMapper shipmentAlphaMapper) {
        this.shipmentAlphaRepository = shipmentAlphaRepository;
        this.shipmentAlphaMapper = shipmentAlphaMapper;
    }

    /**
     * Save a shipmentAlpha.
     *
     * @param shipmentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAlphaDTO save(ShipmentAlphaDTO shipmentAlphaDTO) {
        LOG.debug("Request to save ShipmentAlpha : {}", shipmentAlphaDTO);
        ShipmentAlpha shipmentAlpha = shipmentAlphaMapper.toEntity(shipmentAlphaDTO);
        shipmentAlpha = shipmentAlphaRepository.save(shipmentAlpha);
        return shipmentAlphaMapper.toDto(shipmentAlpha);
    }

    /**
     * Update a shipmentAlpha.
     *
     * @param shipmentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentAlphaDTO update(ShipmentAlphaDTO shipmentAlphaDTO) {
        LOG.debug("Request to update ShipmentAlpha : {}", shipmentAlphaDTO);
        ShipmentAlpha shipmentAlpha = shipmentAlphaMapper.toEntity(shipmentAlphaDTO);
        shipmentAlpha = shipmentAlphaRepository.save(shipmentAlpha);
        return shipmentAlphaMapper.toDto(shipmentAlpha);
    }

    /**
     * Partially update a shipmentAlpha.
     *
     * @param shipmentAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentAlphaDTO> partialUpdate(ShipmentAlphaDTO shipmentAlphaDTO) {
        LOG.debug("Request to partially update ShipmentAlpha : {}", shipmentAlphaDTO);

        return shipmentAlphaRepository
            .findById(shipmentAlphaDTO.getId())
            .map(existingShipmentAlpha -> {
                shipmentAlphaMapper.partialUpdate(existingShipmentAlpha, shipmentAlphaDTO);

                return existingShipmentAlpha;
            })
            .map(shipmentAlphaRepository::save)
            .map(shipmentAlphaMapper::toDto);
    }

    /**
     * Get one shipmentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentAlpha : {}", id);
        return shipmentAlphaRepository.findById(id).map(shipmentAlphaMapper::toDto);
    }

    /**
     * Delete the shipmentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentAlpha : {}", id);
        shipmentAlphaRepository.deleteById(id);
    }
}
