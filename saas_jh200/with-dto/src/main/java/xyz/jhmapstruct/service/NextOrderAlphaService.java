package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.repository.NextOrderAlphaRepository;
import xyz.jhmapstruct.service.dto.NextOrderAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderAlpha}.
 */
@Service
@Transactional
public class NextOrderAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderAlphaService.class);

    private final NextOrderAlphaRepository nextOrderAlphaRepository;

    private final NextOrderAlphaMapper nextOrderAlphaMapper;

    public NextOrderAlphaService(NextOrderAlphaRepository nextOrderAlphaRepository, NextOrderAlphaMapper nextOrderAlphaMapper) {
        this.nextOrderAlphaRepository = nextOrderAlphaRepository;
        this.nextOrderAlphaMapper = nextOrderAlphaMapper;
    }

    /**
     * Save a nextOrderAlpha.
     *
     * @param nextOrderAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderAlphaDTO save(NextOrderAlphaDTO nextOrderAlphaDTO) {
        LOG.debug("Request to save NextOrderAlpha : {}", nextOrderAlphaDTO);
        NextOrderAlpha nextOrderAlpha = nextOrderAlphaMapper.toEntity(nextOrderAlphaDTO);
        nextOrderAlpha = nextOrderAlphaRepository.save(nextOrderAlpha);
        return nextOrderAlphaMapper.toDto(nextOrderAlpha);
    }

    /**
     * Update a nextOrderAlpha.
     *
     * @param nextOrderAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderAlphaDTO update(NextOrderAlphaDTO nextOrderAlphaDTO) {
        LOG.debug("Request to update NextOrderAlpha : {}", nextOrderAlphaDTO);
        NextOrderAlpha nextOrderAlpha = nextOrderAlphaMapper.toEntity(nextOrderAlphaDTO);
        nextOrderAlpha = nextOrderAlphaRepository.save(nextOrderAlpha);
        return nextOrderAlphaMapper.toDto(nextOrderAlpha);
    }

    /**
     * Partially update a nextOrderAlpha.
     *
     * @param nextOrderAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderAlphaDTO> partialUpdate(NextOrderAlphaDTO nextOrderAlphaDTO) {
        LOG.debug("Request to partially update NextOrderAlpha : {}", nextOrderAlphaDTO);

        return nextOrderAlphaRepository
            .findById(nextOrderAlphaDTO.getId())
            .map(existingNextOrderAlpha -> {
                nextOrderAlphaMapper.partialUpdate(existingNextOrderAlpha, nextOrderAlphaDTO);

                return existingNextOrderAlpha;
            })
            .map(nextOrderAlphaRepository::save)
            .map(nextOrderAlphaMapper::toDto);
    }

    /**
     * Get all the nextOrderAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderAlphaRepository.findAllWithEagerRelationships(pageable).map(nextOrderAlphaMapper::toDto);
    }

    /**
     * Get one nextOrderAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderAlpha : {}", id);
        return nextOrderAlphaRepository.findOneWithEagerRelationships(id).map(nextOrderAlphaMapper::toDto);
    }

    /**
     * Delete the nextOrderAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderAlpha : {}", id);
        nextOrderAlphaRepository.deleteById(id);
    }
}
