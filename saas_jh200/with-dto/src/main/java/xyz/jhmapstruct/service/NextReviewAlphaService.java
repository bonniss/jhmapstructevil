package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewAlpha;
import xyz.jhmapstruct.repository.NextReviewAlphaRepository;
import xyz.jhmapstruct.service.dto.NextReviewAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewAlpha}.
 */
@Service
@Transactional
public class NextReviewAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewAlphaService.class);

    private final NextReviewAlphaRepository nextReviewAlphaRepository;

    private final NextReviewAlphaMapper nextReviewAlphaMapper;

    public NextReviewAlphaService(NextReviewAlphaRepository nextReviewAlphaRepository, NextReviewAlphaMapper nextReviewAlphaMapper) {
        this.nextReviewAlphaRepository = nextReviewAlphaRepository;
        this.nextReviewAlphaMapper = nextReviewAlphaMapper;
    }

    /**
     * Save a nextReviewAlpha.
     *
     * @param nextReviewAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewAlphaDTO save(NextReviewAlphaDTO nextReviewAlphaDTO) {
        LOG.debug("Request to save NextReviewAlpha : {}", nextReviewAlphaDTO);
        NextReviewAlpha nextReviewAlpha = nextReviewAlphaMapper.toEntity(nextReviewAlphaDTO);
        nextReviewAlpha = nextReviewAlphaRepository.save(nextReviewAlpha);
        return nextReviewAlphaMapper.toDto(nextReviewAlpha);
    }

    /**
     * Update a nextReviewAlpha.
     *
     * @param nextReviewAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewAlphaDTO update(NextReviewAlphaDTO nextReviewAlphaDTO) {
        LOG.debug("Request to update NextReviewAlpha : {}", nextReviewAlphaDTO);
        NextReviewAlpha nextReviewAlpha = nextReviewAlphaMapper.toEntity(nextReviewAlphaDTO);
        nextReviewAlpha = nextReviewAlphaRepository.save(nextReviewAlpha);
        return nextReviewAlphaMapper.toDto(nextReviewAlpha);
    }

    /**
     * Partially update a nextReviewAlpha.
     *
     * @param nextReviewAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewAlphaDTO> partialUpdate(NextReviewAlphaDTO nextReviewAlphaDTO) {
        LOG.debug("Request to partially update NextReviewAlpha : {}", nextReviewAlphaDTO);

        return nextReviewAlphaRepository
            .findById(nextReviewAlphaDTO.getId())
            .map(existingNextReviewAlpha -> {
                nextReviewAlphaMapper.partialUpdate(existingNextReviewAlpha, nextReviewAlphaDTO);

                return existingNextReviewAlpha;
            })
            .map(nextReviewAlphaRepository::save)
            .map(nextReviewAlphaMapper::toDto);
    }

    /**
     * Get all the nextReviewAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewAlphaRepository.findAllWithEagerRelationships(pageable).map(nextReviewAlphaMapper::toDto);
    }

    /**
     * Get one nextReviewAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewAlpha : {}", id);
        return nextReviewAlphaRepository.findOneWithEagerRelationships(id).map(nextReviewAlphaMapper::toDto);
    }

    /**
     * Delete the nextReviewAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewAlpha : {}", id);
        nextReviewAlphaRepository.deleteById(id);
    }
}
