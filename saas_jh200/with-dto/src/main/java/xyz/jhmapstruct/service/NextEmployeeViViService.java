package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeViVi}.
 */
@Service
@Transactional
public class NextEmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViViService.class);

    private final NextEmployeeViViRepository nextEmployeeViViRepository;

    private final NextEmployeeViViMapper nextEmployeeViViMapper;

    public NextEmployeeViViService(NextEmployeeViViRepository nextEmployeeViViRepository, NextEmployeeViViMapper nextEmployeeViViMapper) {
        this.nextEmployeeViViRepository = nextEmployeeViViRepository;
        this.nextEmployeeViViMapper = nextEmployeeViViMapper;
    }

    /**
     * Save a nextEmployeeViVi.
     *
     * @param nextEmployeeViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViViDTO save(NextEmployeeViViDTO nextEmployeeViViDTO) {
        LOG.debug("Request to save NextEmployeeViVi : {}", nextEmployeeViViDTO);
        NextEmployeeViVi nextEmployeeViVi = nextEmployeeViViMapper.toEntity(nextEmployeeViViDTO);
        nextEmployeeViVi = nextEmployeeViViRepository.save(nextEmployeeViVi);
        return nextEmployeeViViMapper.toDto(nextEmployeeViVi);
    }

    /**
     * Update a nextEmployeeViVi.
     *
     * @param nextEmployeeViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViViDTO update(NextEmployeeViViDTO nextEmployeeViViDTO) {
        LOG.debug("Request to update NextEmployeeViVi : {}", nextEmployeeViViDTO);
        NextEmployeeViVi nextEmployeeViVi = nextEmployeeViViMapper.toEntity(nextEmployeeViViDTO);
        nextEmployeeViVi = nextEmployeeViViRepository.save(nextEmployeeViVi);
        return nextEmployeeViViMapper.toDto(nextEmployeeViVi);
    }

    /**
     * Partially update a nextEmployeeViVi.
     *
     * @param nextEmployeeViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeViViDTO> partialUpdate(NextEmployeeViViDTO nextEmployeeViViDTO) {
        LOG.debug("Request to partially update NextEmployeeViVi : {}", nextEmployeeViViDTO);

        return nextEmployeeViViRepository
            .findById(nextEmployeeViViDTO.getId())
            .map(existingNextEmployeeViVi -> {
                nextEmployeeViViMapper.partialUpdate(existingNextEmployeeViVi, nextEmployeeViViDTO);

                return existingNextEmployeeViVi;
            })
            .map(nextEmployeeViViRepository::save)
            .map(nextEmployeeViViMapper::toDto);
    }

    /**
     * Get one nextEmployeeViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeViVi : {}", id);
        return nextEmployeeViViRepository.findById(id).map(nextEmployeeViViMapper::toDto);
    }

    /**
     * Delete the nextEmployeeViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeViVi : {}", id);
        nextEmployeeViViRepository.deleteById(id);
    }
}
