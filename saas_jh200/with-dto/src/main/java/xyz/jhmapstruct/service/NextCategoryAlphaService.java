package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryAlpha}.
 */
@Service
@Transactional
public class NextCategoryAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryAlphaService.class);

    private final NextCategoryAlphaRepository nextCategoryAlphaRepository;

    private final NextCategoryAlphaMapper nextCategoryAlphaMapper;

    public NextCategoryAlphaService(
        NextCategoryAlphaRepository nextCategoryAlphaRepository,
        NextCategoryAlphaMapper nextCategoryAlphaMapper
    ) {
        this.nextCategoryAlphaRepository = nextCategoryAlphaRepository;
        this.nextCategoryAlphaMapper = nextCategoryAlphaMapper;
    }

    /**
     * Save a nextCategoryAlpha.
     *
     * @param nextCategoryAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryAlphaDTO save(NextCategoryAlphaDTO nextCategoryAlphaDTO) {
        LOG.debug("Request to save NextCategoryAlpha : {}", nextCategoryAlphaDTO);
        NextCategoryAlpha nextCategoryAlpha = nextCategoryAlphaMapper.toEntity(nextCategoryAlphaDTO);
        nextCategoryAlpha = nextCategoryAlphaRepository.save(nextCategoryAlpha);
        return nextCategoryAlphaMapper.toDto(nextCategoryAlpha);
    }

    /**
     * Update a nextCategoryAlpha.
     *
     * @param nextCategoryAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryAlphaDTO update(NextCategoryAlphaDTO nextCategoryAlphaDTO) {
        LOG.debug("Request to update NextCategoryAlpha : {}", nextCategoryAlphaDTO);
        NextCategoryAlpha nextCategoryAlpha = nextCategoryAlphaMapper.toEntity(nextCategoryAlphaDTO);
        nextCategoryAlpha = nextCategoryAlphaRepository.save(nextCategoryAlpha);
        return nextCategoryAlphaMapper.toDto(nextCategoryAlpha);
    }

    /**
     * Partially update a nextCategoryAlpha.
     *
     * @param nextCategoryAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryAlphaDTO> partialUpdate(NextCategoryAlphaDTO nextCategoryAlphaDTO) {
        LOG.debug("Request to partially update NextCategoryAlpha : {}", nextCategoryAlphaDTO);

        return nextCategoryAlphaRepository
            .findById(nextCategoryAlphaDTO.getId())
            .map(existingNextCategoryAlpha -> {
                nextCategoryAlphaMapper.partialUpdate(existingNextCategoryAlpha, nextCategoryAlphaDTO);

                return existingNextCategoryAlpha;
            })
            .map(nextCategoryAlphaRepository::save)
            .map(nextCategoryAlphaMapper::toDto);
    }

    /**
     * Get one nextCategoryAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryAlpha : {}", id);
        return nextCategoryAlphaRepository.findById(id).map(nextCategoryAlphaMapper::toDto);
    }

    /**
     * Delete the nextCategoryAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryAlpha : {}", id);
        nextCategoryAlphaRepository.deleteById(id);
    }
}
