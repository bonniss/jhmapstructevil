package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.repository.NextPaymentThetaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentThetaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentTheta}.
 */
@Service
@Transactional
public class NextPaymentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentThetaService.class);

    private final NextPaymentThetaRepository nextPaymentThetaRepository;

    private final NextPaymentThetaMapper nextPaymentThetaMapper;

    public NextPaymentThetaService(NextPaymentThetaRepository nextPaymentThetaRepository, NextPaymentThetaMapper nextPaymentThetaMapper) {
        this.nextPaymentThetaRepository = nextPaymentThetaRepository;
        this.nextPaymentThetaMapper = nextPaymentThetaMapper;
    }

    /**
     * Save a nextPaymentTheta.
     *
     * @param nextPaymentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentThetaDTO save(NextPaymentThetaDTO nextPaymentThetaDTO) {
        LOG.debug("Request to save NextPaymentTheta : {}", nextPaymentThetaDTO);
        NextPaymentTheta nextPaymentTheta = nextPaymentThetaMapper.toEntity(nextPaymentThetaDTO);
        nextPaymentTheta = nextPaymentThetaRepository.save(nextPaymentTheta);
        return nextPaymentThetaMapper.toDto(nextPaymentTheta);
    }

    /**
     * Update a nextPaymentTheta.
     *
     * @param nextPaymentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentThetaDTO update(NextPaymentThetaDTO nextPaymentThetaDTO) {
        LOG.debug("Request to update NextPaymentTheta : {}", nextPaymentThetaDTO);
        NextPaymentTheta nextPaymentTheta = nextPaymentThetaMapper.toEntity(nextPaymentThetaDTO);
        nextPaymentTheta = nextPaymentThetaRepository.save(nextPaymentTheta);
        return nextPaymentThetaMapper.toDto(nextPaymentTheta);
    }

    /**
     * Partially update a nextPaymentTheta.
     *
     * @param nextPaymentThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentThetaDTO> partialUpdate(NextPaymentThetaDTO nextPaymentThetaDTO) {
        LOG.debug("Request to partially update NextPaymentTheta : {}", nextPaymentThetaDTO);

        return nextPaymentThetaRepository
            .findById(nextPaymentThetaDTO.getId())
            .map(existingNextPaymentTheta -> {
                nextPaymentThetaMapper.partialUpdate(existingNextPaymentTheta, nextPaymentThetaDTO);

                return existingNextPaymentTheta;
            })
            .map(nextPaymentThetaRepository::save)
            .map(nextPaymentThetaMapper::toDto);
    }

    /**
     * Get one nextPaymentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentTheta : {}", id);
        return nextPaymentThetaRepository.findById(id).map(nextPaymentThetaMapper::toDto);
    }

    /**
     * Delete the nextPaymentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentTheta : {}", id);
        nextPaymentThetaRepository.deleteById(id);
    }
}
