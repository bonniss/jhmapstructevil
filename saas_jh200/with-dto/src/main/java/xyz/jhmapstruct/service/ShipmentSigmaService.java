package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.repository.ShipmentSigmaRepository;
import xyz.jhmapstruct.service.dto.ShipmentSigmaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentSigma}.
 */
@Service
@Transactional
public class ShipmentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentSigmaService.class);

    private final ShipmentSigmaRepository shipmentSigmaRepository;

    private final ShipmentSigmaMapper shipmentSigmaMapper;

    public ShipmentSigmaService(ShipmentSigmaRepository shipmentSigmaRepository, ShipmentSigmaMapper shipmentSigmaMapper) {
        this.shipmentSigmaRepository = shipmentSigmaRepository;
        this.shipmentSigmaMapper = shipmentSigmaMapper;
    }

    /**
     * Save a shipmentSigma.
     *
     * @param shipmentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentSigmaDTO save(ShipmentSigmaDTO shipmentSigmaDTO) {
        LOG.debug("Request to save ShipmentSigma : {}", shipmentSigmaDTO);
        ShipmentSigma shipmentSigma = shipmentSigmaMapper.toEntity(shipmentSigmaDTO);
        shipmentSigma = shipmentSigmaRepository.save(shipmentSigma);
        return shipmentSigmaMapper.toDto(shipmentSigma);
    }

    /**
     * Update a shipmentSigma.
     *
     * @param shipmentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentSigmaDTO update(ShipmentSigmaDTO shipmentSigmaDTO) {
        LOG.debug("Request to update ShipmentSigma : {}", shipmentSigmaDTO);
        ShipmentSigma shipmentSigma = shipmentSigmaMapper.toEntity(shipmentSigmaDTO);
        shipmentSigma = shipmentSigmaRepository.save(shipmentSigma);
        return shipmentSigmaMapper.toDto(shipmentSigma);
    }

    /**
     * Partially update a shipmentSigma.
     *
     * @param shipmentSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentSigmaDTO> partialUpdate(ShipmentSigmaDTO shipmentSigmaDTO) {
        LOG.debug("Request to partially update ShipmentSigma : {}", shipmentSigmaDTO);

        return shipmentSigmaRepository
            .findById(shipmentSigmaDTO.getId())
            .map(existingShipmentSigma -> {
                shipmentSigmaMapper.partialUpdate(existingShipmentSigma, shipmentSigmaDTO);

                return existingShipmentSigma;
            })
            .map(shipmentSigmaRepository::save)
            .map(shipmentSigmaMapper::toDto);
    }

    /**
     * Get one shipmentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentSigma : {}", id);
        return shipmentSigmaRepository.findById(id).map(shipmentSigmaMapper::toDto);
    }

    /**
     * Delete the shipmentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentSigma : {}", id);
        shipmentSigmaRepository.deleteById(id);
    }
}
