package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextSupplierThetaRepository;
import xyz.jhmapstruct.service.dto.NextSupplierThetaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierTheta}.
 */
@Service
@Transactional
public class NextSupplierThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierThetaService.class);

    private final NextSupplierThetaRepository nextSupplierThetaRepository;

    private final NextSupplierThetaMapper nextSupplierThetaMapper;

    public NextSupplierThetaService(
        NextSupplierThetaRepository nextSupplierThetaRepository,
        NextSupplierThetaMapper nextSupplierThetaMapper
    ) {
        this.nextSupplierThetaRepository = nextSupplierThetaRepository;
        this.nextSupplierThetaMapper = nextSupplierThetaMapper;
    }

    /**
     * Save a nextSupplierTheta.
     *
     * @param nextSupplierThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierThetaDTO save(NextSupplierThetaDTO nextSupplierThetaDTO) {
        LOG.debug("Request to save NextSupplierTheta : {}", nextSupplierThetaDTO);
        NextSupplierTheta nextSupplierTheta = nextSupplierThetaMapper.toEntity(nextSupplierThetaDTO);
        nextSupplierTheta = nextSupplierThetaRepository.save(nextSupplierTheta);
        return nextSupplierThetaMapper.toDto(nextSupplierTheta);
    }

    /**
     * Update a nextSupplierTheta.
     *
     * @param nextSupplierThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierThetaDTO update(NextSupplierThetaDTO nextSupplierThetaDTO) {
        LOG.debug("Request to update NextSupplierTheta : {}", nextSupplierThetaDTO);
        NextSupplierTheta nextSupplierTheta = nextSupplierThetaMapper.toEntity(nextSupplierThetaDTO);
        nextSupplierTheta = nextSupplierThetaRepository.save(nextSupplierTheta);
        return nextSupplierThetaMapper.toDto(nextSupplierTheta);
    }

    /**
     * Partially update a nextSupplierTheta.
     *
     * @param nextSupplierThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierThetaDTO> partialUpdate(NextSupplierThetaDTO nextSupplierThetaDTO) {
        LOG.debug("Request to partially update NextSupplierTheta : {}", nextSupplierThetaDTO);

        return nextSupplierThetaRepository
            .findById(nextSupplierThetaDTO.getId())
            .map(existingNextSupplierTheta -> {
                nextSupplierThetaMapper.partialUpdate(existingNextSupplierTheta, nextSupplierThetaDTO);

                return existingNextSupplierTheta;
            })
            .map(nextSupplierThetaRepository::save)
            .map(nextSupplierThetaMapper::toDto);
    }

    /**
     * Get all the nextSupplierThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierThetaRepository.findAllWithEagerRelationships(pageable).map(nextSupplierThetaMapper::toDto);
    }

    /**
     * Get one nextSupplierTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierTheta : {}", id);
        return nextSupplierThetaRepository.findOneWithEagerRelationships(id).map(nextSupplierThetaMapper::toDto);
    }

    /**
     * Delete the nextSupplierTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierTheta : {}", id);
        nextSupplierThetaRepository.deleteById(id);
    }
}
