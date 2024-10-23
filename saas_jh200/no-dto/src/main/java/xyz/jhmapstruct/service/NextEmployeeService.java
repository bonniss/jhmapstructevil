package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.repository.NextEmployeeRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployee}.
 */
@Service
@Transactional
public class NextEmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeService.class);

    private final NextEmployeeRepository nextEmployeeRepository;

    public NextEmployeeService(NextEmployeeRepository nextEmployeeRepository) {
        this.nextEmployeeRepository = nextEmployeeRepository;
    }

    /**
     * Save a nextEmployee.
     *
     * @param nextEmployee the entity to save.
     * @return the persisted entity.
     */
    public NextEmployee save(NextEmployee nextEmployee) {
        LOG.debug("Request to save NextEmployee : {}", nextEmployee);
        return nextEmployeeRepository.save(nextEmployee);
    }

    /**
     * Update a nextEmployee.
     *
     * @param nextEmployee the entity to save.
     * @return the persisted entity.
     */
    public NextEmployee update(NextEmployee nextEmployee) {
        LOG.debug("Request to update NextEmployee : {}", nextEmployee);
        return nextEmployeeRepository.save(nextEmployee);
    }

    /**
     * Partially update a nextEmployee.
     *
     * @param nextEmployee the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployee> partialUpdate(NextEmployee nextEmployee) {
        LOG.debug("Request to partially update NextEmployee : {}", nextEmployee);

        return nextEmployeeRepository
            .findById(nextEmployee.getId())
            .map(existingNextEmployee -> {
                if (nextEmployee.getFirstName() != null) {
                    existingNextEmployee.setFirstName(nextEmployee.getFirstName());
                }
                if (nextEmployee.getLastName() != null) {
                    existingNextEmployee.setLastName(nextEmployee.getLastName());
                }
                if (nextEmployee.getEmail() != null) {
                    existingNextEmployee.setEmail(nextEmployee.getEmail());
                }
                if (nextEmployee.getHireDate() != null) {
                    existingNextEmployee.setHireDate(nextEmployee.getHireDate());
                }
                if (nextEmployee.getPosition() != null) {
                    existingNextEmployee.setPosition(nextEmployee.getPosition());
                }

                return existingNextEmployee;
            })
            .map(nextEmployeeRepository::save);
    }

    /**
     * Get one nextEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployee> findOne(Long id) {
        LOG.debug("Request to get NextEmployee : {}", id);
        return nextEmployeeRepository.findById(id);
    }

    /**
     * Delete the nextEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployee : {}", id);
        nextEmployeeRepository.deleteById(id);
    }
}
