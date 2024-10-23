package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.repository.NextShipmentMiRepository;
import xyz.jhmapstruct.service.dto.NextShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentMi}.
 */
@Service
@Transactional
public class NextShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiService.class);

    private final NextShipmentMiRepository nextShipmentMiRepository;

    private final NextShipmentMiMapper nextShipmentMiMapper;

    public NextShipmentMiService(NextShipmentMiRepository nextShipmentMiRepository, NextShipmentMiMapper nextShipmentMiMapper) {
        this.nextShipmentMiRepository = nextShipmentMiRepository;
        this.nextShipmentMiMapper = nextShipmentMiMapper;
    }

    /**
     * Save a nextShipmentMi.
     *
     * @param nextShipmentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiDTO save(NextShipmentMiDTO nextShipmentMiDTO) {
        LOG.debug("Request to save NextShipmentMi : {}", nextShipmentMiDTO);
        NextShipmentMi nextShipmentMi = nextShipmentMiMapper.toEntity(nextShipmentMiDTO);
        nextShipmentMi = nextShipmentMiRepository.save(nextShipmentMi);
        return nextShipmentMiMapper.toDto(nextShipmentMi);
    }

    /**
     * Update a nextShipmentMi.
     *
     * @param nextShipmentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiDTO update(NextShipmentMiDTO nextShipmentMiDTO) {
        LOG.debug("Request to update NextShipmentMi : {}", nextShipmentMiDTO);
        NextShipmentMi nextShipmentMi = nextShipmentMiMapper.toEntity(nextShipmentMiDTO);
        nextShipmentMi = nextShipmentMiRepository.save(nextShipmentMi);
        return nextShipmentMiMapper.toDto(nextShipmentMi);
    }

    /**
     * Partially update a nextShipmentMi.
     *
     * @param nextShipmentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentMiDTO> partialUpdate(NextShipmentMiDTO nextShipmentMiDTO) {
        LOG.debug("Request to partially update NextShipmentMi : {}", nextShipmentMiDTO);

        return nextShipmentMiRepository
            .findById(nextShipmentMiDTO.getId())
            .map(existingNextShipmentMi -> {
                nextShipmentMiMapper.partialUpdate(existingNextShipmentMi, nextShipmentMiDTO);

                return existingNextShipmentMi;
            })
            .map(nextShipmentMiRepository::save)
            .map(nextShipmentMiMapper::toDto);
    }

    /**
     * Get one nextShipmentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentMi : {}", id);
        return nextShipmentMiRepository.findById(id).map(nextShipmentMiMapper::toDto);
    }

    /**
     * Delete the nextShipmentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentMi : {}", id);
        nextShipmentMiRepository.deleteById(id);
    }
}
