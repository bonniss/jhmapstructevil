package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.repository.ShipmentThetaRepository;
import xyz.jhmapstruct.service.dto.ShipmentThetaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentTheta}.
 */
@Service
@Transactional
public class ShipmentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentThetaService.class);

    private final ShipmentThetaRepository shipmentThetaRepository;

    private final ShipmentThetaMapper shipmentThetaMapper;

    public ShipmentThetaService(ShipmentThetaRepository shipmentThetaRepository, ShipmentThetaMapper shipmentThetaMapper) {
        this.shipmentThetaRepository = shipmentThetaRepository;
        this.shipmentThetaMapper = shipmentThetaMapper;
    }

    /**
     * Save a shipmentTheta.
     *
     * @param shipmentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentThetaDTO save(ShipmentThetaDTO shipmentThetaDTO) {
        LOG.debug("Request to save ShipmentTheta : {}", shipmentThetaDTO);
        ShipmentTheta shipmentTheta = shipmentThetaMapper.toEntity(shipmentThetaDTO);
        shipmentTheta = shipmentThetaRepository.save(shipmentTheta);
        return shipmentThetaMapper.toDto(shipmentTheta);
    }

    /**
     * Update a shipmentTheta.
     *
     * @param shipmentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentThetaDTO update(ShipmentThetaDTO shipmentThetaDTO) {
        LOG.debug("Request to update ShipmentTheta : {}", shipmentThetaDTO);
        ShipmentTheta shipmentTheta = shipmentThetaMapper.toEntity(shipmentThetaDTO);
        shipmentTheta = shipmentThetaRepository.save(shipmentTheta);
        return shipmentThetaMapper.toDto(shipmentTheta);
    }

    /**
     * Partially update a shipmentTheta.
     *
     * @param shipmentThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentThetaDTO> partialUpdate(ShipmentThetaDTO shipmentThetaDTO) {
        LOG.debug("Request to partially update ShipmentTheta : {}", shipmentThetaDTO);

        return shipmentThetaRepository
            .findById(shipmentThetaDTO.getId())
            .map(existingShipmentTheta -> {
                shipmentThetaMapper.partialUpdate(existingShipmentTheta, shipmentThetaDTO);

                return existingShipmentTheta;
            })
            .map(shipmentThetaRepository::save)
            .map(shipmentThetaMapper::toDto);
    }

    /**
     * Get one shipmentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentThetaDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentTheta : {}", id);
        return shipmentThetaRepository.findById(id).map(shipmentThetaMapper::toDto);
    }

    /**
     * Delete the shipmentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentTheta : {}", id);
        shipmentThetaRepository.deleteById(id);
    }
}
