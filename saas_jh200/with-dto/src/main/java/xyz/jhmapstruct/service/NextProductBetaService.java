package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.repository.NextProductBetaRepository;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.service.mapper.NextProductBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductBeta}.
 */
@Service
@Transactional
public class NextProductBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductBetaService.class);

    private final NextProductBetaRepository nextProductBetaRepository;

    private final NextProductBetaMapper nextProductBetaMapper;

    public NextProductBetaService(NextProductBetaRepository nextProductBetaRepository, NextProductBetaMapper nextProductBetaMapper) {
        this.nextProductBetaRepository = nextProductBetaRepository;
        this.nextProductBetaMapper = nextProductBetaMapper;
    }

    /**
     * Save a nextProductBeta.
     *
     * @param nextProductBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductBetaDTO save(NextProductBetaDTO nextProductBetaDTO) {
        LOG.debug("Request to save NextProductBeta : {}", nextProductBetaDTO);
        NextProductBeta nextProductBeta = nextProductBetaMapper.toEntity(nextProductBetaDTO);
        nextProductBeta = nextProductBetaRepository.save(nextProductBeta);
        return nextProductBetaMapper.toDto(nextProductBeta);
    }

    /**
     * Update a nextProductBeta.
     *
     * @param nextProductBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductBetaDTO update(NextProductBetaDTO nextProductBetaDTO) {
        LOG.debug("Request to update NextProductBeta : {}", nextProductBetaDTO);
        NextProductBeta nextProductBeta = nextProductBetaMapper.toEntity(nextProductBetaDTO);
        nextProductBeta = nextProductBetaRepository.save(nextProductBeta);
        return nextProductBetaMapper.toDto(nextProductBeta);
    }

    /**
     * Partially update a nextProductBeta.
     *
     * @param nextProductBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductBetaDTO> partialUpdate(NextProductBetaDTO nextProductBetaDTO) {
        LOG.debug("Request to partially update NextProductBeta : {}", nextProductBetaDTO);

        return nextProductBetaRepository
            .findById(nextProductBetaDTO.getId())
            .map(existingNextProductBeta -> {
                nextProductBetaMapper.partialUpdate(existingNextProductBeta, nextProductBetaDTO);

                return existingNextProductBeta;
            })
            .map(nextProductBetaRepository::save)
            .map(nextProductBetaMapper::toDto);
    }

    /**
     * Get all the nextProductBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductBetaRepository.findAllWithEagerRelationships(pageable).map(nextProductBetaMapper::toDto);
    }

    /**
     * Get one nextProductBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductBeta : {}", id);
        return nextProductBetaRepository.findOneWithEagerRelationships(id).map(nextProductBetaMapper::toDto);
    }

    /**
     * Delete the nextProductBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductBeta : {}", id);
        nextProductBetaRepository.deleteById(id);
    }
}
