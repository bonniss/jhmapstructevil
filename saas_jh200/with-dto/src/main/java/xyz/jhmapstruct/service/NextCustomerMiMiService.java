package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.repository.NextCustomerMiMiRepository;
import xyz.jhmapstruct.service.dto.NextCustomerMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerMiMi}.
 */
@Service
@Transactional
public class NextCustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiMiService.class);

    private final NextCustomerMiMiRepository nextCustomerMiMiRepository;

    private final NextCustomerMiMiMapper nextCustomerMiMiMapper;

    public NextCustomerMiMiService(NextCustomerMiMiRepository nextCustomerMiMiRepository, NextCustomerMiMiMapper nextCustomerMiMiMapper) {
        this.nextCustomerMiMiRepository = nextCustomerMiMiRepository;
        this.nextCustomerMiMiMapper = nextCustomerMiMiMapper;
    }

    /**
     * Save a nextCustomerMiMi.
     *
     * @param nextCustomerMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiMiDTO save(NextCustomerMiMiDTO nextCustomerMiMiDTO) {
        LOG.debug("Request to save NextCustomerMiMi : {}", nextCustomerMiMiDTO);
        NextCustomerMiMi nextCustomerMiMi = nextCustomerMiMiMapper.toEntity(nextCustomerMiMiDTO);
        nextCustomerMiMi = nextCustomerMiMiRepository.save(nextCustomerMiMi);
        return nextCustomerMiMiMapper.toDto(nextCustomerMiMi);
    }

    /**
     * Update a nextCustomerMiMi.
     *
     * @param nextCustomerMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiMiDTO update(NextCustomerMiMiDTO nextCustomerMiMiDTO) {
        LOG.debug("Request to update NextCustomerMiMi : {}", nextCustomerMiMiDTO);
        NextCustomerMiMi nextCustomerMiMi = nextCustomerMiMiMapper.toEntity(nextCustomerMiMiDTO);
        nextCustomerMiMi = nextCustomerMiMiRepository.save(nextCustomerMiMi);
        return nextCustomerMiMiMapper.toDto(nextCustomerMiMi);
    }

    /**
     * Partially update a nextCustomerMiMi.
     *
     * @param nextCustomerMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerMiMiDTO> partialUpdate(NextCustomerMiMiDTO nextCustomerMiMiDTO) {
        LOG.debug("Request to partially update NextCustomerMiMi : {}", nextCustomerMiMiDTO);

        return nextCustomerMiMiRepository
            .findById(nextCustomerMiMiDTO.getId())
            .map(existingNextCustomerMiMi -> {
                nextCustomerMiMiMapper.partialUpdate(existingNextCustomerMiMi, nextCustomerMiMiDTO);

                return existingNextCustomerMiMi;
            })
            .map(nextCustomerMiMiRepository::save)
            .map(nextCustomerMiMiMapper::toDto);
    }

    /**
     * Get one nextCustomerMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerMiMi : {}", id);
        return nextCustomerMiMiRepository.findById(id).map(nextCustomerMiMiMapper::toDto);
    }

    /**
     * Delete the nextCustomerMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerMiMi : {}", id);
        nextCustomerMiMiRepository.deleteById(id);
    }
}
