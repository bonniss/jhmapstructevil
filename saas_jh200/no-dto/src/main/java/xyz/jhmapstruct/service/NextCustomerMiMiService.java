package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.repository.NextCustomerMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerMiMi}.
 */
@Service
@Transactional
public class NextCustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiMiService.class);

    private final NextCustomerMiMiRepository nextCustomerMiMiRepository;

    public NextCustomerMiMiService(NextCustomerMiMiRepository nextCustomerMiMiRepository) {
        this.nextCustomerMiMiRepository = nextCustomerMiMiRepository;
    }

    /**
     * Save a nextCustomerMiMi.
     *
     * @param nextCustomerMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiMi save(NextCustomerMiMi nextCustomerMiMi) {
        LOG.debug("Request to save NextCustomerMiMi : {}", nextCustomerMiMi);
        return nextCustomerMiMiRepository.save(nextCustomerMiMi);
    }

    /**
     * Update a nextCustomerMiMi.
     *
     * @param nextCustomerMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMiMi update(NextCustomerMiMi nextCustomerMiMi) {
        LOG.debug("Request to update NextCustomerMiMi : {}", nextCustomerMiMi);
        return nextCustomerMiMiRepository.save(nextCustomerMiMi);
    }

    /**
     * Partially update a nextCustomerMiMi.
     *
     * @param nextCustomerMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerMiMi> partialUpdate(NextCustomerMiMi nextCustomerMiMi) {
        LOG.debug("Request to partially update NextCustomerMiMi : {}", nextCustomerMiMi);

        return nextCustomerMiMiRepository
            .findById(nextCustomerMiMi.getId())
            .map(existingNextCustomerMiMi -> {
                if (nextCustomerMiMi.getFirstName() != null) {
                    existingNextCustomerMiMi.setFirstName(nextCustomerMiMi.getFirstName());
                }
                if (nextCustomerMiMi.getLastName() != null) {
                    existingNextCustomerMiMi.setLastName(nextCustomerMiMi.getLastName());
                }
                if (nextCustomerMiMi.getEmail() != null) {
                    existingNextCustomerMiMi.setEmail(nextCustomerMiMi.getEmail());
                }
                if (nextCustomerMiMi.getPhoneNumber() != null) {
                    existingNextCustomerMiMi.setPhoneNumber(nextCustomerMiMi.getPhoneNumber());
                }

                return existingNextCustomerMiMi;
            })
            .map(nextCustomerMiMiRepository::save);
    }

    /**
     * Get one nextCustomerMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerMiMi> findOne(Long id) {
        LOG.debug("Request to get NextCustomerMiMi : {}", id);
        return nextCustomerMiMiRepository.findById(id);
    }

    /**
     * Delete the nextCustomerMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerMiMi : {}", id);
        nextCustomerMiMiRepository.deleteById(id);
    }
}
