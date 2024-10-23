package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewMiMi;
import xyz.jhmapstruct.repository.NextReviewMiMiRepository;
import xyz.jhmapstruct.service.dto.NextReviewMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewMiMi}.
 */
@Service
@Transactional
public class NextReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiMiService.class);

    private final NextReviewMiMiRepository nextReviewMiMiRepository;

    private final NextReviewMiMiMapper nextReviewMiMiMapper;

    public NextReviewMiMiService(NextReviewMiMiRepository nextReviewMiMiRepository, NextReviewMiMiMapper nextReviewMiMiMapper) {
        this.nextReviewMiMiRepository = nextReviewMiMiRepository;
        this.nextReviewMiMiMapper = nextReviewMiMiMapper;
    }

    /**
     * Save a nextReviewMiMi.
     *
     * @param nextReviewMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiMiDTO save(NextReviewMiMiDTO nextReviewMiMiDTO) {
        LOG.debug("Request to save NextReviewMiMi : {}", nextReviewMiMiDTO);
        NextReviewMiMi nextReviewMiMi = nextReviewMiMiMapper.toEntity(nextReviewMiMiDTO);
        nextReviewMiMi = nextReviewMiMiRepository.save(nextReviewMiMi);
        return nextReviewMiMiMapper.toDto(nextReviewMiMi);
    }

    /**
     * Update a nextReviewMiMi.
     *
     * @param nextReviewMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewMiMiDTO update(NextReviewMiMiDTO nextReviewMiMiDTO) {
        LOG.debug("Request to update NextReviewMiMi : {}", nextReviewMiMiDTO);
        NextReviewMiMi nextReviewMiMi = nextReviewMiMiMapper.toEntity(nextReviewMiMiDTO);
        nextReviewMiMi = nextReviewMiMiRepository.save(nextReviewMiMi);
        return nextReviewMiMiMapper.toDto(nextReviewMiMi);
    }

    /**
     * Partially update a nextReviewMiMi.
     *
     * @param nextReviewMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewMiMiDTO> partialUpdate(NextReviewMiMiDTO nextReviewMiMiDTO) {
        LOG.debug("Request to partially update NextReviewMiMi : {}", nextReviewMiMiDTO);

        return nextReviewMiMiRepository
            .findById(nextReviewMiMiDTO.getId())
            .map(existingNextReviewMiMi -> {
                nextReviewMiMiMapper.partialUpdate(existingNextReviewMiMi, nextReviewMiMiDTO);

                return existingNextReviewMiMi;
            })
            .map(nextReviewMiMiRepository::save)
            .map(nextReviewMiMiMapper::toDto);
    }

    /**
     * Get all the nextReviewMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewMiMiRepository.findAllWithEagerRelationships(pageable).map(nextReviewMiMiMapper::toDto);
    }

    /**
     * Get one nextReviewMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewMiMi : {}", id);
        return nextReviewMiMiRepository.findOneWithEagerRelationships(id).map(nextReviewMiMiMapper::toDto);
    }

    /**
     * Delete the nextReviewMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewMiMi : {}", id);
        nextReviewMiMiRepository.deleteById(id);
    }
}
