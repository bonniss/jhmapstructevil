package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.repository.NextCustomerMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerMi}.
 */
@Service
@Transactional
public class NextCustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiService.class);

    private final NextCustomerMiRepository nextCustomerMiRepository;

    public NextCustomerMiService(NextCustomerMiRepository nextCustomerMiRepository) {
        this.nextCustomerMiRepository = nextCustomerMiRepository;
    }

    /**
     * Save a nextCustomerMi.
     *
     * @param nextCustomerMi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMi save(NextCustomerMi nextCustomerMi) {
        LOG.debug("Request to save NextCustomerMi : {}", nextCustomerMi);
        return nextCustomerMiRepository.save(nextCustomerMi);
    }

    /**
     * Update a nextCustomerMi.
     *
     * @param nextCustomerMi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerMi update(NextCustomerMi nextCustomerMi) {
        LOG.debug("Request to update NextCustomerMi : {}", nextCustomerMi);
        return nextCustomerMiRepository.save(nextCustomerMi);
    }

    /**
     * Partially update a nextCustomerMi.
     *
     * @param nextCustomerMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerMi> partialUpdate(NextCustomerMi nextCustomerMi) {
        LOG.debug("Request to partially update NextCustomerMi : {}", nextCustomerMi);

        return nextCustomerMiRepository
            .findById(nextCustomerMi.getId())
            .map(existingNextCustomerMi -> {
                if (nextCustomerMi.getFirstName() != null) {
                    existingNextCustomerMi.setFirstName(nextCustomerMi.getFirstName());
                }
                if (nextCustomerMi.getLastName() != null) {
                    existingNextCustomerMi.setLastName(nextCustomerMi.getLastName());
                }
                if (nextCustomerMi.getEmail() != null) {
                    existingNextCustomerMi.setEmail(nextCustomerMi.getEmail());
                }
                if (nextCustomerMi.getPhoneNumber() != null) {
                    existingNextCustomerMi.setPhoneNumber(nextCustomerMi.getPhoneNumber());
                }

                return existingNextCustomerMi;
            })
            .map(nextCustomerMiRepository::save);
    }

    /**
     * Get one nextCustomerMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerMi> findOne(Long id) {
        LOG.debug("Request to get NextCustomerMi : {}", id);
        return nextCustomerMiRepository.findById(id);
    }

    /**
     * Delete the nextCustomerMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerMi : {}", id);
        nextCustomerMiRepository.deleteById(id);
    }
}
