package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.repository.NextProductAlphaRepository;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextProductAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductAlpha}.
 */
@Service
@Transactional
public class NextProductAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductAlphaService.class);

    private final NextProductAlphaRepository nextProductAlphaRepository;

    private final NextProductAlphaMapper nextProductAlphaMapper;

    public NextProductAlphaService(NextProductAlphaRepository nextProductAlphaRepository, NextProductAlphaMapper nextProductAlphaMapper) {
        this.nextProductAlphaRepository = nextProductAlphaRepository;
        this.nextProductAlphaMapper = nextProductAlphaMapper;
    }

    /**
     * Save a nextProductAlpha.
     *
     * @param nextProductAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductAlphaDTO save(NextProductAlphaDTO nextProductAlphaDTO) {
        LOG.debug("Request to save NextProductAlpha : {}", nextProductAlphaDTO);
        NextProductAlpha nextProductAlpha = nextProductAlphaMapper.toEntity(nextProductAlphaDTO);
        nextProductAlpha = nextProductAlphaRepository.save(nextProductAlpha);
        return nextProductAlphaMapper.toDto(nextProductAlpha);
    }

    /**
     * Update a nextProductAlpha.
     *
     * @param nextProductAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductAlphaDTO update(NextProductAlphaDTO nextProductAlphaDTO) {
        LOG.debug("Request to update NextProductAlpha : {}", nextProductAlphaDTO);
        NextProductAlpha nextProductAlpha = nextProductAlphaMapper.toEntity(nextProductAlphaDTO);
        nextProductAlpha = nextProductAlphaRepository.save(nextProductAlpha);
        return nextProductAlphaMapper.toDto(nextProductAlpha);
    }

    /**
     * Partially update a nextProductAlpha.
     *
     * @param nextProductAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductAlphaDTO> partialUpdate(NextProductAlphaDTO nextProductAlphaDTO) {
        LOG.debug("Request to partially update NextProductAlpha : {}", nextProductAlphaDTO);

        return nextProductAlphaRepository
            .findById(nextProductAlphaDTO.getId())
            .map(existingNextProductAlpha -> {
                nextProductAlphaMapper.partialUpdate(existingNextProductAlpha, nextProductAlphaDTO);

                return existingNextProductAlpha;
            })
            .map(nextProductAlphaRepository::save)
            .map(nextProductAlphaMapper::toDto);
    }

    /**
     * Get all the nextProductAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductAlphaRepository.findAllWithEagerRelationships(pageable).map(nextProductAlphaMapper::toDto);
    }

    /**
     * Get one nextProductAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductAlpha : {}", id);
        return nextProductAlphaRepository.findOneWithEagerRelationships(id).map(nextProductAlphaMapper::toDto);
    }

    /**
     * Delete the nextProductAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductAlpha : {}", id);
        nextProductAlphaRepository.deleteById(id);
    }
}
