package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.repository.NextShipmentRepository;
import xyz.jhmapstruct.service.dto.NextShipmentDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipment}.
 */
@Service
@Transactional
public class NextShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentService.class);

    private final NextShipmentRepository nextShipmentRepository;

    private final NextShipmentMapper nextShipmentMapper;

    public NextShipmentService(NextShipmentRepository nextShipmentRepository, NextShipmentMapper nextShipmentMapper) {
        this.nextShipmentRepository = nextShipmentRepository;
        this.nextShipmentMapper = nextShipmentMapper;
    }

    /**
     * Save a nextShipment.
     *
     * @param nextShipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentDTO save(NextShipmentDTO nextShipmentDTO) {
        LOG.debug("Request to save NextShipment : {}", nextShipmentDTO);
        NextShipment nextShipment = nextShipmentMapper.toEntity(nextShipmentDTO);
        nextShipment = nextShipmentRepository.save(nextShipment);
        return nextShipmentMapper.toDto(nextShipment);
    }

    /**
     * Update a nextShipment.
     *
     * @param nextShipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentDTO update(NextShipmentDTO nextShipmentDTO) {
        LOG.debug("Request to update NextShipment : {}", nextShipmentDTO);
        NextShipment nextShipment = nextShipmentMapper.toEntity(nextShipmentDTO);
        nextShipment = nextShipmentRepository.save(nextShipment);
        return nextShipmentMapper.toDto(nextShipment);
    }

    /**
     * Partially update a nextShipment.
     *
     * @param nextShipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentDTO> partialUpdate(NextShipmentDTO nextShipmentDTO) {
        LOG.debug("Request to partially update NextShipment : {}", nextShipmentDTO);

        return nextShipmentRepository
            .findById(nextShipmentDTO.getId())
            .map(existingNextShipment -> {
                nextShipmentMapper.partialUpdate(existingNextShipment, nextShipmentDTO);

                return existingNextShipment;
            })
            .map(nextShipmentRepository::save)
            .map(nextShipmentMapper::toDto);
    }

    /**
     * Get one nextShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipment : {}", id);
        return nextShipmentRepository.findById(id).map(nextShipmentMapper::toDto);
    }

    /**
     * Delete the nextShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipment : {}", id);
        nextShipmentRepository.deleteById(id);
    }
}
