package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReviewVi;
import xyz.jhmapstruct.repository.NextReviewViRepository;
import xyz.jhmapstruct.service.dto.NextReviewViDTO;
import xyz.jhmapstruct.service.mapper.NextReviewViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReviewVi}.
 */
@Service
@Transactional
public class NextReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViService.class);

    private final NextReviewViRepository nextReviewViRepository;

    private final NextReviewViMapper nextReviewViMapper;

    public NextReviewViService(NextReviewViRepository nextReviewViRepository, NextReviewViMapper nextReviewViMapper) {
        this.nextReviewViRepository = nextReviewViRepository;
        this.nextReviewViMapper = nextReviewViMapper;
    }

    /**
     * Save a nextReviewVi.
     *
     * @param nextReviewViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViDTO save(NextReviewViDTO nextReviewViDTO) {
        LOG.debug("Request to save NextReviewVi : {}", nextReviewViDTO);
        NextReviewVi nextReviewVi = nextReviewViMapper.toEntity(nextReviewViDTO);
        nextReviewVi = nextReviewViRepository.save(nextReviewVi);
        return nextReviewViMapper.toDto(nextReviewVi);
    }

    /**
     * Update a nextReviewVi.
     *
     * @param nextReviewViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewViDTO update(NextReviewViDTO nextReviewViDTO) {
        LOG.debug("Request to update NextReviewVi : {}", nextReviewViDTO);
        NextReviewVi nextReviewVi = nextReviewViMapper.toEntity(nextReviewViDTO);
        nextReviewVi = nextReviewViRepository.save(nextReviewVi);
        return nextReviewViMapper.toDto(nextReviewVi);
    }

    /**
     * Partially update a nextReviewVi.
     *
     * @param nextReviewViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewViDTO> partialUpdate(NextReviewViDTO nextReviewViDTO) {
        LOG.debug("Request to partially update NextReviewVi : {}", nextReviewViDTO);

        return nextReviewViRepository
            .findById(nextReviewViDTO.getId())
            .map(existingNextReviewVi -> {
                nextReviewViMapper.partialUpdate(existingNextReviewVi, nextReviewViDTO);

                return existingNextReviewVi;
            })
            .map(nextReviewViRepository::save)
            .map(nextReviewViMapper::toDto);
    }

    /**
     * Get all the nextReviewVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewViRepository.findAllWithEagerRelationships(pageable).map(nextReviewViMapper::toDto);
    }

    /**
     * Get one nextReviewVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewViDTO> findOne(Long id) {
        LOG.debug("Request to get NextReviewVi : {}", id);
        return nextReviewViRepository.findOneWithEagerRelationships(id).map(nextReviewViMapper::toDto);
    }

    /**
     * Delete the nextReviewVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReviewVi : {}", id);
        nextReviewViRepository.deleteById(id);
    }
}
