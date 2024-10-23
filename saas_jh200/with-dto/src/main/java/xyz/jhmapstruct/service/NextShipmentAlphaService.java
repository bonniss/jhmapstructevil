package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.repository.NextShipmentAlphaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentAlpha}.
 */
@Service
@Transactional
public class NextShipmentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentAlphaService.class);

    private final NextShipmentAlphaRepository nextShipmentAlphaRepository;

    private final NextShipmentAlphaMapper nextShipmentAlphaMapper;

    public NextShipmentAlphaService(
        NextShipmentAlphaRepository nextShipmentAlphaRepository,
        NextShipmentAlphaMapper nextShipmentAlphaMapper
    ) {
        this.nextShipmentAlphaRepository = nextShipmentAlphaRepository;
        this.nextShipmentAlphaMapper = nextShipmentAlphaMapper;
    }

    /**
     * Save a nextShipmentAlpha.
     *
     * @param nextShipmentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentAlphaDTO save(NextShipmentAlphaDTO nextShipmentAlphaDTO) {
        LOG.debug("Request to save NextShipmentAlpha : {}", nextShipmentAlphaDTO);
        NextShipmentAlpha nextShipmentAlpha = nextShipmentAlphaMapper.toEntity(nextShipmentAlphaDTO);
        nextShipmentAlpha = nextShipmentAlphaRepository.save(nextShipmentAlpha);
        return nextShipmentAlphaMapper.toDto(nextShipmentAlpha);
    }

    /**
     * Update a nextShipmentAlpha.
     *
     * @param nextShipmentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentAlphaDTO update(NextShipmentAlphaDTO nextShipmentAlphaDTO) {
        LOG.debug("Request to update NextShipmentAlpha : {}", nextShipmentAlphaDTO);
        NextShipmentAlpha nextShipmentAlpha = nextShipmentAlphaMapper.toEntity(nextShipmentAlphaDTO);
        nextShipmentAlpha = nextShipmentAlphaRepository.save(nextShipmentAlpha);
        return nextShipmentAlphaMapper.toDto(nextShipmentAlpha);
    }

    /**
     * Partially update a nextShipmentAlpha.
     *
     * @param nextShipmentAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentAlphaDTO> partialUpdate(NextShipmentAlphaDTO nextShipmentAlphaDTO) {
        LOG.debug("Request to partially update NextShipmentAlpha : {}", nextShipmentAlphaDTO);

        return nextShipmentAlphaRepository
            .findById(nextShipmentAlphaDTO.getId())
            .map(existingNextShipmentAlpha -> {
                nextShipmentAlphaMapper.partialUpdate(existingNextShipmentAlpha, nextShipmentAlphaDTO);

                return existingNextShipmentAlpha;
            })
            .map(nextShipmentAlphaRepository::save)
            .map(nextShipmentAlphaMapper::toDto);
    }

    /**
     * Get one nextShipmentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentAlpha : {}", id);
        return nextShipmentAlphaRepository.findById(id).map(nextShipmentAlphaMapper::toDto);
    }

    /**
     * Delete the nextShipmentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentAlpha : {}", id);
        nextShipmentAlphaRepository.deleteById(id);
    }
}
