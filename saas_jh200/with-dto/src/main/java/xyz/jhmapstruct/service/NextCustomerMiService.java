package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.repository.NextCustomerMiRepository;
import xyz.jhmapstruct.service.dto.NextCustomerMiDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerMi}.
 */
@Service
@Transactional
public class NextCustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiService.class);

    private final NextCustomerMiRepository nextCustomerMiRepository;

    private final NextCustomerMiMapper nextCustomerMiMapper;

    public NextCustomerMiService(NextCustomerMiRepository nextCustomerMiRepository, NextCustomerMiMapper nextCustomerMiMapper) {
        this.nextCustomerMiRepository = nextCustomerMiRepository;
        this.nextCustomerMiMapper = nextCustomerMiMapper;
    }

    /**
     * Save a nextCustomerMi.
     *
     * @param nextCustomerMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiDTO save(NextCustomerMiDTO nextCustomerMiDTO) {
        LOG.debug("Request to save NextCustomerMi : {}", nextCustomerMiDTO);
        NextCustomerMi nextCustomerMi = nextCustomerMiMapper.toEntity(nextCustomerMiDTO);
        nextCustomerMi = nextCustomerMiRepository.save(nextCustomerMi);
        return nextCustomerMiMapper.toDto(nextCustomerMi);
    }

    /**
     * Update a nextCustomerMi.
     *
     * @param nextCustomerMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiDTO update(NextCustomerMiDTO nextCustomerMiDTO) {
        LOG.debug("Request to update NextCustomerMi : {}", nextCustomerMiDTO);
        NextCustomerMi nextCustomerMi = nextCustomerMiMapper.toEntity(nextCustomerMiDTO);
        nextCustomerMi = nextCustomerMiRepository.save(nextCustomerMi);
        return nextCustomerMiMapper.toDto(nextCustomerMi);
    }

    /**
     * Partially update a nextCustomerMi.
     *
     * @param nextCustomerMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerMiDTO> partialUpdate(NextCustomerMiDTO nextCustomerMiDTO) {
        LOG.debug("Request to partially update NextCustomerMi : {}", nextCustomerMiDTO);

        return nextCustomerMiRepository
            .findById(nextCustomerMiDTO.getId())
            .map(existingNextCustomerMi -> {
                nextCustomerMiMapper.partialUpdate(existingNextCustomerMi, nextCustomerMiDTO);

                return existingNextCustomerMi;
            })
            .map(nextCustomerMiRepository::save)
            .map(nextCustomerMiMapper::toDto);
    }

    /**
     * Get one nextCustomerMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerMi : {}", id);
        return nextCustomerMiRepository.findById(id).map(nextCustomerMiMapper::toDto);
    }

    /**
     * Delete the nextCustomerMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerMi : {}", id);
        nextCustomerMiRepository.deleteById(id);
    }
}
