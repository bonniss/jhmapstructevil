package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeVi;
import xyz.jhmapstruct.repository.NextEmployeeViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeVi}.
 */
@Service
@Transactional
public class NextEmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViService.class);

    private final NextEmployeeViRepository nextEmployeeViRepository;

    public NextEmployeeViService(NextEmployeeViRepository nextEmployeeViRepository) {
        this.nextEmployeeViRepository = nextEmployeeViRepository;
    }

    /**
     * Save a nextEmployeeVi.
     *
     * @param nextEmployeeVi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeVi save(NextEmployeeVi nextEmployeeVi) {
        LOG.debug("Request to save NextEmployeeVi : {}", nextEmployeeVi);
        return nextEmployeeViRepository.save(nextEmployeeVi);
    }

    /**
     * Update a nextEmployeeVi.
     *
     * @param nextEmployeeVi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeVi update(NextEmployeeVi nextEmployeeVi) {
        LOG.debug("Request to update NextEmployeeVi : {}", nextEmployeeVi);
        return nextEmployeeViRepository.save(nextEmployeeVi);
    }

    /**
     * Partially update a nextEmployeeVi.
     *
     * @param nextEmployeeVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeVi> partialUpdate(NextEmployeeVi nextEmployeeVi) {
        LOG.debug("Request to partially update NextEmployeeVi : {}", nextEmployeeVi);

        return nextEmployeeViRepository
            .findById(nextEmployeeVi.getId())
            .map(existingNextEmployeeVi -> {
                if (nextEmployeeVi.getFirstName() != null) {
                    existingNextEmployeeVi.setFirstName(nextEmployeeVi.getFirstName());
                }
                if (nextEmployeeVi.getLastName() != null) {
                    existingNextEmployeeVi.setLastName(nextEmployeeVi.getLastName());
                }
                if (nextEmployeeVi.getEmail() != null) {
                    existingNextEmployeeVi.setEmail(nextEmployeeVi.getEmail());
                }
                if (nextEmployeeVi.getHireDate() != null) {
                    existingNextEmployeeVi.setHireDate(nextEmployeeVi.getHireDate());
                }
                if (nextEmployeeVi.getPosition() != null) {
                    existingNextEmployeeVi.setPosition(nextEmployeeVi.getPosition());
                }

                return existingNextEmployeeVi;
            })
            .map(nextEmployeeViRepository::save);
    }

    /**
     * Get one nextEmployeeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeVi> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeVi : {}", id);
        return nextEmployeeViRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeVi : {}", id);
        nextEmployeeViRepository.deleteById(id);
    }
}
