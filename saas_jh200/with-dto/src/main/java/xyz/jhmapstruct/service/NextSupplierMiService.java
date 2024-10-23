package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.repository.NextSupplierMiRepository;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierMi}.
 */
@Service
@Transactional
public class NextSupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiService.class);

    private final NextSupplierMiRepository nextSupplierMiRepository;

    private final NextSupplierMiMapper nextSupplierMiMapper;

    public NextSupplierMiService(NextSupplierMiRepository nextSupplierMiRepository, NextSupplierMiMapper nextSupplierMiMapper) {
        this.nextSupplierMiRepository = nextSupplierMiRepository;
        this.nextSupplierMiMapper = nextSupplierMiMapper;
    }

    /**
     * Save a nextSupplierMi.
     *
     * @param nextSupplierMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiDTO save(NextSupplierMiDTO nextSupplierMiDTO) {
        LOG.debug("Request to save NextSupplierMi : {}", nextSupplierMiDTO);
        NextSupplierMi nextSupplierMi = nextSupplierMiMapper.toEntity(nextSupplierMiDTO);
        nextSupplierMi = nextSupplierMiRepository.save(nextSupplierMi);
        return nextSupplierMiMapper.toDto(nextSupplierMi);
    }

    /**
     * Update a nextSupplierMi.
     *
     * @param nextSupplierMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiDTO update(NextSupplierMiDTO nextSupplierMiDTO) {
        LOG.debug("Request to update NextSupplierMi : {}", nextSupplierMiDTO);
        NextSupplierMi nextSupplierMi = nextSupplierMiMapper.toEntity(nextSupplierMiDTO);
        nextSupplierMi = nextSupplierMiRepository.save(nextSupplierMi);
        return nextSupplierMiMapper.toDto(nextSupplierMi);
    }

    /**
     * Partially update a nextSupplierMi.
     *
     * @param nextSupplierMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierMiDTO> partialUpdate(NextSupplierMiDTO nextSupplierMiDTO) {
        LOG.debug("Request to partially update NextSupplierMi : {}", nextSupplierMiDTO);

        return nextSupplierMiRepository
            .findById(nextSupplierMiDTO.getId())
            .map(existingNextSupplierMi -> {
                nextSupplierMiMapper.partialUpdate(existingNextSupplierMi, nextSupplierMiDTO);

                return existingNextSupplierMi;
            })
            .map(nextSupplierMiRepository::save)
            .map(nextSupplierMiMapper::toDto);
    }

    /**
     * Get all the nextSupplierMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierMiRepository.findAllWithEagerRelationships(pageable).map(nextSupplierMiMapper::toDto);
    }

    /**
     * Get one nextSupplierMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierMi : {}", id);
        return nextSupplierMiRepository.findOneWithEagerRelationships(id).map(nextSupplierMiMapper::toDto);
    }

    /**
     * Delete the nextSupplierMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierMi : {}", id);
        nextSupplierMiRepository.deleteById(id);
    }
}
