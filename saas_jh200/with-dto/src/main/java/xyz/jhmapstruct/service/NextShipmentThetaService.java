package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.repository.NextShipmentThetaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentThetaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentTheta}.
 */
@Service
@Transactional
public class NextShipmentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentThetaService.class);

    private final NextShipmentThetaRepository nextShipmentThetaRepository;

    private final NextShipmentThetaMapper nextShipmentThetaMapper;

    public NextShipmentThetaService(
        NextShipmentThetaRepository nextShipmentThetaRepository,
        NextShipmentThetaMapper nextShipmentThetaMapper
    ) {
        this.nextShipmentThetaRepository = nextShipmentThetaRepository;
        this.nextShipmentThetaMapper = nextShipmentThetaMapper;
    }

    /**
     * Save a nextShipmentTheta.
     *
     * @param nextShipmentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentThetaDTO save(NextShipmentThetaDTO nextShipmentThetaDTO) {
        LOG.debug("Request to save NextShipmentTheta : {}", nextShipmentThetaDTO);
        NextShipmentTheta nextShipmentTheta = nextShipmentThetaMapper.toEntity(nextShipmentThetaDTO);
        nextShipmentTheta = nextShipmentThetaRepository.save(nextShipmentTheta);
        return nextShipmentThetaMapper.toDto(nextShipmentTheta);
    }

    /**
     * Update a nextShipmentTheta.
     *
     * @param nextShipmentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentThetaDTO update(NextShipmentThetaDTO nextShipmentThetaDTO) {
        LOG.debug("Request to update NextShipmentTheta : {}", nextShipmentThetaDTO);
        NextShipmentTheta nextShipmentTheta = nextShipmentThetaMapper.toEntity(nextShipmentThetaDTO);
        nextShipmentTheta = nextShipmentThetaRepository.save(nextShipmentTheta);
        return nextShipmentThetaMapper.toDto(nextShipmentTheta);
    }

    /**
     * Partially update a nextShipmentTheta.
     *
     * @param nextShipmentThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentThetaDTO> partialUpdate(NextShipmentThetaDTO nextShipmentThetaDTO) {
        LOG.debug("Request to partially update NextShipmentTheta : {}", nextShipmentThetaDTO);

        return nextShipmentThetaRepository
            .findById(nextShipmentThetaDTO.getId())
            .map(existingNextShipmentTheta -> {
                nextShipmentThetaMapper.partialUpdate(existingNextShipmentTheta, nextShipmentThetaDTO);

                return existingNextShipmentTheta;
            })
            .map(nextShipmentThetaRepository::save)
            .map(nextShipmentThetaMapper::toDto);
    }

    /**
     * Get one nextShipmentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentTheta : {}", id);
        return nextShipmentThetaRepository.findById(id).map(nextShipmentThetaMapper::toDto);
    }

    /**
     * Delete the nextShipmentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentTheta : {}", id);
        nextShipmentThetaRepository.deleteById(id);
    }
}
