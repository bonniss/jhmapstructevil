package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeTheta;
import xyz.jhmapstruct.repository.NextEmployeeThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeTheta}.
 */
@Service
@Transactional
public class NextEmployeeThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeThetaService.class);

    private final NextEmployeeThetaRepository nextEmployeeThetaRepository;

    public NextEmployeeThetaService(NextEmployeeThetaRepository nextEmployeeThetaRepository) {
        this.nextEmployeeThetaRepository = nextEmployeeThetaRepository;
    }

    /**
     * Save a nextEmployeeTheta.
     *
     * @param nextEmployeeTheta the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeTheta save(NextEmployeeTheta nextEmployeeTheta) {
        LOG.debug("Request to save NextEmployeeTheta : {}", nextEmployeeTheta);
        return nextEmployeeThetaRepository.save(nextEmployeeTheta);
    }

    /**
     * Update a nextEmployeeTheta.
     *
     * @param nextEmployeeTheta the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeTheta update(NextEmployeeTheta nextEmployeeTheta) {
        LOG.debug("Request to update NextEmployeeTheta : {}", nextEmployeeTheta);
        return nextEmployeeThetaRepository.save(nextEmployeeTheta);
    }

    /**
     * Partially update a nextEmployeeTheta.
     *
     * @param nextEmployeeTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeTheta> partialUpdate(NextEmployeeTheta nextEmployeeTheta) {
        LOG.debug("Request to partially update NextEmployeeTheta : {}", nextEmployeeTheta);

        return nextEmployeeThetaRepository
            .findById(nextEmployeeTheta.getId())
            .map(existingNextEmployeeTheta -> {
                if (nextEmployeeTheta.getFirstName() != null) {
                    existingNextEmployeeTheta.setFirstName(nextEmployeeTheta.getFirstName());
                }
                if (nextEmployeeTheta.getLastName() != null) {
                    existingNextEmployeeTheta.setLastName(nextEmployeeTheta.getLastName());
                }
                if (nextEmployeeTheta.getEmail() != null) {
                    existingNextEmployeeTheta.setEmail(nextEmployeeTheta.getEmail());
                }
                if (nextEmployeeTheta.getHireDate() != null) {
                    existingNextEmployeeTheta.setHireDate(nextEmployeeTheta.getHireDate());
                }
                if (nextEmployeeTheta.getPosition() != null) {
                    existingNextEmployeeTheta.setPosition(nextEmployeeTheta.getPosition());
                }

                return existingNextEmployeeTheta;
            })
            .map(nextEmployeeThetaRepository::save);
    }

    /**
     * Get one nextEmployeeTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeTheta> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeTheta : {}", id);
        return nextEmployeeThetaRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeTheta : {}", id);
        nextEmployeeThetaRepository.deleteById(id);
    }
}
