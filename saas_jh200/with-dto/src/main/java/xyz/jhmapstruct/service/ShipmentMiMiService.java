package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
@Service
@Transactional
public class ShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiService.class);

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    private final ShipmentMiMiMapper shipmentMiMiMapper;

    public ShipmentMiMiService(ShipmentMiMiRepository shipmentMiMiRepository, ShipmentMiMiMapper shipmentMiMiMapper) {
        this.shipmentMiMiRepository = shipmentMiMiRepository;
        this.shipmentMiMiMapper = shipmentMiMiMapper;
    }

    /**
     * Save a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiMiDTO save(ShipmentMiMiDTO shipmentMiMiDTO) {
        LOG.debug("Request to save ShipmentMiMi : {}", shipmentMiMiDTO);
        ShipmentMiMi shipmentMiMi = shipmentMiMiMapper.toEntity(shipmentMiMiDTO);
        shipmentMiMi = shipmentMiMiRepository.save(shipmentMiMi);
        return shipmentMiMiMapper.toDto(shipmentMiMi);
    }

    /**
     * Update a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiMiDTO update(ShipmentMiMiDTO shipmentMiMiDTO) {
        LOG.debug("Request to update ShipmentMiMi : {}", shipmentMiMiDTO);
        ShipmentMiMi shipmentMiMi = shipmentMiMiMapper.toEntity(shipmentMiMiDTO);
        shipmentMiMi = shipmentMiMiRepository.save(shipmentMiMi);
        return shipmentMiMiMapper.toDto(shipmentMiMi);
    }

    /**
     * Partially update a shipmentMiMi.
     *
     * @param shipmentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one shipmentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentMiMi : {}", id);
        return shipmentMiMiRepository.findById(id).map(shipmentMiMiMapper::toDto);
    }

    /**
     * Delete the shipmentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMiMi : {}", id);
        shipmentMiMiRepository.deleteById(id);
    }
}
