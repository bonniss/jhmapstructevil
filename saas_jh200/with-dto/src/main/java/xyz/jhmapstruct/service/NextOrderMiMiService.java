package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;
import xyz.jhmapstruct.service.dto.NextOrderMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderMiMi}.
 */
@Service
@Transactional
public class NextOrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiMiService.class);

    private final NextOrderMiMiRepository nextOrderMiMiRepository;

    private final NextOrderMiMiMapper nextOrderMiMiMapper;

    public NextOrderMiMiService(NextOrderMiMiRepository nextOrderMiMiRepository, NextOrderMiMiMapper nextOrderMiMiMapper) {
        this.nextOrderMiMiRepository = nextOrderMiMiRepository;
        this.nextOrderMiMiMapper = nextOrderMiMiMapper;
    }

    /**
     * Save a nextOrderMiMi.
     *
     * @param nextOrderMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiMiDTO save(NextOrderMiMiDTO nextOrderMiMiDTO) {
        LOG.debug("Request to save NextOrderMiMi : {}", nextOrderMiMiDTO);
        NextOrderMiMi nextOrderMiMi = nextOrderMiMiMapper.toEntity(nextOrderMiMiDTO);
        nextOrderMiMi = nextOrderMiMiRepository.save(nextOrderMiMi);
        return nextOrderMiMiMapper.toDto(nextOrderMiMi);
    }

    /**
     * Update a nextOrderMiMi.
     *
     * @param nextOrderMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderMiMiDTO update(NextOrderMiMiDTO nextOrderMiMiDTO) {
        LOG.debug("Request to update NextOrderMiMi : {}", nextOrderMiMiDTO);
        NextOrderMiMi nextOrderMiMi = nextOrderMiMiMapper.toEntity(nextOrderMiMiDTO);
        nextOrderMiMi = nextOrderMiMiRepository.save(nextOrderMiMi);
        return nextOrderMiMiMapper.toDto(nextOrderMiMi);
    }

    /**
     * Partially update a nextOrderMiMi.
     *
     * @param nextOrderMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderMiMiDTO> partialUpdate(NextOrderMiMiDTO nextOrderMiMiDTO) {
        LOG.debug("Request to partially update NextOrderMiMi : {}", nextOrderMiMiDTO);

        return nextOrderMiMiRepository
            .findById(nextOrderMiMiDTO.getId())
            .map(existingNextOrderMiMi -> {
                nextOrderMiMiMapper.partialUpdate(existingNextOrderMiMi, nextOrderMiMiDTO);

                return existingNextOrderMiMi;
            })
            .map(nextOrderMiMiRepository::save)
            .map(nextOrderMiMiMapper::toDto);
    }

    /**
     * Get all the nextOrderMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderMiMiRepository.findAllWithEagerRelationships(pageable).map(nextOrderMiMiMapper::toDto);
    }

    /**
     * Get one nextOrderMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderMiMi : {}", id);
        return nextOrderMiMiRepository.findOneWithEagerRelationships(id).map(nextOrderMiMiMapper::toDto);
    }

    /**
     * Delete the nextOrderMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderMiMi : {}", id);
        nextOrderMiMiRepository.deleteById(id);
    }
}
