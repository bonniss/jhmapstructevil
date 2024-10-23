package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeMiMi;
import xyz.jhmapstruct.repository.NextEmployeeMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeMiMi}.
 */
@Service
@Transactional
public class NextEmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiMiService.class);

    private final NextEmployeeMiMiRepository nextEmployeeMiMiRepository;

    public NextEmployeeMiMiService(NextEmployeeMiMiRepository nextEmployeeMiMiRepository) {
        this.nextEmployeeMiMiRepository = nextEmployeeMiMiRepository;
    }

    /**
     * Save a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiMi save(NextEmployeeMiMi nextEmployeeMiMi) {
        LOG.debug("Request to save NextEmployeeMiMi : {}", nextEmployeeMiMi);
        return nextEmployeeMiMiRepository.save(nextEmployeeMiMi);
    }

    /**
     * Update a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiMi update(NextEmployeeMiMi nextEmployeeMiMi) {
        LOG.debug("Request to update NextEmployeeMiMi : {}", nextEmployeeMiMi);
        return nextEmployeeMiMiRepository.save(nextEmployeeMiMi);
    }

    /**
     * Partially update a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeMiMi> partialUpdate(NextEmployeeMiMi nextEmployeeMiMi) {
        LOG.debug("Request to partially update NextEmployeeMiMi : {}", nextEmployeeMiMi);

        return nextEmployeeMiMiRepository
            .findById(nextEmployeeMiMi.getId())
            .map(existingNextEmployeeMiMi -> {
                if (nextEmployeeMiMi.getFirstName() != null) {
                    existingNextEmployeeMiMi.setFirstName(nextEmployeeMiMi.getFirstName());
                }
                if (nextEmployeeMiMi.getLastName() != null) {
                    existingNextEmployeeMiMi.setLastName(nextEmployeeMiMi.getLastName());
                }
                if (nextEmployeeMiMi.getEmail() != null) {
                    existingNextEmployeeMiMi.setEmail(nextEmployeeMiMi.getEmail());
                }
                if (nextEmployeeMiMi.getHireDate() != null) {
                    existingNextEmployeeMiMi.setHireDate(nextEmployeeMiMi.getHireDate());
                }
                if (nextEmployeeMiMi.getPosition() != null) {
                    existingNextEmployeeMiMi.setPosition(nextEmployeeMiMi.getPosition());
                }

                return existingNextEmployeeMiMi;
            })
            .map(nextEmployeeMiMiRepository::save);
    }

    /**
     * Get one nextEmployeeMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeMiMi> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeMiMi : {}", id);
        return nextEmployeeMiMiRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeMiMi : {}", id);
        nextEmployeeMiMiRepository.deleteById(id);
    }
}
