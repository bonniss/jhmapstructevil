package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.repository.NextCustomerViViRepository;
import xyz.jhmapstruct.service.dto.NextCustomerViViDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerViVi}.
 */
@Service
@Transactional
public class NextCustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViViService.class);

    private final NextCustomerViViRepository nextCustomerViViRepository;

    private final NextCustomerViViMapper nextCustomerViViMapper;

    public NextCustomerViViService(NextCustomerViViRepository nextCustomerViViRepository, NextCustomerViViMapper nextCustomerViViMapper) {
        this.nextCustomerViViRepository = nextCustomerViViRepository;
        this.nextCustomerViViMapper = nextCustomerViViMapper;
    }

    /**
     * Save a nextCustomerViVi.
     *
     * @param nextCustomerViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViViDTO save(NextCustomerViViDTO nextCustomerViViDTO) {
        LOG.debug("Request to save NextCustomerViVi : {}", nextCustomerViViDTO);
        NextCustomerViVi nextCustomerViVi = nextCustomerViViMapper.toEntity(nextCustomerViViDTO);
        nextCustomerViVi = nextCustomerViViRepository.save(nextCustomerViVi);
        return nextCustomerViViMapper.toDto(nextCustomerViVi);
    }

    /**
     * Update a nextCustomerViVi.
     *
     * @param nextCustomerViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViViDTO update(NextCustomerViViDTO nextCustomerViViDTO) {
        LOG.debug("Request to update NextCustomerViVi : {}", nextCustomerViViDTO);
        NextCustomerViVi nextCustomerViVi = nextCustomerViViMapper.toEntity(nextCustomerViViDTO);
        nextCustomerViVi = nextCustomerViViRepository.save(nextCustomerViVi);
        return nextCustomerViViMapper.toDto(nextCustomerViVi);
    }

    /**
     * Partially update a nextCustomerViVi.
     *
     * @param nextCustomerViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerViViDTO> partialUpdate(NextCustomerViViDTO nextCustomerViViDTO) {
        LOG.debug("Request to partially update NextCustomerViVi : {}", nextCustomerViViDTO);

        return nextCustomerViViRepository
            .findById(nextCustomerViViDTO.getId())
            .map(existingNextCustomerViVi -> {
                nextCustomerViViMapper.partialUpdate(existingNextCustomerViVi, nextCustomerViViDTO);

                return existingNextCustomerViVi;
            })
            .map(nextCustomerViViRepository::save)
            .map(nextCustomerViViMapper::toDto);
    }

    /**
     * Get one nextCustomerViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerViVi : {}", id);
        return nextCustomerViViRepository.findById(id).map(nextCustomerViViMapper::toDto);
    }

    /**
     * Delete the nextCustomerViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerViVi : {}", id);
        nextCustomerViViRepository.deleteById(id);
    }
}
