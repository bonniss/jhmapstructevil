package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.repository.NextProductThetaRepository;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.mapper.NextProductThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductTheta}.
 */
@Service
@Transactional
public class NextProductThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductThetaService.class);

    private final NextProductThetaRepository nextProductThetaRepository;

    private final NextProductThetaMapper nextProductThetaMapper;

    public NextProductThetaService(NextProductThetaRepository nextProductThetaRepository, NextProductThetaMapper nextProductThetaMapper) {
        this.nextProductThetaRepository = nextProductThetaRepository;
        this.nextProductThetaMapper = nextProductThetaMapper;
    }

    /**
     * Save a nextProductTheta.
     *
     * @param nextProductThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductThetaDTO save(NextProductThetaDTO nextProductThetaDTO) {
        LOG.debug("Request to save NextProductTheta : {}", nextProductThetaDTO);
        NextProductTheta nextProductTheta = nextProductThetaMapper.toEntity(nextProductThetaDTO);
        nextProductTheta = nextProductThetaRepository.save(nextProductTheta);
        return nextProductThetaMapper.toDto(nextProductTheta);
    }

    /**
     * Update a nextProductTheta.
     *
     * @param nextProductThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductThetaDTO update(NextProductThetaDTO nextProductThetaDTO) {
        LOG.debug("Request to update NextProductTheta : {}", nextProductThetaDTO);
        NextProductTheta nextProductTheta = nextProductThetaMapper.toEntity(nextProductThetaDTO);
        nextProductTheta = nextProductThetaRepository.save(nextProductTheta);
        return nextProductThetaMapper.toDto(nextProductTheta);
    }

    /**
     * Partially update a nextProductTheta.
     *
     * @param nextProductThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductThetaDTO> partialUpdate(NextProductThetaDTO nextProductThetaDTO) {
        LOG.debug("Request to partially update NextProductTheta : {}", nextProductThetaDTO);

        return nextProductThetaRepository
            .findById(nextProductThetaDTO.getId())
            .map(existingNextProductTheta -> {
                nextProductThetaMapper.partialUpdate(existingNextProductTheta, nextProductThetaDTO);

                return existingNextProductTheta;
            })
            .map(nextProductThetaRepository::save)
            .map(nextProductThetaMapper::toDto);
    }

    /**
     * Get all the nextProductThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductThetaRepository.findAllWithEagerRelationships(pageable).map(nextProductThetaMapper::toDto);
    }

    /**
     * Get one nextProductTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductTheta : {}", id);
        return nextProductThetaRepository.findOneWithEagerRelationships(id).map(nextProductThetaMapper::toDto);
    }

    /**
     * Delete the nextProductTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductTheta : {}", id);
        nextProductThetaRepository.deleteById(id);
    }
}
