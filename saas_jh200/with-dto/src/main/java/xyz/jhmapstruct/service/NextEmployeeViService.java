package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeVi;
import xyz.jhmapstruct.repository.NextEmployeeViRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeViDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeVi}.
 */
@Service
@Transactional
public class NextEmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViService.class);

    private final NextEmployeeViRepository nextEmployeeViRepository;

    private final NextEmployeeViMapper nextEmployeeViMapper;

    public NextEmployeeViService(NextEmployeeViRepository nextEmployeeViRepository, NextEmployeeViMapper nextEmployeeViMapper) {
        this.nextEmployeeViRepository = nextEmployeeViRepository;
        this.nextEmployeeViMapper = nextEmployeeViMapper;
    }

    /**
     * Save a nextEmployeeVi.
     *
     * @param nextEmployeeViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViDTO save(NextEmployeeViDTO nextEmployeeViDTO) {
        LOG.debug("Request to save NextEmployeeVi : {}", nextEmployeeViDTO);
        NextEmployeeVi nextEmployeeVi = nextEmployeeViMapper.toEntity(nextEmployeeViDTO);
        nextEmployeeVi = nextEmployeeViRepository.save(nextEmployeeVi);
        return nextEmployeeViMapper.toDto(nextEmployeeVi);
    }

    /**
     * Update a nextEmployeeVi.
     *
     * @param nextEmployeeViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViDTO update(NextEmployeeViDTO nextEmployeeViDTO) {
        LOG.debug("Request to update NextEmployeeVi : {}", nextEmployeeViDTO);
        NextEmployeeVi nextEmployeeVi = nextEmployeeViMapper.toEntity(nextEmployeeViDTO);
        nextEmployeeVi = nextEmployeeViRepository.save(nextEmployeeVi);
        return nextEmployeeViMapper.toDto(nextEmployeeVi);
    }

    /**
     * Partially update a nextEmployeeVi.
     *
     * @param nextEmployeeViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeViDTO> partialUpdate(NextEmployeeViDTO nextEmployeeViDTO) {
        LOG.debug("Request to partially update NextEmployeeVi : {}", nextEmployeeViDTO);

        return nextEmployeeViRepository
            .findById(nextEmployeeViDTO.getId())
            .map(existingNextEmployeeVi -> {
                nextEmployeeViMapper.partialUpdate(existingNextEmployeeVi, nextEmployeeViDTO);

                return existingNextEmployeeVi;
            })
            .map(nextEmployeeViRepository::save)
            .map(nextEmployeeViMapper::toDto);
    }

    /**
     * Get one nextEmployeeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeViDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeVi : {}", id);
        return nextEmployeeViRepository.findById(id).map(nextEmployeeViMapper::toDto);
    }

    /**
     * Delete the nextEmployeeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeVi : {}", id);
        nextEmployeeViRepository.deleteById(id);
    }
}
