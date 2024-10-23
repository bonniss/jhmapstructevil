package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.repository.ShipmentRepository;
import xyz.jhmapstruct.service.dto.ShipmentDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

    private final ShipmentMapper shipmentMapper;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    /**
     * Save a shipment.
     *
     * @param shipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentDTO save(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to save Shipment : {}", shipmentDTO);
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);
        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }

    /**
     * Update a shipment.
     *
     * @param shipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentDTO update(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to update Shipment : {}", shipmentDTO);
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);
        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }

    /**
     * Partially update a shipment.
     *
     * @param shipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShipmentDTO> partialUpdate(ShipmentDTO shipmentDTO) {
        LOG.debug("Request to partially update Shipment : {}", shipmentDTO);

        return shipmentRepository
            .findById(shipmentDTO.getId())
            .map(existingShipment -> {
                shipmentMapper.partialUpdate(existingShipment, shipmentDTO);

                return existingShipment;
            })
            .map(shipmentRepository::save)
            .map(shipmentMapper::toDto);
    }

    /**
     * Get one shipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentDTO> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id).map(shipmentMapper::toDto);
    }

    /**
     * Delete the shipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
    }
}
