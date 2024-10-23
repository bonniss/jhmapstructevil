package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.repository.NextCustomerBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerBeta}.
 */
@Service
@Transactional
public class NextCustomerBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerBetaService.class);

    private final NextCustomerBetaRepository nextCustomerBetaRepository;

    public NextCustomerBetaService(NextCustomerBetaRepository nextCustomerBetaRepository) {
        this.nextCustomerBetaRepository = nextCustomerBetaRepository;
    }

    /**
     * Save a nextCustomerBeta.
     *
     * @param nextCustomerBeta the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerBeta save(NextCustomerBeta nextCustomerBeta) {
        LOG.debug("Request to save NextCustomerBeta : {}", nextCustomerBeta);
        return nextCustomerBetaRepository.save(nextCustomerBeta);
    }

    /**
     * Update a nextCustomerBeta.
     *
     * @param nextCustomerBeta the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerBeta update(NextCustomerBeta nextCustomerBeta) {
        LOG.debug("Request to update NextCustomerBeta : {}", nextCustomerBeta);
        return nextCustomerBetaRepository.save(nextCustomerBeta);
    }

    /**
     * Partially update a nextCustomerBeta.
     *
     * @param nextCustomerBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerBeta> partialUpdate(NextCustomerBeta nextCustomerBeta) {
        LOG.debug("Request to partially update NextCustomerBeta : {}", nextCustomerBeta);

        return nextCustomerBetaRepository
            .findById(nextCustomerBeta.getId())
            .map(existingNextCustomerBeta -> {
                if (nextCustomerBeta.getFirstName() != null) {
                    existingNextCustomerBeta.setFirstName(nextCustomerBeta.getFirstName());
                }
                if (nextCustomerBeta.getLastName() != null) {
                    existingNextCustomerBeta.setLastName(nextCustomerBeta.getLastName());
                }
                if (nextCustomerBeta.getEmail() != null) {
                    existingNextCustomerBeta.setEmail(nextCustomerBeta.getEmail());
                }
                if (nextCustomerBeta.getPhoneNumber() != null) {
                    existingNextCustomerBeta.setPhoneNumber(nextCustomerBeta.getPhoneNumber());
                }

                return existingNextCustomerBeta;
            })
            .map(nextCustomerBetaRepository::save);
    }

    /**
     * Get one nextCustomerBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerBeta> findOne(Long id) {
        LOG.debug("Request to get NextCustomerBeta : {}", id);
        return nextCustomerBetaRepository.findById(id);
    }

    /**
     * Delete the nextCustomerBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerBeta : {}", id);
        nextCustomerBetaRepository.deleteById(id);
    }
}
