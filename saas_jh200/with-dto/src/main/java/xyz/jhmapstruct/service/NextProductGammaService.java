package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.repository.NextProductGammaRepository;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.service.mapper.NextProductGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductGamma}.
 */
@Service
@Transactional
public class NextProductGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductGammaService.class);

    private final NextProductGammaRepository nextProductGammaRepository;

    private final NextProductGammaMapper nextProductGammaMapper;

    public NextProductGammaService(NextProductGammaRepository nextProductGammaRepository, NextProductGammaMapper nextProductGammaMapper) {
        this.nextProductGammaRepository = nextProductGammaRepository;
        this.nextProductGammaMapper = nextProductGammaMapper;
    }

    /**
     * Save a nextProductGamma.
     *
     * @param nextProductGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductGammaDTO save(NextProductGammaDTO nextProductGammaDTO) {
        LOG.debug("Request to save NextProductGamma : {}", nextProductGammaDTO);
        NextProductGamma nextProductGamma = nextProductGammaMapper.toEntity(nextProductGammaDTO);
        nextProductGamma = nextProductGammaRepository.save(nextProductGamma);
        return nextProductGammaMapper.toDto(nextProductGamma);
    }

    /**
     * Update a nextProductGamma.
     *
     * @param nextProductGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductGammaDTO update(NextProductGammaDTO nextProductGammaDTO) {
        LOG.debug("Request to update NextProductGamma : {}", nextProductGammaDTO);
        NextProductGamma nextProductGamma = nextProductGammaMapper.toEntity(nextProductGammaDTO);
        nextProductGamma = nextProductGammaRepository.save(nextProductGamma);
        return nextProductGammaMapper.toDto(nextProductGamma);
    }

    /**
     * Partially update a nextProductGamma.
     *
     * @param nextProductGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductGammaDTO> partialUpdate(NextProductGammaDTO nextProductGammaDTO) {
        LOG.debug("Request to partially update NextProductGamma : {}", nextProductGammaDTO);

        return nextProductGammaRepository
            .findById(nextProductGammaDTO.getId())
            .map(existingNextProductGamma -> {
                nextProductGammaMapper.partialUpdate(existingNextProductGamma, nextProductGammaDTO);

                return existingNextProductGamma;
            })
            .map(nextProductGammaRepository::save)
            .map(nextProductGammaMapper::toDto);
    }

    /**
     * Get all the nextProductGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductGammaRepository.findAllWithEagerRelationships(pageable).map(nextProductGammaMapper::toDto);
    }

    /**
     * Get one nextProductGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextProductGamma : {}", id);
        return nextProductGammaRepository.findOneWithEagerRelationships(id).map(nextProductGammaMapper::toDto);
    }

    /**
     * Delete the nextProductGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductGamma : {}", id);
        nextProductGammaRepository.deleteById(id);
    }
}
