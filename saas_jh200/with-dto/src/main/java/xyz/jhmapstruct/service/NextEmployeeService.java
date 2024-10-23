package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.repository.NextEmployeeRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployee}.
 */
@Service
@Transactional
public class NextEmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeService.class);

    private final NextEmployeeRepository nextEmployeeRepository;

    private final NextEmployeeMapper nextEmployeeMapper;

    public NextEmployeeService(NextEmployeeRepository nextEmployeeRepository, NextEmployeeMapper nextEmployeeMapper) {
        this.nextEmployeeRepository = nextEmployeeRepository;
        this.nextEmployeeMapper = nextEmployeeMapper;
    }

    /**
     * Save a nextEmployee.
     *
     * @param nextEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeDTO save(NextEmployeeDTO nextEmployeeDTO) {
        LOG.debug("Request to save NextEmployee : {}", nextEmployeeDTO);
        NextEmployee nextEmployee = nextEmployeeMapper.toEntity(nextEmployeeDTO);
        nextEmployee = nextEmployeeRepository.save(nextEmployee);
        return nextEmployeeMapper.toDto(nextEmployee);
    }

    /**
     * Update a nextEmployee.
     *
     * @param nextEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeDTO update(NextEmployeeDTO nextEmployeeDTO) {
        LOG.debug("Request to update NextEmployee : {}", nextEmployeeDTO);
        NextEmployee nextEmployee = nextEmployeeMapper.toEntity(nextEmployeeDTO);
        nextEmployee = nextEmployeeRepository.save(nextEmployee);
        return nextEmployeeMapper.toDto(nextEmployee);
    }

    /**
     * Partially update a nextEmployee.
     *
     * @param nextEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeDTO> partialUpdate(NextEmployeeDTO nextEmployeeDTO) {
        LOG.debug("Request to partially update NextEmployee : {}", nextEmployeeDTO);

        return nextEmployeeRepository
            .findById(nextEmployeeDTO.getId())
            .map(existingNextEmployee -> {
                nextEmployeeMapper.partialUpdate(existingNextEmployee, nextEmployeeDTO);

                return existingNextEmployee;
            })
            .map(nextEmployeeRepository::save)
            .map(nextEmployeeMapper::toDto);
    }

    /**
     * Get one nextEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployee : {}", id);
        return nextEmployeeRepository.findById(id).map(nextEmployeeMapper::toDto);
    }

    /**
     * Delete the nextEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployee : {}", id);
        nextEmployeeRepository.deleteById(id);
    }
}
