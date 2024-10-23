package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.repository.NextOrderBetaRepository;
import xyz.jhmapstruct.service.dto.NextOrderBetaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderBeta}.
 */
@Service
@Transactional
public class NextOrderBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderBetaService.class);

    private final NextOrderBetaRepository nextOrderBetaRepository;

    private final NextOrderBetaMapper nextOrderBetaMapper;

    public NextOrderBetaService(NextOrderBetaRepository nextOrderBetaRepository, NextOrderBetaMapper nextOrderBetaMapper) {
        this.nextOrderBetaRepository = nextOrderBetaRepository;
        this.nextOrderBetaMapper = nextOrderBetaMapper;
    }

    /**
     * Save a nextOrderBeta.
     *
     * @param nextOrderBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderBetaDTO save(NextOrderBetaDTO nextOrderBetaDTO) {
        LOG.debug("Request to save NextOrderBeta : {}", nextOrderBetaDTO);
        NextOrderBeta nextOrderBeta = nextOrderBetaMapper.toEntity(nextOrderBetaDTO);
        nextOrderBeta = nextOrderBetaRepository.save(nextOrderBeta);
        return nextOrderBetaMapper.toDto(nextOrderBeta);
    }

    /**
     * Update a nextOrderBeta.
     *
     * @param nextOrderBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderBetaDTO update(NextOrderBetaDTO nextOrderBetaDTO) {
        LOG.debug("Request to update NextOrderBeta : {}", nextOrderBetaDTO);
        NextOrderBeta nextOrderBeta = nextOrderBetaMapper.toEntity(nextOrderBetaDTO);
        nextOrderBeta = nextOrderBetaRepository.save(nextOrderBeta);
        return nextOrderBetaMapper.toDto(nextOrderBeta);
    }

    /**
     * Partially update a nextOrderBeta.
     *
     * @param nextOrderBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderBetaDTO> partialUpdate(NextOrderBetaDTO nextOrderBetaDTO) {
        LOG.debug("Request to partially update NextOrderBeta : {}", nextOrderBetaDTO);

        return nextOrderBetaRepository
            .findById(nextOrderBetaDTO.getId())
            .map(existingNextOrderBeta -> {
                nextOrderBetaMapper.partialUpdate(existingNextOrderBeta, nextOrderBetaDTO);

                return existingNextOrderBeta;
            })
            .map(nextOrderBetaRepository::save)
            .map(nextOrderBetaMapper::toDto);
    }

    /**
     * Get all the nextOrderBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderBetaRepository.findAllWithEagerRelationships(pageable).map(nextOrderBetaMapper::toDto);
    }

    /**
     * Get one nextOrderBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderBeta : {}", id);
        return nextOrderBetaRepository.findOneWithEagerRelationships(id).map(nextOrderBetaMapper::toDto);
    }

    /**
     * Delete the nextOrderBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderBeta : {}", id);
        nextOrderBetaRepository.deleteById(id);
    }
}
