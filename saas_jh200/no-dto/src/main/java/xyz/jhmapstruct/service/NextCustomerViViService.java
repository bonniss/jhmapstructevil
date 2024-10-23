package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.repository.NextCustomerViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerViVi}.
 */
@Service
@Transactional
public class NextCustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViViService.class);

    private final NextCustomerViViRepository nextCustomerViViRepository;

    public NextCustomerViViService(NextCustomerViViRepository nextCustomerViViRepository) {
        this.nextCustomerViViRepository = nextCustomerViViRepository;
    }

    /**
     * Save a nextCustomerViVi.
     *
     * @param nextCustomerViVi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViVi save(NextCustomerViVi nextCustomerViVi) {
        LOG.debug("Request to save NextCustomerViVi : {}", nextCustomerViVi);
        return nextCustomerViViRepository.save(nextCustomerViVi);
    }

    /**
     * Update a nextCustomerViVi.
     *
     * @param nextCustomerViVi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerViVi update(NextCustomerViVi nextCustomerViVi) {
        LOG.debug("Request to update NextCustomerViVi : {}", nextCustomerViVi);
        return nextCustomerViViRepository.save(nextCustomerViVi);
    }

    /**
     * Partially update a nextCustomerViVi.
     *
     * @param nextCustomerViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerViVi> partialUpdate(NextCustomerViVi nextCustomerViVi) {
        LOG.debug("Request to partially update NextCustomerViVi : {}", nextCustomerViVi);

        return nextCustomerViViRepository
            .findById(nextCustomerViVi.getId())
            .map(existingNextCustomerViVi -> {
                if (nextCustomerViVi.getFirstName() != null) {
                    existingNextCustomerViVi.setFirstName(nextCustomerViVi.getFirstName());
                }
                if (nextCustomerViVi.getLastName() != null) {
                    existingNextCustomerViVi.setLastName(nextCustomerViVi.getLastName());
                }
                if (nextCustomerViVi.getEmail() != null) {
                    existingNextCustomerViVi.setEmail(nextCustomerViVi.getEmail());
                }
                if (nextCustomerViVi.getPhoneNumber() != null) {
                    existingNextCustomerViVi.setPhoneNumber(nextCustomerViVi.getPhoneNumber());
                }

                return existingNextCustomerViVi;
            })
            .map(nextCustomerViViRepository::save);
    }

    /**
     * Get one nextCustomerViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerViVi> findOne(Long id) {
        LOG.debug("Request to get NextCustomerViVi : {}", id);
        return nextCustomerViViRepository.findById(id);
    }

    /**
     * Delete the nextCustomerViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerViVi : {}", id);
        nextCustomerViViRepository.deleteById(id);
    }
}
