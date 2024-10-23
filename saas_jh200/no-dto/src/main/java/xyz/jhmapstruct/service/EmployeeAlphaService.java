package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeAlpha;
import xyz.jhmapstruct.repository.EmployeeAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeAlpha}.
 */
@Service
@Transactional
public class EmployeeAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAlphaService.class);

    private final EmployeeAlphaRepository employeeAlphaRepository;

    public EmployeeAlphaService(EmployeeAlphaRepository employeeAlphaRepository) {
        this.employeeAlphaRepository = employeeAlphaRepository;
    }

    /**
     * Save a employeeAlpha.
     *
     * @param employeeAlpha the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAlpha save(EmployeeAlpha employeeAlpha) {
        LOG.debug("Request to save EmployeeAlpha : {}", employeeAlpha);
        return employeeAlphaRepository.save(employeeAlpha);
    }

    /**
     * Update a employeeAlpha.
     *
     * @param employeeAlpha the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAlpha update(EmployeeAlpha employeeAlpha) {
        LOG.debug("Request to update EmployeeAlpha : {}", employeeAlpha);
        return employeeAlphaRepository.save(employeeAlpha);
    }

    /**
     * Partially update a employeeAlpha.
     *
     * @param employeeAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeAlpha> partialUpdate(EmployeeAlpha employeeAlpha) {
        LOG.debug("Request to partially update EmployeeAlpha : {}", employeeAlpha);

        return employeeAlphaRepository
            .findById(employeeAlpha.getId())
            .map(existingEmployeeAlpha -> {
                if (employeeAlpha.getFirstName() != null) {
                    existingEmployeeAlpha.setFirstName(employeeAlpha.getFirstName());
                }
                if (employeeAlpha.getLastName() != null) {
                    existingEmployeeAlpha.setLastName(employeeAlpha.getLastName());
                }
                if (employeeAlpha.getEmail() != null) {
                    existingEmployeeAlpha.setEmail(employeeAlpha.getEmail());
                }
                if (employeeAlpha.getHireDate() != null) {
                    existingEmployeeAlpha.setHireDate(employeeAlpha.getHireDate());
                }
                if (employeeAlpha.getPosition() != null) {
                    existingEmployeeAlpha.setPosition(employeeAlpha.getPosition());
                }

                return existingEmployeeAlpha;
            })
            .map(employeeAlphaRepository::save);
    }

    /**
     * Get one employeeAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeAlpha> findOne(Long id) {
        LOG.debug("Request to get EmployeeAlpha : {}", id);
        return employeeAlphaRepository.findById(id);
    }

    /**
     * Delete the employeeAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeAlpha : {}", id);
        employeeAlphaRepository.deleteById(id);
    }
}
