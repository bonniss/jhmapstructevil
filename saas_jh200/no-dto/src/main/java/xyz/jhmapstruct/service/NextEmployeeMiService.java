package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.repository.NextEmployeeMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeMi}.
 */
@Service
@Transactional
public class NextEmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiService.class);

    private final NextEmployeeMiRepository nextEmployeeMiRepository;

    public NextEmployeeMiService(NextEmployeeMiRepository nextEmployeeMiRepository) {
        this.nextEmployeeMiRepository = nextEmployeeMiRepository;
    }

    /**
     * Save a nextEmployeeMi.
     *
     * @param nextEmployeeMi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMi save(NextEmployeeMi nextEmployeeMi) {
        LOG.debug("Request to save NextEmployeeMi : {}", nextEmployeeMi);
        return nextEmployeeMiRepository.save(nextEmployeeMi);
    }

    /**
     * Update a nextEmployeeMi.
     *
     * @param nextEmployeeMi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMi update(NextEmployeeMi nextEmployeeMi) {
        LOG.debug("Request to update NextEmployeeMi : {}", nextEmployeeMi);
        return nextEmployeeMiRepository.save(nextEmployeeMi);
    }

    /**
     * Partially update a nextEmployeeMi.
     *
     * @param nextEmployeeMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeMi> partialUpdate(NextEmployeeMi nextEmployeeMi) {
        LOG.debug("Request to partially update NextEmployeeMi : {}", nextEmployeeMi);

        return nextEmployeeMiRepository
            .findById(nextEmployeeMi.getId())
            .map(existingNextEmployeeMi -> {
                if (nextEmployeeMi.getFirstName() != null) {
                    existingNextEmployeeMi.setFirstName(nextEmployeeMi.getFirstName());
                }
                if (nextEmployeeMi.getLastName() != null) {
                    existingNextEmployeeMi.setLastName(nextEmployeeMi.getLastName());
                }
                if (nextEmployeeMi.getEmail() != null) {
                    existingNextEmployeeMi.setEmail(nextEmployeeMi.getEmail());
                }
                if (nextEmployeeMi.getHireDate() != null) {
                    existingNextEmployeeMi.setHireDate(nextEmployeeMi.getHireDate());
                }
                if (nextEmployeeMi.getPosition() != null) {
                    existingNextEmployeeMi.setPosition(nextEmployeeMi.getPosition());
                }

                return existingNextEmployeeMi;
            })
            .map(nextEmployeeMiRepository::save);
    }

    /**
     * Get one nextEmployeeMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeMi> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeMi : {}", id);
        return nextEmployeeMiRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeMi : {}", id);
        nextEmployeeMiRepository.deleteById(id);
    }
}
