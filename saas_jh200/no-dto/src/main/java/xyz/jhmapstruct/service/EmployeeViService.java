package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
@Service
@Transactional
public class EmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViService.class);

    private final EmployeeViRepository employeeViRepository;

    public EmployeeViService(EmployeeViRepository employeeViRepository) {
        this.employeeViRepository = employeeViRepository;
    }

    /**
     * Save a employeeVi.
     *
     * @param employeeVi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeVi save(EmployeeVi employeeVi) {
        LOG.debug("Request to save EmployeeVi : {}", employeeVi);
        return employeeViRepository.save(employeeVi);
    }

    /**
     * Update a employeeVi.
     *
     * @param employeeVi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeVi update(EmployeeVi employeeVi) {
        LOG.debug("Request to update EmployeeVi : {}", employeeVi);
        return employeeViRepository.save(employeeVi);
    }

    /**
     * Partially update a employeeVi.
     *
     * @param employeeVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeVi> partialUpdate(EmployeeVi employeeVi) {
        LOG.debug("Request to partially update EmployeeVi : {}", employeeVi);

        return employeeViRepository
            .findById(employeeVi.getId())
            .map(existingEmployeeVi -> {
                if (employeeVi.getFirstName() != null) {
                    existingEmployeeVi.setFirstName(employeeVi.getFirstName());
                }
                if (employeeVi.getLastName() != null) {
                    existingEmployeeVi.setLastName(employeeVi.getLastName());
                }
                if (employeeVi.getEmail() != null) {
                    existingEmployeeVi.setEmail(employeeVi.getEmail());
                }
                if (employeeVi.getHireDate() != null) {
                    existingEmployeeVi.setHireDate(employeeVi.getHireDate());
                }
                if (employeeVi.getPosition() != null) {
                    existingEmployeeVi.setPosition(employeeVi.getPosition());
                }

                return existingEmployeeVi;
            })
            .map(employeeViRepository::save);
    }

    /**
     * Get one employeeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeVi> findOne(Long id) {
        LOG.debug("Request to get EmployeeVi : {}", id);
        return employeeViRepository.findById(id);
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
