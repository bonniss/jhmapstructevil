package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.repository.NextReviewGammaRepository;
import xyz.jhmapstruct.service.dto.NextReviewGammaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewGamma}.
 */
@Service
@Transactional
public class NextReviewGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewGammaService.class);

    private final NextReviewGammaRepository nextReviewGammaRepository;

    private final NextReviewGammaMapper nextReviewGammaMapper;

    public NextReviewGammaService(NextReviewGammaRepository nextReviewGammaRepository, NextReviewGammaMapper nextReviewGammaMapper) {
        this.nextReviewGammaRepository = nextReviewGammaRepository;
        this.nextReviewGammaMapper = nextReviewGammaMapper;
    }

    /**
     * Save a nextReviewGamma.
     *
     * @param nextReviewGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewGammaDTO save(NextReviewGammaDTO nextReviewGammaDTO) {
        LOG.debug("Request to save NextReviewGamma : {}", nextReviewGammaDTO);
        NextReviewGamma nextReviewGamma = nextReviewGammaMapper.toEntity(nextReviewGammaDTO);
        nextReviewGamma = nextReviewGammaRepository.save(nextReviewGamma);
        return nextReviewGammaMapper.toDto(nextReviewGamma);
    }

    /**
     * Update a nextReviewGamma.
     *
     * @param nextReviewGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewGammaDTO update(NextReviewGammaDTO nextReviewGammaDTO) {
        LOG.debug("Request to update NextReviewGamma : {}", nextReviewGammaDTO);
        NextReviewGamma nextReviewGamma = nextReviewGammaMapper.toEntity(nextReviewGammaDTO);
        nextReviewGamma = nextReviewGammaRepository.save(nextReviewGamma);
        return nextReviewGammaMapper.toDto(nextReviewGamma);
    }

    /**
     * Partially update a nextReviewGamma.
     *
     * @param nextReviewGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewGammaDTO> partialUpdate(NextReviewGammaDTO nextReviewGammaDTO) {
        LOG.debug("Request to partially update NextReviewGamma : {}", nextReviewGammaDTO);

        return nextReviewGammaRepository
            .findById(nextReviewGammaDTO.getId())
            .map(existingNextReviewGamma -> {
                nextReviewGammaMapper.partialUpdate(existingNextReviewGamma, nextReviewGammaDTO);

                return existingNextReviewGamma;
            })
            .map(nextReviewGammaRepository::save)
            .map(nextReviewGammaMapper::toDto);
    }

    /**
     * Get all the nextReviewGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewGammaRepository.findAllWithEagerRelationships(pageable).map(nextReviewGammaMapper::toDto);
    }

    /**
     * Get one nextReviewGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewGamma : {}", id);
        return nextReviewGammaRepository.findOneWithEagerRelationships(id).map(nextReviewGammaMapper::toDto);
    }

    /**
     * Delete the nextReviewGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewGamma : {}", id);
        nextReviewGammaRepository.deleteById(id);
    }
}
