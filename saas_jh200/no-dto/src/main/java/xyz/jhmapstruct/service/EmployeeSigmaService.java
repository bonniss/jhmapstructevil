package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeSigma;
import xyz.jhmapstruct.repository.EmployeeSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeSigma}.
 */
@Service
@Transactional
public class EmployeeSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeSigmaService.class);

    private final EmployeeSigmaRepository employeeSigmaRepository;

    public EmployeeSigmaService(EmployeeSigmaRepository employeeSigmaRepository) {
        this.employeeSigmaRepository = employeeSigmaRepository;
    }

    /**
     * Save a employeeSigma.
     *
     * @param employeeSigma the entity to save.
     * @return the persisted entity.
     */
    public EmployeeSigma save(EmployeeSigma employeeSigma) {
        LOG.debug("Request to save EmployeeSigma : {}", employeeSigma);
        return employeeSigmaRepository.save(employeeSigma);
    }

    /**
     * Update a employeeSigma.
     *
     * @param employeeSigma the entity to save.
     * @return the persisted entity.
     */
    public EmployeeSigma update(EmployeeSigma employeeSigma) {
        LOG.debug("Request to update EmployeeSigma : {}", employeeSigma);
        return employeeSigmaRepository.save(employeeSigma);
    }

    /**
     * Partially update a employeeSigma.
     *
     * @param employeeSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeSigma> partialUpdate(EmployeeSigma employeeSigma) {
        LOG.debug("Request to partially update EmployeeSigma : {}", employeeSigma);

        return employeeSigmaRepository
            .findById(employeeSigma.getId())
            .map(existingEmployeeSigma -> {
                if (employeeSigma.getFirstName() != null) {
                    existingEmployeeSigma.setFirstName(employeeSigma.getFirstName());
                }
                if (employeeSigma.getLastName() != null) {
                    existingEmployeeSigma.setLastName(employeeSigma.getLastName());
                }
                if (employeeSigma.getEmail() != null) {
                    existingEmployeeSigma.setEmail(employeeSigma.getEmail());
                }
                if (employeeSigma.getHireDate() != null) {
                    existingEmployeeSigma.setHireDate(employeeSigma.getHireDate());
                }
                if (employeeSigma.getPosition() != null) {
                    existingEmployeeSigma.setPosition(employeeSigma.getPosition());
                }

                return existingEmployeeSigma;
            })
            .map(employeeSigmaRepository::save);
    }

    /**
     * Get one employeeSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeSigma> findOne(Long id) {
        LOG.debug("Request to get EmployeeSigma : {}", id);
        return employeeSigmaRepository.findById(id);
    }

    /**
     * Delete the employeeSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeSigma : {}", id);
        employeeSigmaRepository.deleteById(id);
    }
}
