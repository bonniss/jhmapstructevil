package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
@Service
@Transactional
public class EmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViService.class);

    private final EmployeeViViRepository employeeViViRepository;

    private final EmployeeViViMapper employeeViViMapper;

    public EmployeeViViService(EmployeeViViRepository employeeViViRepository, EmployeeViViMapper employeeViViMapper) {
        this.employeeViViRepository = employeeViViRepository;
        this.employeeViViMapper = employeeViViMapper;
    }

    /**
     * Save a employeeViVi.
     *
     * @param employeeViViDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViViDTO save(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to save EmployeeViVi : {}", employeeViViDTO);
        EmployeeViVi employeeViVi = employeeViViMapper.toEntity(employeeViViDTO);
        employeeViVi = employeeViViRepository.save(employeeViVi);
        return employeeViViMapper.toDto(employeeViVi);
    }

    /**
     * Update a employeeViVi.
     *
     * @param employeeViViDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViViDTO update(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to update EmployeeViVi : {}", employeeViViDTO);
        EmployeeViVi employeeViVi = employeeViViMapper.toEntity(employeeViViDTO);
        employeeViVi = employeeViViRepository.save(employeeViVi);
        return employeeViViMapper.toDto(employeeViVi);
    }

    /**
     * Partially update a employeeViVi.
     *
     * @param employeeViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeViViDTO> partialUpdate(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to partially update EmployeeViVi : {}", employeeViViDTO);

        return employeeViViRepository
            .findById(employeeViViDTO.getId())
            .map(existingEmployeeViVi -> {
                employeeViViMapper.partialUpdate(existingEmployeeViVi, employeeViViDTO);

                return existingEmployeeViVi;
            })
            .map(employeeViViRepository::save)
            .map(employeeViViMapper::toDto);
    }

    /**
     * Get one employeeViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeViViDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeViVi : {}", id);
        return employeeViViRepository.findById(id).map(employeeViViMapper::toDto);
    }

    /**
     * Delete the employeeViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeViVi : {}", id);
        employeeViViRepository.deleteById(id);
    }
}
