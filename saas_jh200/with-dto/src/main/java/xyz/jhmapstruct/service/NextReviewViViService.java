package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewViVi;
import xyz.jhmapstruct.repository.NextReviewViViRepository;
import xyz.jhmapstruct.service.dto.NextReviewViViDTO;
import xyz.jhmapstruct.service.mapper.NextReviewViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewViVi}.
 */
@Service
@Transactional
public class NextReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViViService.class);

    private final NextReviewViViRepository nextReviewViViRepository;

    private final NextReviewViViMapper nextReviewViViMapper;

    public NextReviewViViService(NextReviewViViRepository nextReviewViViRepository, NextReviewViViMapper nextReviewViViMapper) {
        this.nextReviewViViRepository = nextReviewViViRepository;
        this.nextReviewViViMapper = nextReviewViViMapper;
    }

    /**
     * Save a nextReviewViVi.
     *
     * @param nextReviewViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViViDTO save(NextReviewViViDTO nextReviewViViDTO) {
        LOG.debug("Request to save NextReviewViVi : {}", nextReviewViViDTO);
        NextReviewViVi nextReviewViVi = nextReviewViViMapper.toEntity(nextReviewViViDTO);
        nextReviewViVi = nextReviewViViRepository.save(nextReviewViVi);
        return nextReviewViViMapper.toDto(nextReviewViVi);
    }

    /**
     * Update a nextReviewViVi.
     *
     * @param nextReviewViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViViDTO update(NextReviewViViDTO nextReviewViViDTO) {
        LOG.debug("Request to update NextReviewViVi : {}", nextReviewViViDTO);
        NextReviewViVi nextReviewViVi = nextReviewViViMapper.toEntity(nextReviewViViDTO);
        nextReviewViVi = nextReviewViViRepository.save(nextReviewViVi);
        return nextReviewViViMapper.toDto(nextReviewViVi);
    }

    /**
     * Partially update a nextReviewViVi.
     *
     * @param nextReviewViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewViViDTO> partialUpdate(NextReviewViViDTO nextReviewViViDTO) {
        LOG.debug("Request to partially update NextReviewViVi : {}", nextReviewViViDTO);

        return nextReviewViViRepository
            .findById(nextReviewViViDTO.getId())
            .map(existingNextReviewViVi -> {
                nextReviewViViMapper.partialUpdate(existingNextReviewViVi, nextReviewViViDTO);

                return existingNextReviewViVi;
            })
            .map(nextReviewViViRepository::save)
            .map(nextReviewViViMapper::toDto);
    }

    /**
     * Get all the nextReviewViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewViViRepository.findAllWithEagerRelationships(pageable).map(nextReviewViViMapper::toDto);
    }

    /**
     * Get one nextReviewViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewViVi : {}", id);
        return nextReviewViViRepository.findOneWithEagerRelationships(id).map(nextReviewViViMapper::toDto);
    }

    /**
     * Delete the nextReviewViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewViVi : {}", id);
        nextReviewViViRepository.deleteById(id);
    }
}
