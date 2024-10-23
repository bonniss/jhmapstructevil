package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.repository.NextReviewBetaRepository;
import xyz.jhmapstruct.service.dto.NextReviewBetaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewBeta}.
 */
@Service
@Transactional
public class NextReviewBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewBetaService.class);

    private final NextReviewBetaRepository nextReviewBetaRepository;

    private final NextReviewBetaMapper nextReviewBetaMapper;

    public NextReviewBetaService(NextReviewBetaRepository nextReviewBetaRepository, NextReviewBetaMapper nextReviewBetaMapper) {
        this.nextReviewBetaRepository = nextReviewBetaRepository;
        this.nextReviewBetaMapper = nextReviewBetaMapper;
    }

    /**
     * Save a nextReviewBeta.
     *
     * @param nextReviewBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewBetaDTO save(NextReviewBetaDTO nextReviewBetaDTO) {
        LOG.debug("Request to save NextReviewBeta : {}", nextReviewBetaDTO);
        NextReviewBeta nextReviewBeta = nextReviewBetaMapper.toEntity(nextReviewBetaDTO);
        nextReviewBeta = nextReviewBetaRepository.save(nextReviewBeta);
        return nextReviewBetaMapper.toDto(nextReviewBeta);
    }

    /**
     * Update a nextReviewBeta.
     *
     * @param nextReviewBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewBetaDTO update(NextReviewBetaDTO nextReviewBetaDTO) {
        LOG.debug("Request to update NextReviewBeta : {}", nextReviewBetaDTO);
        NextReviewBeta nextReviewBeta = nextReviewBetaMapper.toEntity(nextReviewBetaDTO);
        nextReviewBeta = nextReviewBetaRepository.save(nextReviewBeta);
        return nextReviewBetaMapper.toDto(nextReviewBeta);
    }

    /**
     * Partially update a nextReviewBeta.
     *
     * @param nextReviewBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewBetaDTO> partialUpdate(NextReviewBetaDTO nextReviewBetaDTO) {
        LOG.debug("Request to partially update NextReviewBeta : {}", nextReviewBetaDTO);

        return nextReviewBetaRepository
            .findById(nextReviewBetaDTO.getId())
            .map(existingNextReviewBeta -> {
                nextReviewBetaMapper.partialUpdate(existingNextReviewBeta, nextReviewBetaDTO);

                return existingNextReviewBeta;
            })
            .map(nextReviewBetaRepository::save)
            .map(nextReviewBetaMapper::toDto);
    }

    /**
     * Get all the nextReviewBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewBetaRepository.findAllWithEagerRelationships(pageable).map(nextReviewBetaMapper::toDto);
    }

    /**
     * Get one nextReviewBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewBeta : {}", id);
        return nextReviewBetaRepository.findOneWithEagerRelationships(id).map(nextReviewBetaMapper::toDto);
    }

    /**
     * Delete the nextReviewBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewBeta : {}", id);
        nextReviewBetaRepository.deleteById(id);
    }
}
