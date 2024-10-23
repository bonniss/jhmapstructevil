package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.repository.NextCategoryMiRepository;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryMi}.
 */
@Service
@Transactional
public class NextCategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiService.class);

    private final NextCategoryMiRepository nextCategoryMiRepository;

    private final NextCategoryMiMapper nextCategoryMiMapper;

    public NextCategoryMiService(NextCategoryMiRepository nextCategoryMiRepository, NextCategoryMiMapper nextCategoryMiMapper) {
        this.nextCategoryMiRepository = nextCategoryMiRepository;
        this.nextCategoryMiMapper = nextCategoryMiMapper;
    }

    /**
     * Save a nextCategoryMi.
     *
     * @param nextCategoryMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiDTO save(NextCategoryMiDTO nextCategoryMiDTO) {
        LOG.debug("Request to save NextCategoryMi : {}", nextCategoryMiDTO);
        NextCategoryMi nextCategoryMi = nextCategoryMiMapper.toEntity(nextCategoryMiDTO);
        nextCategoryMi = nextCategoryMiRepository.save(nextCategoryMi);
        return nextCategoryMiMapper.toDto(nextCategoryMi);
    }

    /**
     * Update a nextCategoryMi.
     *
     * @param nextCategoryMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiDTO update(NextCategoryMiDTO nextCategoryMiDTO) {
        LOG.debug("Request to update NextCategoryMi : {}", nextCategoryMiDTO);
        NextCategoryMi nextCategoryMi = nextCategoryMiMapper.toEntity(nextCategoryMiDTO);
        nextCategoryMi = nextCategoryMiRepository.save(nextCategoryMi);
        return nextCategoryMiMapper.toDto(nextCategoryMi);
    }

    /**
     * Partially update a nextCategoryMi.
     *
     * @param nextCategoryMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryMiDTO> partialUpdate(NextCategoryMiDTO nextCategoryMiDTO) {
        LOG.debug("Request to partially update NextCategoryMi : {}", nextCategoryMiDTO);

        return nextCategoryMiRepository
            .findById(nextCategoryMiDTO.getId())
            .map(existingNextCategoryMi -> {
                nextCategoryMiMapper.partialUpdate(existingNextCategoryMi, nextCategoryMiDTO);

                return existingNextCategoryMi;
            })
            .map(nextCategoryMiRepository::save)
            .map(nextCategoryMiMapper::toDto);
    }

    /**
     * Get one nextCategoryMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryMi : {}", id);
        return nextCategoryMiRepository.findById(id).map(nextCategoryMiMapper::toDto);
    }

    /**
     * Delete the nextCategoryMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryMi : {}", id);
        nextCategoryMiRepository.deleteById(id);
    }
}
