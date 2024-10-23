package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.repository.NextCustomerThetaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerThetaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerTheta}.
 */
@Service
@Transactional
public class NextCustomerThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerThetaService.class);

    private final NextCustomerThetaRepository nextCustomerThetaRepository;

    private final NextCustomerThetaMapper nextCustomerThetaMapper;

    public NextCustomerThetaService(
        NextCustomerThetaRepository nextCustomerThetaRepository,
        NextCustomerThetaMapper nextCustomerThetaMapper
    ) {
        this.nextCustomerThetaRepository = nextCustomerThetaRepository;
        this.nextCustomerThetaMapper = nextCustomerThetaMapper;
    }

    /**
     * Save a nextCustomerTheta.
     *
     * @param nextCustomerThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerThetaDTO save(NextCustomerThetaDTO nextCustomerThetaDTO) {
        LOG.debug("Request to save NextCustomerTheta : {}", nextCustomerThetaDTO);
        NextCustomerTheta nextCustomerTheta = nextCustomerThetaMapper.toEntity(nextCustomerThetaDTO);
        nextCustomerTheta = nextCustomerThetaRepository.save(nextCustomerTheta);
        return nextCustomerThetaMapper.toDto(nextCustomerTheta);
    }

    /**
     * Update a nextCustomerTheta.
     *
     * @param nextCustomerThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerThetaDTO update(NextCustomerThetaDTO nextCustomerThetaDTO) {
        LOG.debug("Request to update NextCustomerTheta : {}", nextCustomerThetaDTO);
        NextCustomerTheta nextCustomerTheta = nextCustomerThetaMapper.toEntity(nextCustomerThetaDTO);
        nextCustomerTheta = nextCustomerThetaRepository.save(nextCustomerTheta);
        return nextCustomerThetaMapper.toDto(nextCustomerTheta);
    }

    /**
     * Partially update a nextCustomerTheta.
     *
     * @param nextCustomerThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerThetaDTO> partialUpdate(NextCustomerThetaDTO nextCustomerThetaDTO) {
        LOG.debug("Request to partially update NextCustomerTheta : {}", nextCustomerThetaDTO);

        return nextCustomerThetaRepository
            .findById(nextCustomerThetaDTO.getId())
            .map(existingNextCustomerTheta -> {
                nextCustomerThetaMapper.partialUpdate(existingNextCustomerTheta, nextCustomerThetaDTO);

                return existingNextCustomerTheta;
            })
            .map(nextCustomerThetaRepository::save)
            .map(nextCustomerThetaMapper::toDto);
    }

    /**
     * Get one nextCustomerTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerTheta : {}", id);
        return nextCustomerThetaRepository.findById(id).map(nextCustomerThetaMapper::toDto);
    }

    /**
     * Delete the nextCustomerTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerTheta : {}", id);
        nextCustomerThetaRepository.deleteById(id);
    }
}
