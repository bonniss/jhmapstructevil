package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeBeta;
import xyz.jhmapstruct.repository.EmployeeBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeBeta}.
 */
@Service
@Transactional
public class EmployeeBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBetaService.class);

    private final EmployeeBetaRepository employeeBetaRepository;

    public EmployeeBetaService(EmployeeBetaRepository employeeBetaRepository) {
        this.employeeBetaRepository = employeeBetaRepository;
    }

    /**
     * Save a employeeBeta.
     *
     * @param employeeBeta the entity to save.
     * @return the persisted entity.
     */
    public EmployeeBeta save(EmployeeBeta employeeBeta) {
        LOG.debug("Request to save EmployeeBeta : {}", employeeBeta);
        return employeeBetaRepository.save(employeeBeta);
    }

    /**
     * Update a employeeBeta.
     *
     * @param employeeBeta the entity to save.
     * @return the persisted entity.
     */
    public EmployeeBeta update(EmployeeBeta employeeBeta) {
        LOG.debug("Request to update EmployeeBeta : {}", employeeBeta);
        return employeeBetaRepository.save(employeeBeta);
    }

    /**
     * Partially update a employeeBeta.
     *
     * @param employeeBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeBeta> partialUpdate(EmployeeBeta employeeBeta) {
        LOG.debug("Request to partially update EmployeeBeta : {}", employeeBeta);

        return employeeBetaRepository
            .findById(employeeBeta.getId())
            .map(existingEmployeeBeta -> {
                if (employeeBeta.getFirstName() != null) {
                    existingEmployeeBeta.setFirstName(employeeBeta.getFirstName());
                }
                if (employeeBeta.getLastName() != null) {
                    existingEmployeeBeta.setLastName(employeeBeta.getLastName());
                }
                if (employeeBeta.getEmail() != null) {
                    existingEmployeeBeta.setEmail(employeeBeta.getEmail());
                }
                if (employeeBeta.getHireDate() != null) {
                    existingEmployeeBeta.setHireDate(employeeBeta.getHireDate());
                }
                if (employeeBeta.getPosition() != null) {
                    existingEmployeeBeta.setPosition(employeeBeta.getPosition());
                }

                return existingEmployeeBeta;
            })
            .map(employeeBetaRepository::save);
    }

    /**
     * Get one employeeBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeBeta> findOne(Long id) {
        LOG.debug("Request to get EmployeeBeta : {}", id);
        return employeeBetaRepository.findById(id);
    }

    /**
     * Delete the employeeBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeBeta : {}", id);
        employeeBetaRepository.deleteById(id);
    }
}
