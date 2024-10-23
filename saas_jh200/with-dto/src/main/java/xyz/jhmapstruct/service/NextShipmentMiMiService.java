package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;
import xyz.jhmapstruct.service.dto.NextShipmentMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentMiMi}.
 */
@Service
@Transactional
public class NextShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiMiService.class);

    private final NextShipmentMiMiRepository nextShipmentMiMiRepository;

    private final NextShipmentMiMiMapper nextShipmentMiMiMapper;

    public NextShipmentMiMiService(NextShipmentMiMiRepository nextShipmentMiMiRepository, NextShipmentMiMiMapper nextShipmentMiMiMapper) {
        this.nextShipmentMiMiRepository = nextShipmentMiMiRepository;
        this.nextShipmentMiMiMapper = nextShipmentMiMiMapper;
    }

    /**
     * Save a nextShipmentMiMi.
     *
     * @param nextShipmentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiMiDTO save(NextShipmentMiMiDTO nextShipmentMiMiDTO) {
        LOG.debug("Request to save NextShipmentMiMi : {}", nextShipmentMiMiDTO);
        NextShipmentMiMi nextShipmentMiMi = nextShipmentMiMiMapper.toEntity(nextShipmentMiMiDTO);
        nextShipmentMiMi = nextShipmentMiMiRepository.save(nextShipmentMiMi);
        return nextShipmentMiMiMapper.toDto(nextShipmentMiMi);
    }

    /**
     * Update a nextShipmentMiMi.
     *
     * @param nextShipmentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiMiDTO update(NextShipmentMiMiDTO nextShipmentMiMiDTO) {
        LOG.debug("Request to update NextShipmentMiMi : {}", nextShipmentMiMiDTO);
        NextShipmentMiMi nextShipmentMiMi = nextShipmentMiMiMapper.toEntity(nextShipmentMiMiDTO);
        nextShipmentMiMi = nextShipmentMiMiRepository.save(nextShipmentMiMi);
        return nextShipmentMiMiMapper.toDto(nextShipmentMiMi);
    }

    /**
     * Partially update a nextShipmentMiMi.
     *
     * @param nextShipmentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentMiMiDTO> partialUpdate(NextShipmentMiMiDTO nextShipmentMiMiDTO) {
        LOG.debug("Request to partially update NextShipmentMiMi : {}", nextShipmentMiMiDTO);

        return nextShipmentMiMiRepository
            .findById(nextShipmentMiMiDTO.getId())
            .map(existingNextShipmentMiMi -> {
                nextShipmentMiMiMapper.partialUpdate(existingNextShipmentMiMi, nextShipmentMiMiDTO);

                return existingNextShipmentMiMi;
            })
            .map(nextShipmentMiMiRepository::save)
            .map(nextShipmentMiMiMapper::toDto);
    }

    /**
     * Get one nextShipmentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentMiMi : {}", id);
        return nextShipmentMiMiRepository.findById(id).map(nextShipmentMiMiMapper::toDto);
    }

    /**
     * Delete the nextShipmentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentMiMi : {}", id);
        nextShipmentMiMiRepository.deleteById(id);
    }
}
