package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
@Service
@Transactional
public class EmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiMiService.class);

    private final EmployeeMiMiRepository employeeMiMiRepository;

    public EmployeeMiMiService(EmployeeMiMiRepository employeeMiMiRepository) {
        this.employeeMiMiRepository = employeeMiMiRepository;
    }

    /**
     * Save a employeeMiMi.
     *
     * @param employeeMiMi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiMi save(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to save EmployeeMiMi : {}", employeeMiMi);
        return employeeMiMiRepository.save(employeeMiMi);
    }

    /**
     * Update a employeeMiMi.
     *
     * @param employeeMiMi the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiMi update(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to update EmployeeMiMi : {}", employeeMiMi);
        return employeeMiMiRepository.save(employeeMiMi);
    }

    /**
     * Partially update a employeeMiMi.
     *
     * @param employeeMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeMiMi> partialUpdate(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to partially update EmployeeMiMi : {}", employeeMiMi);

        return employeeMiMiRepository
            .findById(employeeMiMi.getId())
            .map(existingEmployeeMiMi -> {
                if (employeeMiMi.getFirstName() != null) {
                    existingEmployeeMiMi.setFirstName(employeeMiMi.getFirstName());
                }
                if (employeeMiMi.getLastName() != null) {
                    existingEmployeeMiMi.setLastName(employeeMiMi.getLastName());
                }
                if (employeeMiMi.getEmail() != null) {
                    existingEmployeeMiMi.setEmail(employeeMiMi.getEmail());
                }
                if (employeeMiMi.getHireDate() != null) {
                    existingEmployeeMiMi.setHireDate(employeeMiMi.getHireDate());
                }
                if (employeeMiMi.getPosition() != null) {
                    existingEmployeeMiMi.setPosition(employeeMiMi.getPosition());
                }

                return existingEmployeeMiMi;
            })
            .map(employeeMiMiRepository::save);
    }

    /**
     * Get one employeeMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeMiMi> findOne(Long id) {
        LOG.debug("Request to get EmployeeMiMi : {}", id);
        return employeeMiMiRepository.findById(id);
    }

    /**
     * Delete the employeeMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMiMi : {}", id);
        employeeMiMiRepository.deleteById(id);
    }
}
