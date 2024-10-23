package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.repository.ShipmentGammaRepository;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentGamma}.
 */
@Service
@Transactional
public class ShipmentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentGammaService.class);

    private final ShipmentGammaRepository shipmentGammaRepository;

    private final ShipmentGammaMapper shipmentGammaMapper;

    public ShipmentGammaService(ShipmentGammaRepository shipmentGammaRepository, ShipmentGammaMapper shipmentGammaMapper) {
        this.shipmentGammaRepository = shipmentGammaRepository;
        this.shipmentGammaMapper = shipmentGammaMapper;
    }

    /**
     * Save a shipmentGamma.
     *
     * @param shipmentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentGammaDTO save(ShipmentGammaDTO shipmentGammaDTO) {
        LOG.debug("Request to save ShipmentGamma : {}", shipmentGammaDTO);
        ShipmentGamma shipmentGamma = shipmentGammaMapper.toEntity(shipmentGammaDTO);
        shipmentGamma = shipmentGammaRepository.save(shipmentGamma);
        return shipmentGammaMapper.toDto(shipmentGamma);
    }

    /**
     * Update a shipmentGamma.
     *
     * @param shipmentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentGammaDTO update(ShipmentGammaDTO shipmentGammaDTO) {
        LOG.debug("Request to update ShipmentGamma : {}", shipmentGammaDTO);
        ShipmentGamma shipmentGamma = shipmentGammaMapper.toEntity(shipmentGammaDTO);
        shipmentGamma = shipmentGammaRepository.save(shipmentGamma);
        return shipmentGammaMapper.toDto(shipmentGamma);
    }

    /**
     * Partially update a shipmentGamma.
     *
     * @param shipmentGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentGammaDTO> partialUpdate(ShipmentGammaDTO shipmentGammaDTO) {
        LOG.debug("Request to partially update ShipmentGamma : {}", shipmentGammaDTO);

        return shipmentGammaRepository
            .findById(shipmentGammaDTO.getId())
            .map(existingShipmentGamma -> {
                shipmentGammaMapper.partialUpdate(existingShipmentGamma, shipmentGammaDTO);

                return existingShipmentGamma;
            })
            .map(shipmentGammaRepository::save)
            .map(shipmentGammaMapper::toDto);
    }

    /**
     * Get one shipmentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentGammaDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentGamma : {}", id);
        return shipmentGammaRepository.findById(id).map(shipmentGammaMapper::toDto);
    }

    /**
     * Delete the shipmentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentGamma : {}", id);
        shipmentGammaRepository.deleteById(id);
    }
}
