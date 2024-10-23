package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.repository.NextCustomerAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerAlpha}.
 */
@Service
@Transactional
public class NextCustomerAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerAlphaService.class);

    private final NextCustomerAlphaRepository nextCustomerAlphaRepository;

    public NextCustomerAlphaService(NextCustomerAlphaRepository nextCustomerAlphaRepository) {
        this.nextCustomerAlphaRepository = nextCustomerAlphaRepository;
    }

    /**
     * Save a nextCustomerAlpha.
     *
     * @param nextCustomerAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerAlpha save(NextCustomerAlpha nextCustomerAlpha) {
        LOG.debug("Request to save NextCustomerAlpha : {}", nextCustomerAlpha);
        return nextCustomerAlphaRepository.save(nextCustomerAlpha);
    }

    /**
     * Update a nextCustomerAlpha.
     *
     * @param nextCustomerAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerAlpha update(NextCustomerAlpha nextCustomerAlpha) {
        LOG.debug("Request to update NextCustomerAlpha : {}", nextCustomerAlpha);
        return nextCustomerAlphaRepository.save(nextCustomerAlpha);
    }

    /**
     * Partially update a nextCustomerAlpha.
     *
     * @param nextCustomerAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerAlpha> partialUpdate(NextCustomerAlpha nextCustomerAlpha) {
        LOG.debug("Request to partially update NextCustomerAlpha : {}", nextCustomerAlpha);

        return nextCustomerAlphaRepository
            .findById(nextCustomerAlpha.getId())
            .map(existingNextCustomerAlpha -> {
                if (nextCustomerAlpha.getFirstName() != null) {
                    existingNextCustomerAlpha.setFirstName(nextCustomerAlpha.getFirstName());
                }
                if (nextCustomerAlpha.getLastName() != null) {
                    existingNextCustomerAlpha.setLastName(nextCustomerAlpha.getLastName());
                }
                if (nextCustomerAlpha.getEmail() != null) {
                    existingNextCustomerAlpha.setEmail(nextCustomerAlpha.getEmail());
                }
                if (nextCustomerAlpha.getPhoneNumber() != null) {
                    existingNextCustomerAlpha.setPhoneNumber(nextCustomerAlpha.getPhoneNumber());
                }

                return existingNextCustomerAlpha;
            })
            .map(nextCustomerAlphaRepository::save);
    }

    /**
     * Get one nextCustomerAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerAlpha> findOne(Long id) {
        LOG.debug("Request to get NextCustomerAlpha : {}", id);
        return nextCustomerAlphaRepository.findById(id);
    }

    /**
     * Delete the nextCustomerAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerAlpha : {}", id);
        nextCustomerAlphaRepository.deleteById(id);
    }
}
