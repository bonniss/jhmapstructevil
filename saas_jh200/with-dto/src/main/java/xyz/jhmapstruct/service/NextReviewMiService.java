package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.repository.NextReviewMiRepository;
import xyz.jhmapstruct.service.dto.NextReviewMiDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewMi}.
 */
@Service
@Transactional
public class NextReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiService.class);

    private final NextReviewMiRepository nextReviewMiRepository;

    private final NextReviewMiMapper nextReviewMiMapper;

    public NextReviewMiService(NextReviewMiRepository nextReviewMiRepository, NextReviewMiMapper nextReviewMiMapper) {
        this.nextReviewMiRepository = nextReviewMiRepository;
        this.nextReviewMiMapper = nextReviewMiMapper;
    }

    /**
     * Save a nextReviewMi.
     *
     * @param nextReviewMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiDTO save(NextReviewMiDTO nextReviewMiDTO) {
        LOG.debug("Request to save NextReviewMi : {}", nextReviewMiDTO);
        NextReviewMi nextReviewMi = nextReviewMiMapper.toEntity(nextReviewMiDTO);
        nextReviewMi = nextReviewMiRepository.save(nextReviewMi);
        return nextReviewMiMapper.toDto(nextReviewMi);
    }

    /**
     * Update a nextReviewMi.
     *
     * @param nextReviewMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiDTO update(NextReviewMiDTO nextReviewMiDTO) {
        LOG.debug("Request to update NextReviewMi : {}", nextReviewMiDTO);
        NextReviewMi nextReviewMi = nextReviewMiMapper.toEntity(nextReviewMiDTO);
        nextReviewMi = nextReviewMiRepository.save(nextReviewMi);
        return nextReviewMiMapper.toDto(nextReviewMi);
    }

    /**
     * Partially update a nextReviewMi.
     *
     * @param nextReviewMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewMiDTO> partialUpdate(NextReviewMiDTO nextReviewMiDTO) {
        LOG.debug("Request to partially update NextReviewMi : {}", nextReviewMiDTO);

        return nextReviewMiRepository
            .findById(nextReviewMiDTO.getId())
            .map(existingNextReviewMi -> {
                nextReviewMiMapper.partialUpdate(existingNextReviewMi, nextReviewMiDTO);

                return existingNextReviewMi;
            })
            .map(nextReviewMiRepository::save)
            .map(nextReviewMiMapper::toDto);
    }

    /**
     * Get all the nextReviewMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewMiRepository.findAllWithEagerRelationships(pageable).map(nextReviewMiMapper::toDto);
    }

    /**
     * Get one nextReviewMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewMi : {}", id);
        return nextReviewMiRepository.findOneWithEagerRelationships(id).map(nextReviewMiMapper::toDto);
    }

    /**
     * Delete the nextReviewMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewMi : {}", id);
        nextReviewMiRepository.deleteById(id);
    }
}
