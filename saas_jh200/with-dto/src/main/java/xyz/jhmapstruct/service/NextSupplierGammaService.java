package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextSupplierGammaRepository;
import xyz.jhmapstruct.service.dto.NextSupplierGammaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierGamma}.
 */
@Service
@Transactional
public class NextSupplierGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierGammaService.class);

    private final NextSupplierGammaRepository nextSupplierGammaRepository;

    private final NextSupplierGammaMapper nextSupplierGammaMapper;

    public NextSupplierGammaService(
        NextSupplierGammaRepository nextSupplierGammaRepository,
        NextSupplierGammaMapper nextSupplierGammaMapper
    ) {
        this.nextSupplierGammaRepository = nextSupplierGammaRepository;
        this.nextSupplierGammaMapper = nextSupplierGammaMapper;
    }

    /**
     * Save a nextSupplierGamma.
     *
     * @param nextSupplierGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierGammaDTO save(NextSupplierGammaDTO nextSupplierGammaDTO) {
        LOG.debug("Request to save NextSupplierGamma : {}", nextSupplierGammaDTO);
        NextSupplierGamma nextSupplierGamma = nextSupplierGammaMapper.toEntity(nextSupplierGammaDTO);
        nextSupplierGamma = nextSupplierGammaRepository.save(nextSupplierGamma);
        return nextSupplierGammaMapper.toDto(nextSupplierGamma);
    }

    /**
     * Update a nextSupplierGamma.
     *
     * @param nextSupplierGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierGammaDTO update(NextSupplierGammaDTO nextSupplierGammaDTO) {
        LOG.debug("Request to update NextSupplierGamma : {}", nextSupplierGammaDTO);
        NextSupplierGamma nextSupplierGamma = nextSupplierGammaMapper.toEntity(nextSupplierGammaDTO);
        nextSupplierGamma = nextSupplierGammaRepository.save(nextSupplierGamma);
        return nextSupplierGammaMapper.toDto(nextSupplierGamma);
    }

    /**
     * Partially update a nextSupplierGamma.
     *
     * @param nextSupplierGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierGammaDTO> partialUpdate(NextSupplierGammaDTO nextSupplierGammaDTO) {
        LOG.debug("Request to partially update NextSupplierGamma : {}", nextSupplierGammaDTO);

        return nextSupplierGammaRepository
            .findById(nextSupplierGammaDTO.getId())
            .map(existingNextSupplierGamma -> {
                nextSupplierGammaMapper.partialUpdate(existingNextSupplierGamma, nextSupplierGammaDTO);

                return existingNextSupplierGamma;
            })
            .map(nextSupplierGammaRepository::save)
            .map(nextSupplierGammaMapper::toDto);
    }

    /**
     * Get all the nextSupplierGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierGammaRepository.findAllWithEagerRelationships(pageable).map(nextSupplierGammaMapper::toDto);
    }

    /**
     * Get one nextSupplierGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierGamma : {}", id);
        return nextSupplierGammaRepository.findOneWithEagerRelationships(id).map(nextSupplierGammaMapper::toDto);
    }

    /**
     * Delete the nextSupplierGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierGamma : {}", id);
        nextSupplierGammaRepository.deleteById(id);
    }
}
