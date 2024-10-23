package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeGamma;
import xyz.jhmapstruct.repository.EmployeeGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeGamma}.
 */
@Service
@Transactional
public class EmployeeGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeGammaService.class);

    private final EmployeeGammaRepository employeeGammaRepository;

    public EmployeeGammaService(EmployeeGammaRepository employeeGammaRepository) {
        this.employeeGammaRepository = employeeGammaRepository;
    }

    /**
     * Save a employeeGamma.
     *
     * @param employeeGamma the entity to save.
     * @return the persisted entity.
     */
    public EmployeeGamma save(EmployeeGamma employeeGamma) {
        LOG.debug("Request to save EmployeeGamma : {}", employeeGamma);
        return employeeGammaRepository.save(employeeGamma);
    }

    /**
     * Update a employeeGamma.
     *
     * @param employeeGamma the entity to save.
     * @return the persisted entity.
     */
    public EmployeeGamma update(EmployeeGamma employeeGamma) {
        LOG.debug("Request to update EmployeeGamma : {}", employeeGamma);
        return employeeGammaRepository.save(employeeGamma);
    }

    /**
     * Partially update a employeeGamma.
     *
     * @param employeeGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeGamma> partialUpdate(EmployeeGamma employeeGamma) {
        LOG.debug("Request to partially update EmployeeGamma : {}", employeeGamma);

        return employeeGammaRepository
            .findById(employeeGamma.getId())
            .map(existingEmployeeGamma -> {
                if (employeeGamma.getFirstName() != null) {
                    existingEmployeeGamma.setFirstName(employeeGamma.getFirstName());
                }
                if (employeeGamma.getLastName() != null) {
                    existingEmployeeGamma.setLastName(employeeGamma.getLastName());
                }
                if (employeeGamma.getEmail() != null) {
                    existingEmployeeGamma.setEmail(employeeGamma.getEmail());
                }
                if (employeeGamma.getHireDate() != null) {
                    existingEmployeeGamma.setHireDate(employeeGamma.getHireDate());
                }
                if (employeeGamma.getPosition() != null) {
                    existingEmployeeGamma.setPosition(employeeGamma.getPosition());
                }

                return existingEmployeeGamma;
            })
            .map(employeeGammaRepository::save);
    }

    /**
     * Get one employeeGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeGamma> findOne(Long id) {
        LOG.debug("Request to get EmployeeGamma : {}", id);
        return employeeGammaRepository.findById(id);
    }

    /**
     * Delete the employeeGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeGamma : {}", id);
        employeeGammaRepository.deleteById(id);
    }
}
