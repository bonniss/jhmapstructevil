package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.repository.NextOrderSigmaRepository;
import xyz.jhmapstruct.service.dto.NextOrderSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderSigma}.
 */
@Service
@Transactional
public class NextOrderSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderSigmaService.class);

    private final NextOrderSigmaRepository nextOrderSigmaRepository;

    private final NextOrderSigmaMapper nextOrderSigmaMapper;

    public NextOrderSigmaService(NextOrderSigmaRepository nextOrderSigmaRepository, NextOrderSigmaMapper nextOrderSigmaMapper) {
        this.nextOrderSigmaRepository = nextOrderSigmaRepository;
        this.nextOrderSigmaMapper = nextOrderSigmaMapper;
    }

    /**
     * Save a nextOrderSigma.
     *
     * @param nextOrderSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderSigmaDTO save(NextOrderSigmaDTO nextOrderSigmaDTO) {
        LOG.debug("Request to save NextOrderSigma : {}", nextOrderSigmaDTO);
        NextOrderSigma nextOrderSigma = nextOrderSigmaMapper.toEntity(nextOrderSigmaDTO);
        nextOrderSigma = nextOrderSigmaRepository.save(nextOrderSigma);
        return nextOrderSigmaMapper.toDto(nextOrderSigma);
    }

    /**
     * Update a nextOrderSigma.
     *
     * @param nextOrderSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOrderSigmaDTO update(NextOrderSigmaDTO nextOrderSigmaDTO) {
        LOG.debug("Request to update NextOrderSigma : {}", nextOrderSigmaDTO);
        NextOrderSigma nextOrderSigma = nextOrderSigmaMapper.toEntity(nextOrderSigmaDTO);
        nextOrderSigma = nextOrderSigmaRepository.save(nextOrderSigma);
        return nextOrderSigmaMapper.toDto(nextOrderSigma);
    }

    /**
     * Partially update a nextOrderSigma.
     *
     * @param nextOrderSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderSigmaDTO> partialUpdate(NextOrderSigmaDTO nextOrderSigmaDTO) {
        LOG.debug("Request to partially update NextOrderSigma : {}", nextOrderSigmaDTO);

        return nextOrderSigmaRepository
            .findById(nextOrderSigmaDTO.getId())
            .map(existingNextOrderSigma -> {
                nextOrderSigmaMapper.partialUpdate(existingNextOrderSigma, nextOrderSigmaDTO);

                return existingNextOrderSigma;
            })
            .map(nextOrderSigmaRepository::save)
            .map(nextOrderSigmaMapper::toDto);
    }

    /**
     * Get all the nextOrderSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderSigmaRepository.findAllWithEagerRelationships(pageable).map(nextOrderSigmaMapper::toDto);
    }

    /**
     * Get one nextOrderSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextOrderSigma : {}", id);
        return nextOrderSigmaRepository.findOneWithEagerRelationships(id).map(nextOrderSigmaMapper::toDto);
    }

    /**
     * Delete the nextOrderSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderSigma : {}", id);
        nextOrderSigmaRepository.deleteById(id);
    }
}
