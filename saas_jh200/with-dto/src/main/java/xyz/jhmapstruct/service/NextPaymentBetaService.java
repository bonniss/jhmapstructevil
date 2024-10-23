package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.repository.NextPaymentBetaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentBetaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentBeta}.
 */
@Service
@Transactional
public class NextPaymentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentBetaService.class);

    private final NextPaymentBetaRepository nextPaymentBetaRepository;

    private final NextPaymentBetaMapper nextPaymentBetaMapper;

    public NextPaymentBetaService(NextPaymentBetaRepository nextPaymentBetaRepository, NextPaymentBetaMapper nextPaymentBetaMapper) {
        this.nextPaymentBetaRepository = nextPaymentBetaRepository;
        this.nextPaymentBetaMapper = nextPaymentBetaMapper;
    }

    /**
     * Save a nextPaymentBeta.
     *
     * @param nextPaymentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentBetaDTO save(NextPaymentBetaDTO nextPaymentBetaDTO) {
        LOG.debug("Request to save NextPaymentBeta : {}", nextPaymentBetaDTO);
        NextPaymentBeta nextPaymentBeta = nextPaymentBetaMapper.toEntity(nextPaymentBetaDTO);
        nextPaymentBeta = nextPaymentBetaRepository.save(nextPaymentBeta);
        return nextPaymentBetaMapper.toDto(nextPaymentBeta);
    }

    /**
     * Update a nextPaymentBeta.
     *
     * @param nextPaymentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentBetaDTO update(NextPaymentBetaDTO nextPaymentBetaDTO) {
        LOG.debug("Request to update NextPaymentBeta : {}", nextPaymentBetaDTO);
        NextPaymentBeta nextPaymentBeta = nextPaymentBetaMapper.toEntity(nextPaymentBetaDTO);
        nextPaymentBeta = nextPaymentBetaRepository.save(nextPaymentBeta);
        return nextPaymentBetaMapper.toDto(nextPaymentBeta);
    }

    /**
     * Partially update a nextPaymentBeta.
     *
     * @param nextPaymentBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentBetaDTO> partialUpdate(NextPaymentBetaDTO nextPaymentBetaDTO) {
        LOG.debug("Request to partially update NextPaymentBeta : {}", nextPaymentBetaDTO);

        return nextPaymentBetaRepository
            .findById(nextPaymentBetaDTO.getId())
            .map(existingNextPaymentBeta -> {
                nextPaymentBetaMapper.partialUpdate(existingNextPaymentBeta, nextPaymentBetaDTO);

                return existingNextPaymentBeta;
            })
            .map(nextPaymentBetaRepository::save)
            .map(nextPaymentBetaMapper::toDto);
    }

    /**
     * Get one nextPaymentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentBeta : {}", id);
        return nextPaymentBetaRepository.findById(id).map(nextPaymentBetaMapper::toDto);
    }

    /**
     * Delete the nextPaymentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentBeta : {}", id);
        nextPaymentBetaRepository.deleteById(id);
    }
}
