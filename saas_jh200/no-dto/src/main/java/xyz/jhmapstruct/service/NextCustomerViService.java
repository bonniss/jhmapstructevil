package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.repository.NextCustomerViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerVi}.
 */
@Service
@Transactional
public class NextCustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViService.class);

    private final NextCustomerViRepository nextCustomerViRepository;

    public NextCustomerViService(NextCustomerViRepository nextCustomerViRepository) {
        this.nextCustomerViRepository = nextCustomerViRepository;
    }

    /**
     * Save a nextCustomerVi.
     *
     * @param nextCustomerVi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerVi save(NextCustomerVi nextCustomerVi) {
        LOG.debug("Request to save NextCustomerVi : {}", nextCustomerVi);
        return nextCustomerViRepository.save(nextCustomerVi);
    }

    /**
     * Update a nextCustomerVi.
     *
     * @param nextCustomerVi the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerVi update(NextCustomerVi nextCustomerVi) {
        LOG.debug("Request to update NextCustomerVi : {}", nextCustomerVi);
        return nextCustomerViRepository.save(nextCustomerVi);
    }

    /**
     * Partially update a nextCustomerVi.
     *
     * @param nextCustomerVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerVi> partialUpdate(NextCustomerVi nextCustomerVi) {
        LOG.debug("Request to partially update NextCustomerVi : {}", nextCustomerVi);

        return nextCustomerViRepository
            .findById(nextCustomerVi.getId())
            .map(existingNextCustomerVi -> {
                if (nextCustomerVi.getFirstName() != null) {
                    existingNextCustomerVi.setFirstName(nextCustomerVi.getFirstName());
                }
                if (nextCustomerVi.getLastName() != null) {
                    existingNextCustomerVi.setLastName(nextCustomerVi.getLastName());
                }
                if (nextCustomerVi.getEmail() != null) {
                    existingNextCustomerVi.setEmail(nextCustomerVi.getEmail());
                }
                if (nextCustomerVi.getPhoneNumber() != null) {
                    existingNextCustomerVi.setPhoneNumber(nextCustomerVi.getPhoneNumber());
                }

                return existingNextCustomerVi;
            })
            .map(nextCustomerViRepository::save);
    }

    /**
     * Get one nextCustomerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerVi> findOne(Long id) {
        LOG.debug("Request to get NextCustomerVi : {}", id);
        return nextCustomerViRepository.findById(id);
    }

    /**
     * Delete the nextCustomerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerVi : {}", id);
        nextCustomerViRepository.deleteById(id);
    }
}
