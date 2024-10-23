package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewSigma;
import xyz.jhmapstruct.repository.NextReviewSigmaRepository;
import xyz.jhmapstruct.service.dto.NextReviewSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewSigma}.
 */
@Service
@Transactional
public class NextReviewSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewSigmaService.class);

    private final NextReviewSigmaRepository nextReviewSigmaRepository;

    private final NextReviewSigmaMapper nextReviewSigmaMapper;

    public NextReviewSigmaService(NextReviewSigmaRepository nextReviewSigmaRepository, NextReviewSigmaMapper nextReviewSigmaMapper) {
        this.nextReviewSigmaRepository = nextReviewSigmaRepository;
        this.nextReviewSigmaMapper = nextReviewSigmaMapper;
    }

    /**
     * Save a nextReviewSigma.
     *
     * @param nextReviewSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewSigmaDTO save(NextReviewSigmaDTO nextReviewSigmaDTO) {
        LOG.debug("Request to save NextReviewSigma : {}", nextReviewSigmaDTO);
        NextReviewSigma nextReviewSigma = nextReviewSigmaMapper.toEntity(nextReviewSigmaDTO);
        nextReviewSigma = nextReviewSigmaRepository.save(nextReviewSigma);
        return nextReviewSigmaMapper.toDto(nextReviewSigma);
    }

    /**
     * Update a nextReviewSigma.
     *
     * @param nextReviewSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewSigmaDTO update(NextReviewSigmaDTO nextReviewSigmaDTO) {
        LOG.debug("Request to update NextReviewSigma : {}", nextReviewSigmaDTO);
        NextReviewSigma nextReviewSigma = nextReviewSigmaMapper.toEntity(nextReviewSigmaDTO);
        nextReviewSigma = nextReviewSigmaRepository.save(nextReviewSigma);
        return nextReviewSigmaMapper.toDto(nextReviewSigma);
    }

    /**
     * Partially update a nextReviewSigma.
     *
     * @param nextReviewSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewSigmaDTO> partialUpdate(NextReviewSigmaDTO nextReviewSigmaDTO) {
        LOG.debug("Request to partially update NextReviewSigma : {}", nextReviewSigmaDTO);

        return nextReviewSigmaRepository
            .findById(nextReviewSigmaDTO.getId())
            .map(existingNextReviewSigma -> {
                nextReviewSigmaMapper.partialUpdate(existingNextReviewSigma, nextReviewSigmaDTO);

                return existingNextReviewSigma;
            })
            .map(nextReviewSigmaRepository::save)
            .map(nextReviewSigmaMapper::toDto);
    }

    /**
     * Get all the nextReviewSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewSigmaRepository.findAllWithEagerRelationships(pageable).map(nextReviewSigmaMapper::toDto);
    }

    /**
     * Get one nextReviewSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewSigma : {}", id);
        return nextReviewSigmaRepository.findOneWithEagerRelationships(id).map(nextReviewSigmaMapper::toDto);
    }

    /**
     * Delete the nextReviewSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewSigma : {}", id);
        nextReviewSigmaRepository.deleteById(id);
    }
}
