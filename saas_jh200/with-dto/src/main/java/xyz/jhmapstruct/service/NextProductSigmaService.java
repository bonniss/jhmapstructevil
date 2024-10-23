package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.repository.NextProductSigmaRepository;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextProductSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductSigma}.
 */
@Service
@Transactional
public class NextProductSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductSigmaService.class);

    private final NextProductSigmaRepository nextProductSigmaRepository;

    private final NextProductSigmaMapper nextProductSigmaMapper;

    public NextProductSigmaService(NextProductSigmaRepository nextProductSigmaRepository, NextProductSigmaMapper nextProductSigmaMapper) {
        this.nextProductSigmaRepository = nextProductSigmaRepository;
        this.nextProductSigmaMapper = nextProductSigmaMapper;
    }

    /**
     * Save a nextProductSigma.
     *
     * @param nextProductSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductSigmaDTO save(NextProductSigmaDTO nextProductSigmaDTO) {
        LOG.debug("Request to save NextProductSigma : {}", nextProductSigmaDTO);
        NextProductSigma nextProductSigma = nextProductSigmaMapper.toEntity(nextProductSigmaDTO);
        nextProductSigma = nextProductSigmaRepository.save(nextProductSigma);
        return nextProductSigmaMapper.toDto(nextProductSigma);
    }

    /**
     * Update a nextProductSigma.
     *
     * @param nextProductSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductSigmaDTO update(NextProductSigmaDTO nextProductSigmaDTO) {
        LOG.debug("Request to update NextProductSigma : {}", nextProductSigmaDTO);
        NextProductSigma nextProductSigma = nextProductSigmaMapper.toEntity(nextProductSigmaDTO);
        nextProductSigma = nextProductSigmaRepository.save(nextProductSigma);
        return nextProductSigmaMapper.toDto(nextProductSigma);
    }

    /**
     * Partially update a nextProductSigma.
     *
     * @param nextProductSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductSigmaDTO> partialUpdate(NextProductSigmaDTO nextProductSigmaDTO) {
        LOG.debug("Request to partially update NextProductSigma : {}", nextProductSigmaDTO);

        return nextProductSigmaRepository
            .findById(nextProductSigmaDTO.getId())
            .map(existingNextProductSigma -> {
                nextProductSigmaMapper.partialUpdate(existingNextProductSigma, nextProductSigmaDTO);

                return existingNextProductSigma;
            })
            .map(nextProductSigmaRepository::save)
            .map(nextProductSigmaMapper::toDto);
    }

    /**
     * Get all the nextProductSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductSigmaRepository.findAllWithEagerRelationships(pageable).map(nextProductSigmaMapper::toDto);
    }

    /**
     * Get one nextProductSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductSigma : {}", id);
        return nextProductSigmaRepository.findOneWithEagerRelationships(id).map(nextProductSigmaMapper::toDto);
    }

    /**
     * Delete the nextProductSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductSigma : {}", id);
        nextProductSigmaRepository.deleteById(id);
    }
}
