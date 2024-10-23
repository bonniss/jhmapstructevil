package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
@Service
@Transactional
public class EmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViService.class);

    private final EmployeeViViRepository employeeViViRepository;

    public EmployeeViViService(EmployeeViViRepository employeeViViRepository) {
        this.employeeViViRepository = employeeViViRepository;
    }

    /**
     * Save a employeeViVi.
     *
     * @param employeeViVi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViVi save(EmployeeViVi employeeViVi) {
        LOG.debug("Request to save EmployeeViVi : {}", employeeViVi);
        return employeeViViRepository.save(employeeViVi);
    }

    /**
     * Update a employeeViVi.
     *
     * @param employeeViVi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeViVi update(EmployeeViVi employeeViVi) {
        LOG.debug("Request to update EmployeeViVi : {}", employeeViVi);
        return employeeViViRepository.save(employeeViVi);
    }

    /**
     * Partially update a employeeViVi.
     *
     * @param employeeViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeViVi> partialUpdate(EmployeeViVi employeeViVi) {
        LOG.debug("Request to partially update EmployeeViVi : {}", employeeViVi);

        return employeeViViRepository
            .findById(employeeViVi.getId())
            .map(existingEmployeeViVi -> {
                if (employeeViVi.getFirstName() != null) {
                    existingEmployeeViVi.setFirstName(employeeViVi.getFirstName());
                }
                if (employeeViVi.getLastName() != null) {
                    existingEmployeeViVi.setLastName(employeeViVi.getLastName());
                }
                if (employeeViVi.getEmail() != null) {
                    existingEmployeeViVi.setEmail(employeeViVi.getEmail());
                }
                if (employeeViVi.getHireDate() != null) {
                    existingEmployeeViVi.setHireDate(employeeViVi.getHireDate());
                }
                if (employeeViVi.getPosition() != null) {
                    existingEmployeeViVi.setPosition(employeeViVi.getPosition());
                }

                return existingEmployeeViVi;
            })
            .map(employeeViViRepository::save);
    }

    /**
     * Get one employeeViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeViVi> findOne(Long id) {
        LOG.debug("Request to get EmployeeViVi : {}", id);
        return employeeViViRepository.findById(id);
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
