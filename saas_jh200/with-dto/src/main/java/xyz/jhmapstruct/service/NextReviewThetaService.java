package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.repository.NextReviewThetaRepository;
import xyz.jhmapstruct.service.dto.NextReviewThetaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewTheta}.
 */
@Service
@Transactional
public class NextReviewThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewThetaService.class);

    private final NextReviewThetaRepository nextReviewThetaRepository;

    private final NextReviewThetaMapper nextReviewThetaMapper;

    public NextReviewThetaService(NextReviewThetaRepository nextReviewThetaRepository, NextReviewThetaMapper nextReviewThetaMapper) {
        this.nextReviewThetaRepository = nextReviewThetaRepository;
        this.nextReviewThetaMapper = nextReviewThetaMapper;
    }

    /**
     * Save a nextReviewTheta.
     *
     * @param nextReviewThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewThetaDTO save(NextReviewThetaDTO nextReviewThetaDTO) {
        LOG.debug("Request to save NextReviewTheta : {}", nextReviewThetaDTO);
        NextReviewTheta nextReviewTheta = nextReviewThetaMapper.toEntity(nextReviewThetaDTO);
        nextReviewTheta = nextReviewThetaRepository.save(nextReviewTheta);
        return nextReviewThetaMapper.toDto(nextReviewTheta);
    }

    /**
     * Update a nextReviewTheta.
     *
     * @param nextReviewThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewThetaDTO update(NextReviewThetaDTO nextReviewThetaDTO) {
        LOG.debug("Request to update NextReviewTheta : {}", nextReviewThetaDTO);
        NextReviewTheta nextReviewTheta = nextReviewThetaMapper.toEntity(nextReviewThetaDTO);
        nextReviewTheta = nextReviewThetaRepository.save(nextReviewTheta);
        return nextReviewThetaMapper.toDto(nextReviewTheta);
    }

    /**
     * Partially update a nextReviewTheta.
     *
     * @param nextReviewThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewThetaDTO> partialUpdate(NextReviewThetaDTO nextReviewThetaDTO) {
        LOG.debug("Request to partially update NextReviewTheta : {}", nextReviewThetaDTO);

        return nextReviewThetaRepository
            .findById(nextReviewThetaDTO.getId())
            .map(existingNextReviewTheta -> {
                nextReviewThetaMapper.partialUpdate(existingNextReviewTheta, nextReviewThetaDTO);

                return existingNextReviewTheta;
            })
            .map(nextReviewThetaRepository::save)
            .map(nextReviewThetaMapper::toDto);
    }

    /**
     * Get all the nextReviewThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewThetaRepository.findAllWithEagerRelationships(pageable).map(nextReviewThetaMapper::toDto);
    }

    /**
     * Get one nextReviewTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewTheta : {}", id);
        return nextReviewThetaRepository.findOneWithEagerRelationships(id).map(nextReviewThetaMapper::toDto);
    }

    /**
     * Delete the nextReviewTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewTheta : {}", id);
        nextReviewThetaRepository.deleteById(id);
    }
}
