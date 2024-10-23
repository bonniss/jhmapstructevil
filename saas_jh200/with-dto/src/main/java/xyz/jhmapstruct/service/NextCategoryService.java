package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.repository.NextCategoryRepository;
import xyz.jhmapstruct.service.dto.NextCategoryDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategory}.
 */
@Service
@Transactional
public class NextCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryService.class);

    private final NextCategoryRepository nextCategoryRepository;

    private final NextCategoryMapper nextCategoryMapper;

    public NextCategoryService(NextCategoryRepository nextCategoryRepository, NextCategoryMapper nextCategoryMapper) {
        this.nextCategoryRepository = nextCategoryRepository;
        this.nextCategoryMapper = nextCategoryMapper;
    }

    /**
     * Save a nextCategory.
     *
     * @param nextCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryDTO save(NextCategoryDTO nextCategoryDTO) {
        LOG.debug("Request to save NextCategory : {}", nextCategoryDTO);
        NextCategory nextCategory = nextCategoryMapper.toEntity(nextCategoryDTO);
        nextCategory = nextCategoryRepository.save(nextCategory);
        return nextCategoryMapper.toDto(nextCategory);
    }

    /**
     * Update a nextCategory.
     *
     * @param nextCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryDTO update(NextCategoryDTO nextCategoryDTO) {
        LOG.debug("Request to update NextCategory : {}", nextCategoryDTO);
        NextCategory nextCategory = nextCategoryMapper.toEntity(nextCategoryDTO);
        nextCategory = nextCategoryRepository.save(nextCategory);
        return nextCategoryMapper.toDto(nextCategory);
    }

    /**
     * Partially update a nextCategory.
     *
     * @param nextCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryDTO> partialUpdate(NextCategoryDTO nextCategoryDTO) {
        LOG.debug("Request to partially update NextCategory : {}", nextCategoryDTO);

        return nextCategoryRepository
            .findById(nextCategoryDTO.getId())
            .map(existingNextCategory -> {
                nextCategoryMapper.partialUpdate(existingNextCategory, nextCategoryDTO);

                return existingNextCategory;
            })
            .map(nextCategoryRepository::save)
            .map(nextCategoryMapper::toDto);
    }

    /**
     * Get one nextCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategory : {}", id);
        return nextCategoryRepository.findById(id).map(nextCategoryMapper::toDto);
    }

    /**
     * Delete the nextCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategory : {}", id);
        nextCategoryRepository.deleteById(id);
    }
}
