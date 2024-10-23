package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.repository.NextProductViRepository;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductVi}.
 */
@Service
@Transactional
public class NextProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViService.class);

    private final NextProductViRepository nextProductViRepository;

    private final NextProductViMapper nextProductViMapper;

    public NextProductViService(NextProductViRepository nextProductViRepository, NextProductViMapper nextProductViMapper) {
        this.nextProductViRepository = nextProductViRepository;
        this.nextProductViMapper = nextProductViMapper;
    }

    /**
     * Save a nextProductVi.
     *
     * @param nextProductViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductViDTO save(NextProductViDTO nextProductViDTO) {
        LOG.debug("Request to save NextProductVi : {}", nextProductViDTO);
        NextProductVi nextProductVi = nextProductViMapper.toEntity(nextProductViDTO);
        nextProductVi = nextProductViRepository.save(nextProductVi);
        return nextProductViMapper.toDto(nextProductVi);
    }

    /**
     * Update a nextProductVi.
     *
     * @param nextProductViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductViDTO update(NextProductViDTO nextProductViDTO) {
        LOG.debug("Request to update NextProductVi : {}", nextProductViDTO);
        NextProductVi nextProductVi = nextProductViMapper.toEntity(nextProductViDTO);
        nextProductVi = nextProductViRepository.save(nextProductVi);
        return nextProductViMapper.toDto(nextProductVi);
    }

    /**
     * Partially update a nextProductVi.
     *
     * @param nextProductViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductViDTO> partialUpdate(NextProductViDTO nextProductViDTO) {
        LOG.debug("Request to partially update NextProductVi : {}", nextProductViDTO);

        return nextProductViRepository
            .findById(nextProductViDTO.getId())
            .map(existingNextProductVi -> {
                nextProductViMapper.partialUpdate(existingNextProductVi, nextProductViDTO);

                return existingNextProductVi;
            })
            .map(nextProductViRepository::save)
            .map(nextProductViMapper::toDto);
    }

    /**
     * Get all the nextProductVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductViRepository.findAllWithEagerRelationships(pageable).map(nextProductViMapper::toDto);
    }

    /**
     * Get one nextProductVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductViDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductVi : {}", id);
        return nextProductViRepository.findOneWithEagerRelationships(id).map(nextProductViMapper::toDto);
    }

    /**
     * Delete the nextProductVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductVi : {}", id);
        nextProductViRepository.deleteById(id);
    }
}
