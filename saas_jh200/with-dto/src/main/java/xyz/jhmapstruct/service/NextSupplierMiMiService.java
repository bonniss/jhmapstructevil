package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.repository.NextSupplierMiMiRepository;
import xyz.jhmapstruct.service.dto.NextSupplierMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierMiMi}.
 */
@Service
@Transactional
public class NextSupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiMiService.class);

    private final NextSupplierMiMiRepository nextSupplierMiMiRepository;

    private final NextSupplierMiMiMapper nextSupplierMiMiMapper;

    public NextSupplierMiMiService(NextSupplierMiMiRepository nextSupplierMiMiRepository, NextSupplierMiMiMapper nextSupplierMiMiMapper) {
        this.nextSupplierMiMiRepository = nextSupplierMiMiRepository;
        this.nextSupplierMiMiMapper = nextSupplierMiMiMapper;
    }

    /**
     * Save a nextSupplierMiMi.
     *
     * @param nextSupplierMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiMiDTO save(NextSupplierMiMiDTO nextSupplierMiMiDTO) {
        LOG.debug("Request to save NextSupplierMiMi : {}", nextSupplierMiMiDTO);
        NextSupplierMiMi nextSupplierMiMi = nextSupplierMiMiMapper.toEntity(nextSupplierMiMiDTO);
        nextSupplierMiMi = nextSupplierMiMiRepository.save(nextSupplierMiMi);
        return nextSupplierMiMiMapper.toDto(nextSupplierMiMi);
    }

    /**
     * Update a nextSupplierMiMi.
     *
     * @param nextSupplierMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiMiDTO update(NextSupplierMiMiDTO nextSupplierMiMiDTO) {
        LOG.debug("Request to update NextSupplierMiMi : {}", nextSupplierMiMiDTO);
        NextSupplierMiMi nextSupplierMiMi = nextSupplierMiMiMapper.toEntity(nextSupplierMiMiDTO);
        nextSupplierMiMi = nextSupplierMiMiRepository.save(nextSupplierMiMi);
        return nextSupplierMiMiMapper.toDto(nextSupplierMiMi);
    }

    /**
     * Partially update a nextSupplierMiMi.
     *
     * @param nextSupplierMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierMiMiDTO> partialUpdate(NextSupplierMiMiDTO nextSupplierMiMiDTO) {
        LOG.debug("Request to partially update NextSupplierMiMi : {}", nextSupplierMiMiDTO);

        return nextSupplierMiMiRepository
            .findById(nextSupplierMiMiDTO.getId())
            .map(existingNextSupplierMiMi -> {
                nextSupplierMiMiMapper.partialUpdate(existingNextSupplierMiMi, nextSupplierMiMiDTO);

                return existingNextSupplierMiMi;
            })
            .map(nextSupplierMiMiRepository::save)
            .map(nextSupplierMiMiMapper::toDto);
    }

    /**
     * Get all the nextSupplierMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierMiMiRepository.findAllWithEagerRelationships(pageable).map(nextSupplierMiMiMapper::toDto);
    }

    /**
     * Get one nextSupplierMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextSupplierMiMi : {}", id);
        return nextSupplierMiMiRepository.findOneWithEagerRelationships(id).map(nextSupplierMiMiMapper::toDto);
    }

    /**
     * Delete the nextSupplierMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierMiMi : {}", id);
        nextSupplierMiMiRepository.deleteById(id);
    }
}
