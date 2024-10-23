package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.repository.NextProductMiRepository;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.service.mapper.NextProductMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductMi}.
 */
@Service
@Transactional
public class NextProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiService.class);

    private final NextProductMiRepository nextProductMiRepository;

    private final NextProductMiMapper nextProductMiMapper;

    public NextProductMiService(NextProductMiRepository nextProductMiRepository, NextProductMiMapper nextProductMiMapper) {
        this.nextProductMiRepository = nextProductMiRepository;
        this.nextProductMiMapper = nextProductMiMapper;
    }

    /**
     * Save a nextProductMi.
     *
     * @param nextProductMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiDTO save(NextProductMiDTO nextProductMiDTO) {
        LOG.debug("Request to save NextProductMi : {}", nextProductMiDTO);
        NextProductMi nextProductMi = nextProductMiMapper.toEntity(nextProductMiDTO);
        nextProductMi = nextProductMiRepository.save(nextProductMi);
        return nextProductMiMapper.toDto(nextProductMi);
    }

    /**
     * Update a nextProductMi.
     *
     * @param nextProductMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiDTO update(NextProductMiDTO nextProductMiDTO) {
        LOG.debug("Request to update NextProductMi : {}", nextProductMiDTO);
        NextProductMi nextProductMi = nextProductMiMapper.toEntity(nextProductMiDTO);
        nextProductMi = nextProductMiRepository.save(nextProductMi);
        return nextProductMiMapper.toDto(nextProductMi);
    }

    /**
     * Partially update a nextProductMi.
     *
     * @param nextProductMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductMiDTO> partialUpdate(NextProductMiDTO nextProductMiDTO) {
        LOG.debug("Request to partially update NextProductMi : {}", nextProductMiDTO);

        return nextProductMiRepository
            .findById(nextProductMiDTO.getId())
            .map(existingNextProductMi -> {
                nextProductMiMapper.partialUpdate(existingNextProductMi, nextProductMiDTO);

                return existingNextProductMi;
            })
            .map(nextProductMiRepository::save)
            .map(nextProductMiMapper::toDto);
    }

    /**
     * Get all the nextProductMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductMiRepository.findAllWithEagerRelationships(pageable).map(nextProductMiMapper::toDto);
    }

    /**
     * Get one nextProductMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductMi : {}", id);
        return nextProductMiRepository.findOneWithEagerRelationships(id).map(nextProductMiMapper::toDto);
    }

    /**
     * Delete the nextProductMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductMi : {}", id);
        nextProductMiRepository.deleteById(id);
    }
}
