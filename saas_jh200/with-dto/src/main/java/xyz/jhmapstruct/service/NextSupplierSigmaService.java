package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextSupplierSigmaRepository;
import xyz.jhmapstruct.service.dto.NextSupplierSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierSigma}.
 */
@Service
@Transactional
public class NextSupplierSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierSigmaService.class);

    private final NextSupplierSigmaRepository nextSupplierSigmaRepository;

    private final NextSupplierSigmaMapper nextSupplierSigmaMapper;

    public NextSupplierSigmaService(
        NextSupplierSigmaRepository nextSupplierSigmaRepository,
        NextSupplierSigmaMapper nextSupplierSigmaMapper
    ) {
        this.nextSupplierSigmaRepository = nextSupplierSigmaRepository;
        this.nextSupplierSigmaMapper = nextSupplierSigmaMapper;
    }

    /**
     * Save a nextSupplierSigma.
     *
     * @param nextSupplierSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierSigmaDTO save(NextSupplierSigmaDTO nextSupplierSigmaDTO) {
        LOG.debug("Request to save NextSupplierSigma : {}", nextSupplierSigmaDTO);
        NextSupplierSigma nextSupplierSigma = nextSupplierSigmaMapper.toEntity(nextSupplierSigmaDTO);
        nextSupplierSigma = nextSupplierSigmaRepository.save(nextSupplierSigma);
        return nextSupplierSigmaMapper.toDto(nextSupplierSigma);
    }

    /**
     * Update a nextSupplierSigma.
     *
     * @param nextSupplierSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierSigmaDTO update(NextSupplierSigmaDTO nextSupplierSigmaDTO) {
        LOG.debug("Request to update NextSupplierSigma : {}", nextSupplierSigmaDTO);
        NextSupplierSigma nextSupplierSigma = nextSupplierSigmaMapper.toEntity(nextSupplierSigmaDTO);
        nextSupplierSigma = nextSupplierSigmaRepository.save(nextSupplierSigma);
        return nextSupplierSigmaMapper.toDto(nextSupplierSigma);
    }

    /**
     * Partially update a nextSupplierSigma.
     *
     * @param nextSupplierSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierSigmaDTO> partialUpdate(NextSupplierSigmaDTO nextSupplierSigmaDTO) {
        LOG.debug("Request to partially update NextSupplierSigma : {}", nextSupplierSigmaDTO);

        return nextSupplierSigmaRepository
            .findById(nextSupplierSigmaDTO.getId())
            .map(existingNextSupplierSigma -> {
                nextSupplierSigmaMapper.partialUpdate(existingNextSupplierSigma, nextSupplierSigmaDTO);

                return existingNextSupplierSigma;
            })
            .map(nextSupplierSigmaRepository::save)
            .map(nextSupplierSigmaMapper::toDto);
    }

    /**
     * Get all the nextSupplierSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierSigmaRepository.findAllWithEagerRelationships(pageable).map(nextSupplierSigmaMapper::toDto);
    }

    /**
     * Get one nextSupplierSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierSigma : {}", id);
        return nextSupplierSigmaRepository.findOneWithEagerRelationships(id).map(nextSupplierSigmaMapper::toDto);
    }

    /**
     * Delete the nextSupplierSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierSigma : {}", id);
        nextSupplierSigmaRepository.deleteById(id);
    }
}
