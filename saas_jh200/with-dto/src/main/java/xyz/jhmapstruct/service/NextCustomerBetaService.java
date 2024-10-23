package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.repository.NextCustomerBetaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerBetaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerBeta}.
 */
@Service
@Transactional
public class NextCustomerBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerBetaService.class);

    private final NextCustomerBetaRepository nextCustomerBetaRepository;

    private final NextCustomerBetaMapper nextCustomerBetaMapper;

    public NextCustomerBetaService(NextCustomerBetaRepository nextCustomerBetaRepository, NextCustomerBetaMapper nextCustomerBetaMapper) {
        this.nextCustomerBetaRepository = nextCustomerBetaRepository;
        this.nextCustomerBetaMapper = nextCustomerBetaMapper;
    }

    /**
     * Save a nextCustomerBeta.
     *
     * @param nextCustomerBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerBetaDTO save(NextCustomerBetaDTO nextCustomerBetaDTO) {
        LOG.debug("Request to save NextCustomerBeta : {}", nextCustomerBetaDTO);
        NextCustomerBeta nextCustomerBeta = nextCustomerBetaMapper.toEntity(nextCustomerBetaDTO);
        nextCustomerBeta = nextCustomerBetaRepository.save(nextCustomerBeta);
        return nextCustomerBetaMapper.toDto(nextCustomerBeta);
    }

    /**
     * Update a nextCustomerBeta.
     *
     * @param nextCustomerBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerBetaDTO update(NextCustomerBetaDTO nextCustomerBetaDTO) {
        LOG.debug("Request to update NextCustomerBeta : {}", nextCustomerBetaDTO);
        NextCustomerBeta nextCustomerBeta = nextCustomerBetaMapper.toEntity(nextCustomerBetaDTO);
        nextCustomerBeta = nextCustomerBetaRepository.save(nextCustomerBeta);
        return nextCustomerBetaMapper.toDto(nextCustomerBeta);
    }

    /**
     * Partially update a nextCustomerBeta.
     *
     * @param nextCustomerBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerBetaDTO> partialUpdate(NextCustomerBetaDTO nextCustomerBetaDTO) {
        LOG.debug("Request to partially update NextCustomerBeta : {}", nextCustomerBetaDTO);

        return nextCustomerBetaRepository
            .findById(nextCustomerBetaDTO.getId())
            .map(existingNextCustomerBeta -> {
                nextCustomerBetaMapper.partialUpdate(existingNextCustomerBeta, nextCustomerBetaDTO);

                return existingNextCustomerBeta;
            })
            .map(nextCustomerBetaRepository::save)
            .map(nextCustomerBetaMapper::toDto);
    }

    /**
     * Get one nextCustomerBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerBeta : {}", id);
        return nextCustomerBetaRepository.findById(id).map(nextCustomerBetaMapper::toDto);
    }

    /**
     * Delete the nextCustomerBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerBeta : {}", id);
        nextCustomerBetaRepository.deleteById(id);
    }
}
