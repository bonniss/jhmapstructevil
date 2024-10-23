package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
@Service
@Transactional
public class EmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiService.class);

    private final EmployeeMiRepository employeeMiRepository;

    public EmployeeMiService(EmployeeMiRepository employeeMiRepository) {
        this.employeeMiRepository = employeeMiRepository;
    }

    /**
     * Save a employeeMi.
     *
     * @param employeeMi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMi save(EmployeeMi employeeMi) {
        LOG.debug("Request to save EmployeeMi : {}", employeeMi);
        return employeeMiRepository.save(employeeMi);
    }

    /**
     * Update a employeeMi.
     *
     * @param employeeMi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMi update(EmployeeMi employeeMi) {
        LOG.debug("Request to update EmployeeMi : {}", employeeMi);
        return employeeMiRepository.save(employeeMi);
    }

    /**
     * Partially update a employeeMi.
     *
     * @param employeeMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeMi> partialUpdate(EmployeeMi employeeMi) {
        LOG.debug("Request to partially update EmployeeMi : {}", employeeMi);

        return employeeMiRepository
            .findById(employeeMi.getId())
            .map(existingEmployeeMi -> {
                if (employeeMi.getFirstName() != null) {
                    existingEmployeeMi.setFirstName(employeeMi.getFirstName());
                }
                if (employeeMi.getLastName() != null) {
                    existingEmployeeMi.setLastName(employeeMi.getLastName());
                }
                if (employeeMi.getEmail() != null) {
                    existingEmployeeMi.setEmail(employeeMi.getEmail());
                }
                if (employeeMi.getHireDate() != null) {
                    existingEmployeeMi.setHireDate(employeeMi.getHireDate());
                }
                if (employeeMi.getPosition() != null) {
                    existingEmployeeMi.setPosition(employeeMi.getPosition());
                }

                return existingEmployeeMi;
            })
            .map(employeeMiRepository::save);
    }

    /**
     * Get one employeeMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeMi> findOne(Long id) {
        LOG.debug("Request to get EmployeeMi : {}", id);
        return employeeMiRepository.findById(id);
    }

    /**
     * Delete the employeeMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMi : {}", id);
        employeeMiRepository.deleteById(id);
    }
}
