package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.repository.ShipmentBetaRepository;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentBeta}.
 */
@Service
@Transactional
public class ShipmentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentBetaService.class);

    private final ShipmentBetaRepository shipmentBetaRepository;

    private final ShipmentBetaMapper shipmentBetaMapper;

    public ShipmentBetaService(ShipmentBetaRepository shipmentBetaRepository, ShipmentBetaMapper shipmentBetaMapper) {
        this.shipmentBetaRepository = shipmentBetaRepository;
        this.shipmentBetaMapper = shipmentBetaMapper;
    }

    /**
     * Save a shipmentBeta.
     *
     * @param shipmentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentBetaDTO save(ShipmentBetaDTO shipmentBetaDTO) {
        LOG.debug("Request to save ShipmentBeta : {}", shipmentBetaDTO);
        ShipmentBeta shipmentBeta = shipmentBetaMapper.toEntity(shipmentBetaDTO);
        shipmentBeta = shipmentBetaRepository.save(shipmentBeta);
        return shipmentBetaMapper.toDto(shipmentBeta);
    }

    /**
     * Update a shipmentBeta.
     *
     * @param shipmentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentBetaDTO update(ShipmentBetaDTO shipmentBetaDTO) {
        LOG.debug("Request to update ShipmentBeta : {}", shipmentBetaDTO);
        ShipmentBeta shipmentBeta = shipmentBetaMapper.toEntity(shipmentBetaDTO);
        shipmentBeta = shipmentBetaRepository.save(shipmentBeta);
        return shipmentBetaMapper.toDto(shipmentBeta);
    }

    /**
     * Partially update a shipmentBeta.
     *
     * @param shipmentBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentBetaDTO> partialUpdate(ShipmentBetaDTO shipmentBetaDTO) {
        LOG.debug("Request to partially update ShipmentBeta : {}", shipmentBetaDTO);

        return shipmentBetaRepository
            .findById(shipmentBetaDTO.getId())
            .map(existingShipmentBeta -> {
                shipmentBetaMapper.partialUpdate(existingShipmentBeta, shipmentBetaDTO);

                return existingShipmentBeta;
            })
            .map(shipmentBetaRepository::save)
            .map(shipmentBetaMapper::toDto);
    }

    /**
     * Get one shipmentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentBetaDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentBeta : {}", id);
        return shipmentBetaRepository.findById(id).map(shipmentBetaMapper::toDto);
    }

    /**
     * Delete the shipmentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentBeta : {}", id);
        shipmentBetaRepository.deleteById(id);
    }
}
