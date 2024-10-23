package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.repository.NextOrderGammaRepository;
import xyz.jhmapstruct.service.dto.NextOrderGammaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderGamma}.
 */
@Service
@Transactional
public class NextOrderGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderGammaService.class);

    private final NextOrderGammaRepository nextOrderGammaRepository;

    private final NextOrderGammaMapper nextOrderGammaMapper;

    public NextOrderGammaService(NextOrderGammaRepository nextOrderGammaRepository, NextOrderGammaMapper nextOrderGammaMapper) {
        this.nextOrderGammaRepository = nextOrderGammaRepository;
        this.nextOrderGammaMapper = nextOrderGammaMapper;
    }

    /**
     * Save a nextOrderGamma.
     *
     * @param nextOrderGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderGammaDTO save(NextOrderGammaDTO nextOrderGammaDTO) {
        LOG.debug("Request to save NextOrderGamma : {}", nextOrderGammaDTO);
        NextOrderGamma nextOrderGamma = nextOrderGammaMapper.toEntity(nextOrderGammaDTO);
        nextOrderGamma = nextOrderGammaRepository.save(nextOrderGamma);
        return nextOrderGammaMapper.toDto(nextOrderGamma);
    }

    /**
     * Update a nextOrderGamma.
     *
     * @param nextOrderGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderGammaDTO update(NextOrderGammaDTO nextOrderGammaDTO) {
        LOG.debug("Request to update NextOrderGamma : {}", nextOrderGammaDTO);
        NextOrderGamma nextOrderGamma = nextOrderGammaMapper.toEntity(nextOrderGammaDTO);
        nextOrderGamma = nextOrderGammaRepository.save(nextOrderGamma);
        return nextOrderGammaMapper.toDto(nextOrderGamma);
    }

    /**
     * Partially update a nextOrderGamma.
     *
     * @param nextOrderGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderGammaDTO> partialUpdate(NextOrderGammaDTO nextOrderGammaDTO) {
        LOG.debug("Request to partially update NextOrderGamma : {}", nextOrderGammaDTO);

        return nextOrderGammaRepository
            .findById(nextOrderGammaDTO.getId())
            .map(existingNextOrderGamma -> {
                nextOrderGammaMapper.partialUpdate(existingNextOrderGamma, nextOrderGammaDTO);

                return existingNextOrderGamma;
            })
            .map(nextOrderGammaRepository::save)
            .map(nextOrderGammaMapper::toDto);
    }

    /**
     * Get all the nextOrderGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderGammaRepository.findAllWithEagerRelationships(pageable).map(nextOrderGammaMapper::toDto);
    }

    /**
     * Get one nextOrderGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderGamma : {}", id);
        return nextOrderGammaRepository.findOneWithEagerRelationships(id).map(nextOrderGammaMapper::toDto);
    }

    /**
     * Delete the nextOrderGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderGamma : {}", id);
        nextOrderGammaRepository.deleteById(id);
    }
}
