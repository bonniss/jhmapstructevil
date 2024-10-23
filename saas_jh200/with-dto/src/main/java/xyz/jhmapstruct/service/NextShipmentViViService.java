package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.repository.NextShipmentViViRepository;
import xyz.jhmapstruct.service.dto.NextShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentViVi}.
 */
@Service
@Transactional
public class NextShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViViService.class);

    private final NextShipmentViViRepository nextShipmentViViRepository;

    private final NextShipmentViViMapper nextShipmentViViMapper;

    public NextShipmentViViService(NextShipmentViViRepository nextShipmentViViRepository, NextShipmentViViMapper nextShipmentViViMapper) {
        this.nextShipmentViViRepository = nextShipmentViViRepository;
        this.nextShipmentViViMapper = nextShipmentViViMapper;
    }

    /**
     * Save a nextShipmentViVi.
     *
     * @param nextShipmentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViViDTO save(NextShipmentViViDTO nextShipmentViViDTO) {
        LOG.debug("Request to save NextShipmentViVi : {}", nextShipmentViViDTO);
        NextShipmentViVi nextShipmentViVi = nextShipmentViViMapper.toEntity(nextShipmentViViDTO);
        nextShipmentViVi = nextShipmentViViRepository.save(nextShipmentViVi);
        return nextShipmentViViMapper.toDto(nextShipmentViVi);
    }

    /**
     * Update a nextShipmentViVi.
     *
     * @param nextShipmentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViViDTO update(NextShipmentViViDTO nextShipmentViViDTO) {
        LOG.debug("Request to update NextShipmentViVi : {}", nextShipmentViViDTO);
        NextShipmentViVi nextShipmentViVi = nextShipmentViViMapper.toEntity(nextShipmentViViDTO);
        nextShipmentViVi = nextShipmentViViRepository.save(nextShipmentViVi);
        return nextShipmentViViMapper.toDto(nextShipmentViVi);
    }

    /**
     * Partially update a nextShipmentViVi.
     *
     * @param nextShipmentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentViViDTO> partialUpdate(NextShipmentViViDTO nextShipmentViViDTO) {
        LOG.debug("Request to partially update NextShipmentViVi : {}", nextShipmentViViDTO);

        return nextShipmentViViRepository
            .findById(nextShipmentViViDTO.getId())
            .map(existingNextShipmentViVi -> {
                nextShipmentViViMapper.partialUpdate(existingNextShipmentViVi, nextShipmentViViDTO);

                return existingNextShipmentViVi;
            })
            .map(nextShipmentViViRepository::save)
            .map(nextShipmentViViMapper::toDto);
    }

    /**
     * Get one nextShipmentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentViVi : {}", id);
        return nextShipmentViViRepository.findById(id).map(nextShipmentViViMapper::toDto);
    }

    /**
     * Delete the nextShipmentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentViVi : {}", id);
        nextShipmentViViRepository.deleteById(id);
    }
}
