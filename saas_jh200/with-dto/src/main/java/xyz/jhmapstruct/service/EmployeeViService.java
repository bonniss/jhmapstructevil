package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
@Service
@Transactional
public class EmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViService.class);

    private final EmployeeViRepository employeeViRepository;

    private final EmployeeViMapper employeeViMapper;

    public EmployeeViService(EmployeeViRepository employeeViRepository, EmployeeViMapper employeeViMapper) {
        this.employeeViRepository = employeeViRepository;
        this.employeeViMapper = employeeViMapper;
    }

    /**
     * Save a employeeVi.
     *
     * @param employeeViDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViDTO save(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to save EmployeeVi : {}", employeeViDTO);
        EmployeeVi employeeVi = employeeViMapper.toEntity(employeeViDTO);
        employeeVi = employeeViRepository.save(employeeVi);
        return employeeViMapper.toDto(employeeVi);
    }

    /**
     * Update a employeeVi.
     *
     * @param employeeViDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViDTO update(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to update EmployeeVi : {}", employeeViDTO);
        EmployeeVi employeeVi = employeeViMapper.toEntity(employeeViDTO);
        employeeVi = employeeViRepository.save(employeeVi);
        return employeeViMapper.toDto(employeeVi);
    }

    /**
     * Partially update a employeeVi.
     *
     * @param employeeViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeViDTO> partialUpdate(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to partially update EmployeeVi : {}", employeeViDTO);

        return employeeViRepository
            .findById(employeeViDTO.getId())
            .map(existingEmployeeVi -> {
                employeeViMapper.partialUpdate(existingEmployeeVi, employeeViDTO);

                return existingEmployeeVi;
            })
            .map(employeeViRepository::save)
            .map(employeeViMapper::toDto);
    }

    /**
     * Get one employeeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeViDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeVi : {}", id);
        return employeeViRepository.findById(id).map(employeeViMapper::toDto);
    }

    /**
     * Delete the employeeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeVi : {}", id);
        employeeViRepository.deleteById(id);
    }
}
