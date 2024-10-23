package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.repository.NextSupplierBetaRepository;
import xyz.jhmapstruct.service.dto.NextSupplierBetaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierBeta}.
 */
@Service
@Transactional
public class NextSupplierBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierBetaService.class);

    private final NextSupplierBetaRepository nextSupplierBetaRepository;

    private final NextSupplierBetaMapper nextSupplierBetaMapper;

    public NextSupplierBetaService(NextSupplierBetaRepository nextSupplierBetaRepository, NextSupplierBetaMapper nextSupplierBetaMapper) {
        this.nextSupplierBetaRepository = nextSupplierBetaRepository;
        this.nextSupplierBetaMapper = nextSupplierBetaMapper;
    }

    /**
     * Save a nextSupplierBeta.
     *
     * @param nextSupplierBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierBetaDTO save(NextSupplierBetaDTO nextSupplierBetaDTO) {
        LOG.debug("Request to save NextSupplierBeta : {}", nextSupplierBetaDTO);
        NextSupplierBeta nextSupplierBeta = nextSupplierBetaMapper.toEntity(nextSupplierBetaDTO);
        nextSupplierBeta = nextSupplierBetaRepository.save(nextSupplierBeta);
        return nextSupplierBetaMapper.toDto(nextSupplierBeta);
    }

    /**
     * Update a nextSupplierBeta.
     *
     * @param nextSupplierBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierBetaDTO update(NextSupplierBetaDTO nextSupplierBetaDTO) {
        LOG.debug("Request to update NextSupplierBeta : {}", nextSupplierBetaDTO);
        NextSupplierBeta nextSupplierBeta = nextSupplierBetaMapper.toEntity(nextSupplierBetaDTO);
        nextSupplierBeta = nextSupplierBetaRepository.save(nextSupplierBeta);
        return nextSupplierBetaMapper.toDto(nextSupplierBeta);
    }

    /**
     * Partially update a nextSupplierBeta.
     *
     * @param nextSupplierBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierBetaDTO> partialUpdate(NextSupplierBetaDTO nextSupplierBetaDTO) {
        LOG.debug("Request to partially update NextSupplierBeta : {}", nextSupplierBetaDTO);

        return nextSupplierBetaRepository
            .findById(nextSupplierBetaDTO.getId())
            .map(existingNextSupplierBeta -> {
                nextSupplierBetaMapper.partialUpdate(existingNextSupplierBeta, nextSupplierBetaDTO);

                return existingNextSupplierBeta;
            })
            .map(nextSupplierBetaRepository::save)
            .map(nextSupplierBetaMapper::toDto);
    }

    /**
     * Get all the nextSupplierBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierBetaRepository.findAllWithEagerRelationships(pageable).map(nextSupplierBetaMapper::toDto);
    }

    /**
     * Get one nextSupplierBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierBeta : {}", id);
        return nextSupplierBetaRepository.findOneWithEagerRelationships(id).map(nextSupplierBetaMapper::toDto);
    }

    /**
     * Delete the nextSupplierBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierBeta : {}", id);
        nextSupplierBetaRepository.deleteById(id);
    }
}
