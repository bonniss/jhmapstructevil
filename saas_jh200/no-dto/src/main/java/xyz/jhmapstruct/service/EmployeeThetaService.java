package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeTheta;
import xyz.jhmapstruct.repository.EmployeeThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeTheta}.
 */
@Service
@Transactional
public class EmployeeThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeThetaService.class);

    private final EmployeeThetaRepository employeeThetaRepository;

    public EmployeeThetaService(EmployeeThetaRepository employeeThetaRepository) {
        this.employeeThetaRepository = employeeThetaRepository;
    }

    /**
     * Save a employeeTheta.
     *
     * @param employeeTheta the entity to save.
     * @return the persisted entity.
     */
    public EmployeeTheta save(EmployeeTheta employeeTheta) {
        LOG.debug("Request to save EmployeeTheta : {}", employeeTheta);
        return employeeThetaRepository.save(employeeTheta);
    }

    /**
     * Update a employeeTheta.
     *
     * @param employeeTheta the entity to save.
     * @return the persisted entity.
     */
    public EmployeeTheta update(EmployeeTheta employeeTheta) {
        LOG.debug("Request to update EmployeeTheta : {}", employeeTheta);
        return employeeThetaRepository.save(employeeTheta);
    }

    /**
     * Partially update a employeeTheta.
     *
     * @param employeeTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeTheta> partialUpdate(EmployeeTheta employeeTheta) {
        LOG.debug("Request to partially update EmployeeTheta : {}", employeeTheta);

        return employeeThetaRepository
            .findById(employeeTheta.getId())
            .map(existingEmployeeTheta -> {
                if (employeeTheta.getFirstName() != null) {
                    existingEmployeeTheta.setFirstName(employeeTheta.getFirstName());
                }
                if (employeeTheta.getLastName() != null) {
                    existingEmployeeTheta.setLastName(employeeTheta.getLastName());
                }
                if (employeeTheta.getEmail() != null) {
                    existingEmployeeTheta.setEmail(employeeTheta.getEmail());
                }
                if (employeeTheta.getHireDate() != null) {
                    existingEmployeeTheta.setHireDate(employeeTheta.getHireDate());
                }
                if (employeeTheta.getPosition() != null) {
                    existingEmployeeTheta.setPosition(employeeTheta.getPosition());
                }

                return existingEmployeeTheta;
            })
            .map(employeeThetaRepository::save);
    }

    /**
     * Get one employeeTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeTheta> findOne(Long id) {
        LOG.debug("Request to get EmployeeTheta : {}", id);
        return employeeThetaRepository.findById(id);
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
