package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.repository.NextProductMiMiRepository;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextProductMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductMiMi}.
 */
@Service
@Transactional
public class NextProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiMiService.class);

    private final NextProductMiMiRepository nextProductMiMiRepository;

    private final NextProductMiMiMapper nextProductMiMiMapper;

    public NextProductMiMiService(NextProductMiMiRepository nextProductMiMiRepository, NextProductMiMiMapper nextProductMiMiMapper) {
        this.nextProductMiMiRepository = nextProductMiMiRepository;
        this.nextProductMiMiMapper = nextProductMiMiMapper;
    }

    /**
     * Save a nextProductMiMi.
     *
     * @param nextProductMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiMiDTO save(NextProductMiMiDTO nextProductMiMiDTO) {
        LOG.debug("Request to save NextProductMiMi : {}", nextProductMiMiDTO);
        NextProductMiMi nextProductMiMi = nextProductMiMiMapper.toEntity(nextProductMiMiDTO);
        nextProductMiMi = nextProductMiMiRepository.save(nextProductMiMi);
        return nextProductMiMiMapper.toDto(nextProductMiMi);
    }

    /**
     * Update a nextProductMiMi.
     *
     * @param nextProductMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiMiDTO update(NextProductMiMiDTO nextProductMiMiDTO) {
        LOG.debug("Request to update NextProductMiMi : {}", nextProductMiMiDTO);
        NextProductMiMi nextProductMiMi = nextProductMiMiMapper.toEntity(nextProductMiMiDTO);
        nextProductMiMi = nextProductMiMiRepository.save(nextProductMiMi);
        return nextProductMiMiMapper.toDto(nextProductMiMi);
    }

    /**
     * Partially update a nextProductMiMi.
     *
     * @param nextProductMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductMiMiDTO> partialUpdate(NextProductMiMiDTO nextProductMiMiDTO) {
        LOG.debug("Request to partially update NextProductMiMi : {}", nextProductMiMiDTO);

        return nextProductMiMiRepository
            .findById(nextProductMiMiDTO.getId())
            .map(existingNextProductMiMi -> {
                nextProductMiMiMapper.partialUpdate(existingNextProductMiMi, nextProductMiMiDTO);

                return existingNextProductMiMi;
            })
            .map(nextProductMiMiRepository::save)
            .map(nextProductMiMiMapper::toDto);
    }

    /**
     * Get all the nextProductMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductMiMiRepository.findAllWithEagerRelationships(pageable).map(nextProductMiMiMapper::toDto);
    }

    /**
     * Get one nextProductMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductMiMi : {}", id);
        return nextProductMiMiRepository.findOneWithEagerRelationships(id).map(nextProductMiMiMapper::toDto);
    }

    /**
     * Delete the nextProductMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductMiMi : {}", id);
        nextProductMiMiRepository.deleteById(id);
    }
}
