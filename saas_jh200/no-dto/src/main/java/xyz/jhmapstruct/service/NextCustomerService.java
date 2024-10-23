package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.repository.NextCustomerRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomer}.
 */
@Service
@Transactional
public class NextCustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerService.class);

    private final NextCustomerRepository nextCustomerRepository;

    public NextCustomerService(NextCustomerRepository nextCustomerRepository) {
        this.nextCustomerRepository = nextCustomerRepository;
    }

    /**
     * Save a nextCustomer.
     *
     * @param nextCustomer the entity to save.
     * @return the persisted entity.
     */
    public NextCustomer save(NextCustomer nextCustomer) {
        LOG.debug("Request to save NextCustomer : {}", nextCustomer);
        return nextCustomerRepository.save(nextCustomer);
    }

    /**
     * Update a nextCustomer.
     *
     * @param nextCustomer the entity to save.
     * @return the persisted entity.
     */
    public NextCustomer update(NextCustomer nextCustomer) {
        LOG.debug("Request to update NextCustomer : {}", nextCustomer);
        return nextCustomerRepository.save(nextCustomer);
    }

    /**
     * Partially update a nextCustomer.
     *
     * @param nextCustomer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomer> partialUpdate(NextCustomer nextCustomer) {
        LOG.debug("Request to partially update NextCustomer : {}", nextCustomer);

        return nextCustomerRepository
            .findById(nextCustomer.getId())
            .map(existingNextCustomer -> {
                if (nextCustomer.getFirstName() != null) {
                    existingNextCustomer.setFirstName(nextCustomer.getFirstName());
                }
                if (nextCustomer.getLastName() != null) {
                    existingNextCustomer.setLastName(nextCustomer.getLastName());
                }
                if (nextCustomer.getEmail() != null) {
                    existingNextCustomer.setEmail(nextCustomer.getEmail());
                }
                if (nextCustomer.getPhoneNumber() != null) {
                    existingNextCustomer.setPhoneNumber(nextCustomer.getPhoneNumber());
                }

                return existingNextCustomer;
            })
            .map(nextCustomerRepository::save);
    }

    /**
     * Get one nextCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomer> findOne(Long id) {
        LOG.debug("Request to get NextCustomer : {}", id);
        return nextCustomerRepository.findById(id);
    }

    /**
     * Delete the nextCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomer : {}", id);
        nextCustomerRepository.deleteById(id);
    }
}
