package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.repository.NextOrderThetaRepository;
import xyz.jhmapstruct.service.dto.NextOrderThetaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderTheta}.
 */
@Service
@Transactional
public class NextOrderThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderThetaService.class);

    private final NextOrderThetaRepository nextOrderThetaRepository;

    private final NextOrderThetaMapper nextOrderThetaMapper;

    public NextOrderThetaService(NextOrderThetaRepository nextOrderThetaRepository, NextOrderThetaMapper nextOrderThetaMapper) {
        this.nextOrderThetaRepository = nextOrderThetaRepository;
        this.nextOrderThetaMapper = nextOrderThetaMapper;
    }

    /**
     * Save a nextOrderTheta.
     *
     * @param nextOrderThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderThetaDTO save(NextOrderThetaDTO nextOrderThetaDTO) {
        LOG.debug("Request to save NextOrderTheta : {}", nextOrderThetaDTO);
        NextOrderTheta nextOrderTheta = nextOrderThetaMapper.toEntity(nextOrderThetaDTO);
        nextOrderTheta = nextOrderThetaRepository.save(nextOrderTheta);
        return nextOrderThetaMapper.toDto(nextOrderTheta);
    }

    /**
     * Update a nextOrderTheta.
     *
     * @param nextOrderThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderThetaDTO update(NextOrderThetaDTO nextOrderThetaDTO) {
        LOG.debug("Request to update NextOrderTheta : {}", nextOrderThetaDTO);
        NextOrderTheta nextOrderTheta = nextOrderThetaMapper.toEntity(nextOrderThetaDTO);
        nextOrderTheta = nextOrderThetaRepository.save(nextOrderTheta);
        return nextOrderThetaMapper.toDto(nextOrderTheta);
    }

    /**
     * Partially update a nextOrderTheta.
     *
     * @param nextOrderThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderThetaDTO> partialUpdate(NextOrderThetaDTO nextOrderThetaDTO) {
        LOG.debug("Request to partially update NextOrderTheta : {}", nextOrderThetaDTO);

        return nextOrderThetaRepository
            .findById(nextOrderThetaDTO.getId())
            .map(existingNextOrderTheta -> {
                nextOrderThetaMapper.partialUpdate(existingNextOrderTheta, nextOrderThetaDTO);

                return existingNextOrderTheta;
            })
            .map(nextOrderThetaRepository::save)
            .map(nextOrderThetaMapper::toDto);
    }

    /**
     * Get all the nextOrderThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderThetaRepository.findAllWithEagerRelationships(pageable).map(nextOrderThetaMapper::toDto);
    }

    /**
     * Get one nextOrderTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderTheta : {}", id);
        return nextOrderThetaRepository.findOneWithEagerRelationships(id).map(nextOrderThetaMapper::toDto);
    }

    /**
     * Delete the nextOrderTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderTheta : {}", id);
        nextOrderThetaRepository.deleteById(id);
    }
}
