package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.repository.NextCustomerGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerGamma}.
 */
@Service
@Transactional
public class NextCustomerGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerGammaService.class);

    private final NextCustomerGammaRepository nextCustomerGammaRepository;

    public NextCustomerGammaService(NextCustomerGammaRepository nextCustomerGammaRepository) {
        this.nextCustomerGammaRepository = nextCustomerGammaRepository;
    }

    /**
     * Save a nextCustomerGamma.
     *
     * @param nextCustomerGamma the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerGamma save(NextCustomerGamma nextCustomerGamma) {
        LOG.debug("Request to save NextCustomerGamma : {}", nextCustomerGamma);
        return nextCustomerGammaRepository.save(nextCustomerGamma);
    }

    /**
     * Update a nextCustomerGamma.
     *
     * @param nextCustomerGamma the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerGamma update(NextCustomerGamma nextCustomerGamma) {
        LOG.debug("Request to update NextCustomerGamma : {}", nextCustomerGamma);
        return nextCustomerGammaRepository.save(nextCustomerGamma);
    }

    /**
     * Partially update a nextCustomerGamma.
     *
     * @param nextCustomerGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerGamma> partialUpdate(NextCustomerGamma nextCustomerGamma) {
        LOG.debug("Request to partially update NextCustomerGamma : {}", nextCustomerGamma);

        return nextCustomerGammaRepository
            .findById(nextCustomerGamma.getId())
            .map(existingNextCustomerGamma -> {
                if (nextCustomerGamma.getFirstName() != null) {
                    existingNextCustomerGamma.setFirstName(nextCustomerGamma.getFirstName());
                }
                if (nextCustomerGamma.getLastName() != null) {
                    existingNextCustomerGamma.setLastName(nextCustomerGamma.getLastName());
                }
                if (nextCustomerGamma.getEmail() != null) {
                    existingNextCustomerGamma.setEmail(nextCustomerGamma.getEmail());
                }
                if (nextCustomerGamma.getPhoneNumber() != null) {
                    existingNextCustomerGamma.setPhoneNumber(nextCustomerGamma.getPhoneNumber());
                }

                return existingNextCustomerGamma;
            })
            .map(nextCustomerGammaRepository::save);
    }

    /**
     * Get one nextCustomerGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerGamma> findOne(Long id) {
        LOG.debug("Request to get NextCustomerGamma : {}", id);
        return nextCustomerGammaRepository.findById(id);
    }

    /**
     * Delete the nextCustomerGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerGamma : {}", id);
        nextCustomerGammaRepository.deleteById(id);
    }
}
