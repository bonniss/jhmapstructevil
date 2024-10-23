package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.repository.NextCategoryViViRepository;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryViVi}.
 */
@Service
@Transactional
public class NextCategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViViService.class);

    private final NextCategoryViViRepository nextCategoryViViRepository;

    private final NextCategoryViViMapper nextCategoryViViMapper;

    public NextCategoryViViService(NextCategoryViViRepository nextCategoryViViRepository, NextCategoryViViMapper nextCategoryViViMapper) {
        this.nextCategoryViViRepository = nextCategoryViViRepository;
        this.nextCategoryViViMapper = nextCategoryViViMapper;
    }

    /**
     * Save a nextCategoryViVi.
     *
     * @param nextCategoryViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViViDTO save(NextCategoryViViDTO nextCategoryViViDTO) {
        LOG.debug("Request to save NextCategoryViVi : {}", nextCategoryViViDTO);
        NextCategoryViVi nextCategoryViVi = nextCategoryViViMapper.toEntity(nextCategoryViViDTO);
        nextCategoryViVi = nextCategoryViViRepository.save(nextCategoryViVi);
        return nextCategoryViViMapper.toDto(nextCategoryViVi);
    }

    /**
     * Update a nextCategoryViVi.
     *
     * @param nextCategoryViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViViDTO update(NextCategoryViViDTO nextCategoryViViDTO) {
        LOG.debug("Request to update NextCategoryViVi : {}", nextCategoryViViDTO);
        NextCategoryViVi nextCategoryViVi = nextCategoryViViMapper.toEntity(nextCategoryViViDTO);
        nextCategoryViVi = nextCategoryViViRepository.save(nextCategoryViVi);
        return nextCategoryViViMapper.toDto(nextCategoryViVi);
    }

    /**
     * Partially update a nextCategoryViVi.
     *
     * @param nextCategoryViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryViViDTO> partialUpdate(NextCategoryViViDTO nextCategoryViViDTO) {
        LOG.debug("Request to partially update NextCategoryViVi : {}", nextCategoryViViDTO);

        return nextCategoryViViRepository
            .findById(nextCategoryViViDTO.getId())
            .map(existingNextCategoryViVi -> {
                nextCategoryViViMapper.partialUpdate(existingNextCategoryViVi, nextCategoryViViDTO);

                return existingNextCategoryViVi;
            })
            .map(nextCategoryViViRepository::save)
            .map(nextCategoryViViMapper::toDto);
    }

    /**
     * Get one nextCategoryViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryViVi : {}", id);
        return nextCategoryViViRepository.findById(id).map(nextCategoryViViMapper::toDto);
    }

    /**
     * Delete the nextCategoryViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryViVi : {}", id);
        nextCategoryViViRepository.deleteById(id);
    }
}
