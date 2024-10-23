package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.repository.NextSupplierAlphaRepository;
import xyz.jhmapstruct.service.dto.NextSupplierAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierAlpha}.
 */
@Service
@Transactional
public class NextSupplierAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierAlphaService.class);

    private final NextSupplierAlphaRepository nextSupplierAlphaRepository;

    private final NextSupplierAlphaMapper nextSupplierAlphaMapper;

    public NextSupplierAlphaService(
        NextSupplierAlphaRepository nextSupplierAlphaRepository,
        NextSupplierAlphaMapper nextSupplierAlphaMapper
    ) {
        this.nextSupplierAlphaRepository = nextSupplierAlphaRepository;
        this.nextSupplierAlphaMapper = nextSupplierAlphaMapper;
    }

    /**
     * Save a nextSupplierAlpha.
     *
     * @param nextSupplierAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierAlphaDTO save(NextSupplierAlphaDTO nextSupplierAlphaDTO) {
        LOG.debug("Request to save NextSupplierAlpha : {}", nextSupplierAlphaDTO);
        NextSupplierAlpha nextSupplierAlpha = nextSupplierAlphaMapper.toEntity(nextSupplierAlphaDTO);
        nextSupplierAlpha = nextSupplierAlphaRepository.save(nextSupplierAlpha);
        return nextSupplierAlphaMapper.toDto(nextSupplierAlpha);
    }

    /**
     * Update a nextSupplierAlpha.
     *
     * @param nextSupplierAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierAlphaDTO update(NextSupplierAlphaDTO nextSupplierAlphaDTO) {
        LOG.debug("Request to update NextSupplierAlpha : {}", nextSupplierAlphaDTO);
        NextSupplierAlpha nextSupplierAlpha = nextSupplierAlphaMapper.toEntity(nextSupplierAlphaDTO);
        nextSupplierAlpha = nextSupplierAlphaRepository.save(nextSupplierAlpha);
        return nextSupplierAlphaMapper.toDto(nextSupplierAlpha);
    }

    /**
     * Partially update a nextSupplierAlpha.
     *
     * @param nextSupplierAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierAlphaDTO> partialUpdate(NextSupplierAlphaDTO nextSupplierAlphaDTO) {
        LOG.debug("Request to partially update NextSupplierAlpha : {}", nextSupplierAlphaDTO);

        return nextSupplierAlphaRepository
            .findById(nextSupplierAlphaDTO.getId())
            .map(existingNextSupplierAlpha -> {
                nextSupplierAlphaMapper.partialUpdate(existingNextSupplierAlpha, nextSupplierAlphaDTO);

                return existingNextSupplierAlpha;
            })
            .map(nextSupplierAlphaRepository::save)
            .map(nextSupplierAlphaMapper::toDto);
    }

    /**
     * Get all the nextSupplierAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierAlphaRepository.findAllWithEagerRelationships(pageable).map(nextSupplierAlphaMapper::toDto);
    }

    /**
     * Get one nextSupplierAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierAlpha : {}", id);
        return nextSupplierAlphaRepository.findOneWithEagerRelationships(id).map(nextSupplierAlphaMapper::toDto);
    }

    /**
     * Delete the nextSupplierAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierAlpha : {}", id);
        nextSupplierAlphaRepository.deleteById(id);
    }
}
