package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.repository.NextCategoryMiMiRepository;
import xyz.jhmapstruct.service.dto.NextCategoryMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryMiMi}.
 */
@Service
@Transactional
public class NextCategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiMiService.class);

    private final NextCategoryMiMiRepository nextCategoryMiMiRepository;

    private final NextCategoryMiMiMapper nextCategoryMiMiMapper;

    public NextCategoryMiMiService(NextCategoryMiMiRepository nextCategoryMiMiRepository, NextCategoryMiMiMapper nextCategoryMiMiMapper) {
        this.nextCategoryMiMiRepository = nextCategoryMiMiRepository;
        this.nextCategoryMiMiMapper = nextCategoryMiMiMapper;
    }

    /**
     * Save a nextCategoryMiMi.
     *
     * @param nextCategoryMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiMiDTO save(NextCategoryMiMiDTO nextCategoryMiMiDTO) {
        LOG.debug("Request to save NextCategoryMiMi : {}", nextCategoryMiMiDTO);
        NextCategoryMiMi nextCategoryMiMi = nextCategoryMiMiMapper.toEntity(nextCategoryMiMiDTO);
        nextCategoryMiMi = nextCategoryMiMiRepository.save(nextCategoryMiMi);
        return nextCategoryMiMiMapper.toDto(nextCategoryMiMi);
    }

    /**
     * Update a nextCategoryMiMi.
     *
     * @param nextCategoryMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiMiDTO update(NextCategoryMiMiDTO nextCategoryMiMiDTO) {
        LOG.debug("Request to update NextCategoryMiMi : {}", nextCategoryMiMiDTO);
        NextCategoryMiMi nextCategoryMiMi = nextCategoryMiMiMapper.toEntity(nextCategoryMiMiDTO);
        nextCategoryMiMi = nextCategoryMiMiRepository.save(nextCategoryMiMi);
        return nextCategoryMiMiMapper.toDto(nextCategoryMiMi);
    }

    /**
     * Partially update a nextCategoryMiMi.
     *
     * @param nextCategoryMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryMiMiDTO> partialUpdate(NextCategoryMiMiDTO nextCategoryMiMiDTO) {
        LOG.debug("Request to partially update NextCategoryMiMi : {}", nextCategoryMiMiDTO);

        return nextCategoryMiMiRepository
            .findById(nextCategoryMiMiDTO.getId())
            .map(existingNextCategoryMiMi -> {
                nextCategoryMiMiMapper.partialUpdate(existingNextCategoryMiMi, nextCategoryMiMiDTO);

                return existingNextCategoryMiMi;
            })
            .map(nextCategoryMiMiRepository::save)
            .map(nextCategoryMiMiMapper::toDto);
    }

    /**
     * Get one nextCategoryMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryMiMi : {}", id);
        return nextCategoryMiMiRepository.findById(id).map(nextCategoryMiMiMapper::toDto);
    }

    /**
     * Delete the nextCategoryMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryMiMi : {}", id);
        nextCategoryMiMiRepository.deleteById(id);
    }
}
