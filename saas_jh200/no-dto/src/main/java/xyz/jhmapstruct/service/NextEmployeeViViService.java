package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeViVi}.
 */
@Service
@Transactional
public class NextEmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViViService.class);

    private final NextEmployeeViViRepository nextEmployeeViViRepository;

    public NextEmployeeViViService(NextEmployeeViViRepository nextEmployeeViViRepository) {
        this.nextEmployeeViViRepository = nextEmployeeViViRepository;
    }

    /**
     * Save a nextEmployeeViVi.
     *
     * @param nextEmployeeViVi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViVi save(NextEmployeeViVi nextEmployeeViVi) {
        LOG.debug("Request to save NextEmployeeViVi : {}", nextEmployeeViVi);
        return nextEmployeeViViRepository.save(nextEmployeeViVi);
    }

    /**
     * Update a nextEmployeeViVi.
     *
     * @param nextEmployeeViVi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeViVi update(NextEmployeeViVi nextEmployeeViVi) {
        LOG.debug("Request to update NextEmployeeViVi : {}", nextEmployeeViVi);
        return nextEmployeeViViRepository.save(nextEmployeeViVi);
    }

    /**
     * Partially update a nextEmployeeViVi.
     *
     * @param nextEmployeeViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeViVi> partialUpdate(NextEmployeeViVi nextEmployeeViVi) {
        LOG.debug("Request to partially update NextEmployeeViVi : {}", nextEmployeeViVi);

        return nextEmployeeViViRepository
            .findById(nextEmployeeViVi.getId())
            .map(existingNextEmployeeViVi -> {
                if (nextEmployeeViVi.getFirstName() != null) {
                    existingNextEmployeeViVi.setFirstName(nextEmployeeViVi.getFirstName());
                }
                if (nextEmployeeViVi.getLastName() != null) {
                    existingNextEmployeeViVi.setLastName(nextEmployeeViVi.getLastName());
                }
                if (nextEmployeeViVi.getEmail() != null) {
                    existingNextEmployeeViVi.setEmail(nextEmployeeViVi.getEmail());
                }
                if (nextEmployeeViVi.getHireDate() != null) {
                    existingNextEmployeeViVi.setHireDate(nextEmployeeViVi.getHireDate());
                }
                if (nextEmployeeViVi.getPosition() != null) {
                    existingNextEmployeeViVi.setPosition(nextEmployeeViVi.getPosition());
                }

                return existingNextEmployeeViVi;
            })
            .map(nextEmployeeViViRepository::save);
    }

    /**
     * Get one nextEmployeeViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeViVi> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeViVi : {}", id);
        return nextEmployeeViViRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeViVi : {}", id);
        nextEmployeeViViRepository.deleteById(id);
    }
}
