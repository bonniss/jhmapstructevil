package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.repository.NextCategoryBetaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryBeta}.
 */
@Service
@Transactional
public class NextCategoryBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryBetaService.class);

    private final NextCategoryBetaRepository nextCategoryBetaRepository;

    private final NextCategoryBetaMapper nextCategoryBetaMapper;

    public NextCategoryBetaService(NextCategoryBetaRepository nextCategoryBetaRepository, NextCategoryBetaMapper nextCategoryBetaMapper) {
        this.nextCategoryBetaRepository = nextCategoryBetaRepository;
        this.nextCategoryBetaMapper = nextCategoryBetaMapper;
    }

    /**
     * Save a nextCategoryBeta.
     *
     * @param nextCategoryBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryBetaDTO save(NextCategoryBetaDTO nextCategoryBetaDTO) {
        LOG.debug("Request to save NextCategoryBeta : {}", nextCategoryBetaDTO);
        NextCategoryBeta nextCategoryBeta = nextCategoryBetaMapper.toEntity(nextCategoryBetaDTO);
        nextCategoryBeta = nextCategoryBetaRepository.save(nextCategoryBeta);
        return nextCategoryBetaMapper.toDto(nextCategoryBeta);
    }

    /**
     * Update a nextCategoryBeta.
     *
     * @param nextCategoryBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryBetaDTO update(NextCategoryBetaDTO nextCategoryBetaDTO) {
        LOG.debug("Request to update NextCategoryBeta : {}", nextCategoryBetaDTO);
        NextCategoryBeta nextCategoryBeta = nextCategoryBetaMapper.toEntity(nextCategoryBetaDTO);
        nextCategoryBeta = nextCategoryBetaRepository.save(nextCategoryBeta);
        return nextCategoryBetaMapper.toDto(nextCategoryBeta);
    }

    /**
     * Partially update a nextCategoryBeta.
     *
     * @param nextCategoryBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryBetaDTO> partialUpdate(NextCategoryBetaDTO nextCategoryBetaDTO) {
        LOG.debug("Request to partially update NextCategoryBeta : {}", nextCategoryBetaDTO);

        return nextCategoryBetaRepository
            .findById(nextCategoryBetaDTO.getId())
            .map(existingNextCategoryBeta -> {
                nextCategoryBetaMapper.partialUpdate(existingNextCategoryBeta, nextCategoryBetaDTO);

                return existingNextCategoryBeta;
            })
            .map(nextCategoryBetaRepository::save)
            .map(nextCategoryBetaMapper::toDto);
    }

    /**
     * Get one nextCategoryBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryBeta : {}", id);
        return nextCategoryBetaRepository.findById(id).map(nextCategoryBetaMapper::toDto);
    }

    /**
     * Delete the nextCategoryBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryBeta : {}", id);
        nextCategoryBetaRepository.deleteById(id);
    }
}
