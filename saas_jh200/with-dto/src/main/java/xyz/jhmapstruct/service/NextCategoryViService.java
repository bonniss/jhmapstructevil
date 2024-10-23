package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.repository.NextCategoryViRepository;
import xyz.jhmapstruct.service.dto.NextCategoryViDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryVi}.
 */
@Service
@Transactional
public class NextCategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViService.class);

    private final NextCategoryViRepository nextCategoryViRepository;

    private final NextCategoryViMapper nextCategoryViMapper;

    public NextCategoryViService(NextCategoryViRepository nextCategoryViRepository, NextCategoryViMapper nextCategoryViMapper) {
        this.nextCategoryViRepository = nextCategoryViRepository;
        this.nextCategoryViMapper = nextCategoryViMapper;
    }

    /**
     * Save a nextCategoryVi.
     *
     * @param nextCategoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViDTO save(NextCategoryViDTO nextCategoryViDTO) {
        LOG.debug("Request to save NextCategoryVi : {}", nextCategoryViDTO);
        NextCategoryVi nextCategoryVi = nextCategoryViMapper.toEntity(nextCategoryViDTO);
        nextCategoryVi = nextCategoryViRepository.save(nextCategoryVi);
        return nextCategoryViMapper.toDto(nextCategoryVi);
    }

    /**
     * Update a nextCategoryVi.
     *
     * @param nextCategoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViDTO update(NextCategoryViDTO nextCategoryViDTO) {
        LOG.debug("Request to update NextCategoryVi : {}", nextCategoryViDTO);
        NextCategoryVi nextCategoryVi = nextCategoryViMapper.toEntity(nextCategoryViDTO);
        nextCategoryVi = nextCategoryViRepository.save(nextCategoryVi);
        return nextCategoryViMapper.toDto(nextCategoryVi);
    }

    /**
     * Partially update a nextCategoryVi.
     *
     * @param nextCategoryViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryViDTO> partialUpdate(NextCategoryViDTO nextCategoryViDTO) {
        LOG.debug("Request to partially update NextCategoryVi : {}", nextCategoryViDTO);

        return nextCategoryViRepository
            .findById(nextCategoryViDTO.getId())
            .map(existingNextCategoryVi -> {
                nextCategoryViMapper.partialUpdate(existingNextCategoryVi, nextCategoryViDTO);

                return existingNextCategoryVi;
            })
            .map(nextCategoryViRepository::save)
            .map(nextCategoryViMapper::toDto);
    }

    /**
     * Get one nextCategoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryViDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryVi : {}", id);
        return nextCategoryViRepository.findById(id).map(nextCategoryViMapper::toDto);
    }

    /**
     * Delete the nextCategoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryVi : {}", id);
        nextCategoryViRepository.deleteById(id);
    }
}
