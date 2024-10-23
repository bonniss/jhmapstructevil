package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeTheta;
import xyz.jhmapstruct.repository.EmployeeThetaRepository;
import xyz.jhmapstruct.service.dto.EmployeeThetaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeTheta}.
 */
@Service
@Transactional
public class EmployeeThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeThetaService.class);

    private final EmployeeThetaRepository employeeThetaRepository;

    private final EmployeeThetaMapper employeeThetaMapper;

    public EmployeeThetaService(EmployeeThetaRepository employeeThetaRepository, EmployeeThetaMapper employeeThetaMapper) {
        this.employeeThetaRepository = employeeThetaRepository;
        this.employeeThetaMapper = employeeThetaMapper;
    }

    /**
     * Save a employeeTheta.
     *
     * @param employeeThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeThetaDTO save(EmployeeThetaDTO employeeThetaDTO) {
        LOG.debug("Request to save EmployeeTheta : {}", employeeThetaDTO);
        EmployeeTheta employeeTheta = employeeThetaMapper.toEntity(employeeThetaDTO);
        employeeTheta = employeeThetaRepository.save(employeeTheta);
        return employeeThetaMapper.toDto(employeeTheta);
    }

    /**
     * Update a employeeTheta.
     *
     * @param employeeThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeThetaDTO update(EmployeeThetaDTO employeeThetaDTO) {
        LOG.debug("Request to update EmployeeTheta : {}", employeeThetaDTO);
        EmployeeTheta employeeTheta = employeeThetaMapper.toEntity(employeeThetaDTO);
        employeeTheta = employeeThetaRepository.save(employeeTheta);
        return employeeThetaMapper.toDto(employeeTheta);
    }

    /**
     * Partially update a employeeTheta.
     *
     * @param employeeThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeThetaDTO> partialUpdate(EmployeeThetaDTO employeeThetaDTO) {
        LOG.debug("Request to partially update EmployeeTheta : {}", employeeThetaDTO);

        return employeeThetaRepository
            .findById(employeeThetaDTO.getId())
            .map(existingEmployeeTheta -> {
                employeeThetaMapper.partialUpdate(existingEmployeeTheta, employeeThetaDTO);

                return existingEmployeeTheta;
            })
            .map(employeeThetaRepository::save)
            .map(employeeThetaMapper::toDto);
    }

    /**
     * Get one employeeTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeThetaDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeTheta : {}", id);
        return employeeThetaRepository.findById(id).map(employeeThetaMapper::toDto);
    }

    /**
     * Delete the employeeTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeTheta : {}", id);
        employeeThetaRepository.deleteById(id);
    }
}
