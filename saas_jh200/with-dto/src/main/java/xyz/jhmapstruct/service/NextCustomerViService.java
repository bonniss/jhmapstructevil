package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.repository.NextCustomerViRepository;
import xyz.jhmapstruct.service.dto.NextCustomerViDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerVi}.
 */
@Service
@Transactional
public class NextCustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViService.class);

    private final NextCustomerViRepository nextCustomerViRepository;

    private final NextCustomerViMapper nextCustomerViMapper;

    public NextCustomerViService(NextCustomerViRepository nextCustomerViRepository, NextCustomerViMapper nextCustomerViMapper) {
        this.nextCustomerViRepository = nextCustomerViRepository;
        this.nextCustomerViMapper = nextCustomerViMapper;
    }

    /**
     * Save a nextCustomerVi.
     *
     * @param nextCustomerViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViDTO save(NextCustomerViDTO nextCustomerViDTO) {
        LOG.debug("Request to save NextCustomerVi : {}", nextCustomerViDTO);
        NextCustomerVi nextCustomerVi = nextCustomerViMapper.toEntity(nextCustomerViDTO);
        nextCustomerVi = nextCustomerViRepository.save(nextCustomerVi);
        return nextCustomerViMapper.toDto(nextCustomerVi);
    }

    /**
     * Update a nextCustomerVi.
     *
     * @param nextCustomerViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViDTO update(NextCustomerViDTO nextCustomerViDTO) {
        LOG.debug("Request to update NextCustomerVi : {}", nextCustomerViDTO);
        NextCustomerVi nextCustomerVi = nextCustomerViMapper.toEntity(nextCustomerViDTO);
        nextCustomerVi = nextCustomerViRepository.save(nextCustomerVi);
        return nextCustomerViMapper.toDto(nextCustomerVi);
    }

    /**
     * Partially update a nextCustomerVi.
     *
     * @param nextCustomerViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerViDTO> partialUpdate(NextCustomerViDTO nextCustomerViDTO) {
        LOG.debug("Request to partially update NextCustomerVi : {}", nextCustomerViDTO);

        return nextCustomerViRepository
            .findById(nextCustomerViDTO.getId())
            .map(existingNextCustomerVi -> {
                nextCustomerViMapper.partialUpdate(existingNextCustomerVi, nextCustomerViDTO);

                return existingNextCustomerVi;
            })
            .map(nextCustomerViRepository::save)
            .map(nextCustomerViMapper::toDto);
    }

    /**
     * Get one nextCustomerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerViDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerVi : {}", id);
        return nextCustomerViRepository.findById(id).map(nextCustomerViMapper::toDto);
    }

    /**
     * Delete the nextCustomerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerVi : {}", id);
        nextCustomerViRepository.deleteById(id);
    }
}
