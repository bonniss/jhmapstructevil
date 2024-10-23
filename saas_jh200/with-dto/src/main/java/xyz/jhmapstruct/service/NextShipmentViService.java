package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.repository.NextShipmentViRepository;
import xyz.jhmapstruct.service.dto.NextShipmentViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentVi}.
 */
@Service
@Transactional
public class NextShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViService.class);

    private final NextShipmentViRepository nextShipmentViRepository;

    private final NextShipmentViMapper nextShipmentViMapper;

    public NextShipmentViService(NextShipmentViRepository nextShipmentViRepository, NextShipmentViMapper nextShipmentViMapper) {
        this.nextShipmentViRepository = nextShipmentViRepository;
        this.nextShipmentViMapper = nextShipmentViMapper;
    }

    /**
     * Save a nextShipmentVi.
     *
     * @param nextShipmentViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViDTO save(NextShipmentViDTO nextShipmentViDTO) {
        LOG.debug("Request to save NextShipmentVi : {}", nextShipmentViDTO);
        NextShipmentVi nextShipmentVi = nextShipmentViMapper.toEntity(nextShipmentViDTO);
        nextShipmentVi = nextShipmentViRepository.save(nextShipmentVi);
        return nextShipmentViMapper.toDto(nextShipmentVi);
    }

    /**
     * Update a nextShipmentVi.
     *
     * @param nextShipmentViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViDTO update(NextShipmentViDTO nextShipmentViDTO) {
        LOG.debug("Request to update NextShipmentVi : {}", nextShipmentViDTO);
        NextShipmentVi nextShipmentVi = nextShipmentViMapper.toEntity(nextShipmentViDTO);
        nextShipmentVi = nextShipmentViRepository.save(nextShipmentVi);
        return nextShipmentViMapper.toDto(nextShipmentVi);
    }

    /**
     * Partially update a nextShipmentVi.
     *
     * @param nextShipmentViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentViDTO> partialUpdate(NextShipmentViDTO nextShipmentViDTO) {
        LOG.debug("Request to partially update NextShipmentVi : {}", nextShipmentViDTO);

        return nextShipmentViRepository
            .findById(nextShipmentViDTO.getId())
            .map(existingNextShipmentVi -> {
                nextShipmentViMapper.partialUpdate(existingNextShipmentVi, nextShipmentViDTO);

                return existingNextShipmentVi;
            })
            .map(nextShipmentViRepository::save)
            .map(nextShipmentViMapper::toDto);
    }

    /**
     * Get one nextShipmentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentViDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentVi : {}", id);
        return nextShipmentViRepository.findById(id).map(nextShipmentViMapper::toDto);
    }

    /**
     * Delete the nextShipmentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentVi : {}", id);
        nextShipmentViRepository.deleteById(id);
    }
}
