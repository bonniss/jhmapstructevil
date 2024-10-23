package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.repository.NextCustomerSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerSigma}.
 */
@Service
@Transactional
public class NextCustomerSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerSigmaService.class);

    private final NextCustomerSigmaRepository nextCustomerSigmaRepository;

    public NextCustomerSigmaService(NextCustomerSigmaRepository nextCustomerSigmaRepository) {
        this.nextCustomerSigmaRepository = nextCustomerSigmaRepository;
    }

    /**
     * Save a nextCustomerSigma.
     *
     * @param nextCustomerSigma the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerSigma save(NextCustomerSigma nextCustomerSigma) {
        LOG.debug("Request to save NextCustomerSigma : {}", nextCustomerSigma);
        return nextCustomerSigmaRepository.save(nextCustomerSigma);
    }

    /**
     * Update a nextCustomerSigma.
     *
     * @param nextCustomerSigma the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerSigma update(NextCustomerSigma nextCustomerSigma) {
        LOG.debug("Request to update NextCustomerSigma : {}", nextCustomerSigma);
        return nextCustomerSigmaRepository.save(nextCustomerSigma);
    }

    /**
     * Partially update a nextCustomerSigma.
     *
     * @param nextCustomerSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerSigma> partialUpdate(NextCustomerSigma nextCustomerSigma) {
        LOG.debug("Request to partially update NextCustomerSigma : {}", nextCustomerSigma);

        return nextCustomerSigmaRepository
            .findById(nextCustomerSigma.getId())
            .map(existingNextCustomerSigma -> {
                if (nextCustomerSigma.getFirstName() != null) {
                    existingNextCustomerSigma.setFirstName(nextCustomerSigma.getFirstName());
                }
                if (nextCustomerSigma.getLastName() != null) {
                    existingNextCustomerSigma.setLastName(nextCustomerSigma.getLastName());
                }
                if (nextCustomerSigma.getEmail() != null) {
                    existingNextCustomerSigma.setEmail(nextCustomerSigma.getEmail());
                }
                if (nextCustomerSigma.getPhoneNumber() != null) {
                    existingNextCustomerSigma.setPhoneNumber(nextCustomerSigma.getPhoneNumber());
                }

                return existingNextCustomerSigma;
            })
            .map(nextCustomerSigmaRepository::save);
    }

    /**
     * Get one nextCustomerSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerSigma> findOne(Long id) {
        LOG.debug("Request to get NextCustomerSigma : {}", id);
        return nextCustomerSigmaRepository.findById(id);
    }

    /**
     * Delete the nextCustomerSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerSigma : {}", id);
        nextCustomerSigmaRepository.deleteById(id);
    }
}
