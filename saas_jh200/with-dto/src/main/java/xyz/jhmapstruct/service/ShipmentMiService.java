package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
@Service
@Transactional
public class ShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiService.class);

    private final ShipmentMiRepository shipmentMiRepository;

    private final ShipmentMiMapper shipmentMiMapper;

    public ShipmentMiService(ShipmentMiRepository shipmentMiRepository, ShipmentMiMapper shipmentMiMapper) {
        this.shipmentMiRepository = shipmentMiRepository;
        this.shipmentMiMapper = shipmentMiMapper;
    }

    /**
     * Save a shipmentMi.
     *
     * @param shipmentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiDTO save(ShipmentMiDTO shipmentMiDTO) {
        LOG.debug("Request to save ShipmentMi : {}", shipmentMiDTO);
        ShipmentMi shipmentMi = shipmentMiMapper.toEntity(shipmentMiDTO);
        shipmentMi = shipmentMiRepository.save(shipmentMi);
        return shipmentMiMapper.toDto(shipmentMi);
    }

    /**
     * Update a shipmentMi.
     *
     * @param shipmentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentMiDTO update(ShipmentMiDTO shipmentMiDTO) {
        LOG.debug("Request to update ShipmentMi : {}", shipmentMiDTO);
        ShipmentMi shipmentMi = shipmentMiMapper.toEntity(shipmentMiDTO);
        shipmentMi = shipmentMiRepository.save(shipmentMi);
        return shipmentMiMapper.toDto(shipmentMi);
    }

    /**
     * Partially update a shipmentMi.
     *
     * @param shipmentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one shipmentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentMiDTO> findOne(Long id) {
        LOG.debug("Request to get ShipmentMi : {}", id);
        return shipmentMiRepository.findById(id).map(shipmentMiMapper::toDto);
    }

    /**
     * Delete the shipmentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShipmentMi : {}", id);
        shipmentMiRepository.deleteById(id);
    }
}
