package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.repository.NextOrderMiRepository;
import xyz.jhmapstruct.service.dto.NextOrderMiDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderMi}.
 */
@Service
@Transactional
public class NextOrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiService.class);

    private final NextOrderMiRepository nextOrderMiRepository;

    private final NextOrderMiMapper nextOrderMiMapper;

    public NextOrderMiService(NextOrderMiRepository nextOrderMiRepository, NextOrderMiMapper nextOrderMiMapper) {
        this.nextOrderMiRepository = nextOrderMiRepository;
        this.nextOrderMiMapper = nextOrderMiMapper;
    }

    /**
     * Save a nextOrderMi.
     *
     * @param nextOrderMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiDTO save(NextOrderMiDTO nextOrderMiDTO) {
        LOG.debug("Request to save NextOrderMi : {}", nextOrderMiDTO);
        NextOrderMi nextOrderMi = nextOrderMiMapper.toEntity(nextOrderMiDTO);
        nextOrderMi = nextOrderMiRepository.save(nextOrderMi);
        return nextOrderMiMapper.toDto(nextOrderMi);
    }

    /**
     * Update a nextOrderMi.
     *
     * @param nextOrderMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiDTO update(NextOrderMiDTO nextOrderMiDTO) {
        LOG.debug("Request to update NextOrderMi : {}", nextOrderMiDTO);
        NextOrderMi nextOrderMi = nextOrderMiMapper.toEntity(nextOrderMiDTO);
        nextOrderMi = nextOrderMiRepository.save(nextOrderMi);
        return nextOrderMiMapper.toDto(nextOrderMi);
    }

    /**
     * Partially update a nextOrderMi.
     *
     * @param nextOrderMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderMiDTO> partialUpdate(NextOrderMiDTO nextOrderMiDTO) {
        LOG.debug("Request to partially update NextOrderMi : {}", nextOrderMiDTO);

        return nextOrderMiRepository
            .findById(nextOrderMiDTO.getId())
            .map(existingNextOrderMi -> {
                nextOrderMiMapper.partialUpdate(existingNextOrderMi, nextOrderMiDTO);

                return existingNextOrderMi;
            })
            .map(nextOrderMiRepository::save)
            .map(nextOrderMiMapper::toDto);
    }

    /**
     * Get all the nextOrderMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderMiRepository.findAllWithEagerRelationships(pageable).map(nextOrderMiMapper::toDto);
    }

    /**
     * Get one nextOrderMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderMi : {}", id);
        return nextOrderMiRepository.findOneWithEagerRelationships(id).map(nextOrderMiMapper::toDto);
    }

    /**
     * Delete the nextOrderMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderMi : {}", id);
        nextOrderMiRepository.deleteById(id);
    }
}
