package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.repository.NextShipmentBetaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentBeta}.
 */
@Service
@Transactional
public class NextShipmentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentBetaService.class);

    private final NextShipmentBetaRepository nextShipmentBetaRepository;

    private final NextShipmentBetaMapper nextShipmentBetaMapper;

    public NextShipmentBetaService(NextShipmentBetaRepository nextShipmentBetaRepository, NextShipmentBetaMapper nextShipmentBetaMapper) {
        this.nextShipmentBetaRepository = nextShipmentBetaRepository;
        this.nextShipmentBetaMapper = nextShipmentBetaMapper;
    }

    /**
     * Save a nextShipmentBeta.
     *
     * @param nextShipmentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentBetaDTO save(NextShipmentBetaDTO nextShipmentBetaDTO) {
        LOG.debug("Request to save NextShipmentBeta : {}", nextShipmentBetaDTO);
        NextShipmentBeta nextShipmentBeta = nextShipmentBetaMapper.toEntity(nextShipmentBetaDTO);
        nextShipmentBeta = nextShipmentBetaRepository.save(nextShipmentBeta);
        return nextShipmentBetaMapper.toDto(nextShipmentBeta);
    }

    /**
     * Update a nextShipmentBeta.
     *
     * @param nextShipmentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentBetaDTO update(NextShipmentBetaDTO nextShipmentBetaDTO) {
        LOG.debug("Request to update NextShipmentBeta : {}", nextShipmentBetaDTO);
        NextShipmentBeta nextShipmentBeta = nextShipmentBetaMapper.toEntity(nextShipmentBetaDTO);
        nextShipmentBeta = nextShipmentBetaRepository.save(nextShipmentBeta);
        return nextShipmentBetaMapper.toDto(nextShipmentBeta);
    }

    /**
     * Partially update a nextShipmentBeta.
     *
     * @param nextShipmentBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentBetaDTO> partialUpdate(NextShipmentBetaDTO nextShipmentBetaDTO) {
        LOG.debug("Request to partially update NextShipmentBeta : {}", nextShipmentBetaDTO);

        return nextShipmentBetaRepository
            .findById(nextShipmentBetaDTO.getId())
            .map(existingNextShipmentBeta -> {
                nextShipmentBetaMapper.partialUpdate(existingNextShipmentBeta, nextShipmentBetaDTO);

                return existingNextShipmentBeta;
            })
            .map(nextShipmentBetaRepository::save)
            .map(nextShipmentBetaMapper::toDto);
    }

    /**
     * Get one nextShipmentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentBeta : {}", id);
        return nextShipmentBetaRepository.findById(id).map(nextShipmentBetaMapper::toDto);
    }

    /**
     * Delete the nextShipmentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentBeta : {}", id);
        nextShipmentBetaRepository.deleteById(id);
    }
}
