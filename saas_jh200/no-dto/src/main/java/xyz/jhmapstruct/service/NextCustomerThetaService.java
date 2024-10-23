package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.repository.NextCustomerThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerTheta}.
 */
@Service
@Transactional
public class NextCustomerThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerThetaService.class);

    private final NextCustomerThetaRepository nextCustomerThetaRepository;

    public NextCustomerThetaService(NextCustomerThetaRepository nextCustomerThetaRepository) {
        this.nextCustomerThetaRepository = nextCustomerThetaRepository;
    }

    /**
     * Save a nextCustomerTheta.
     *
     * @param nextCustomerTheta the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerTheta save(NextCustomerTheta nextCustomerTheta) {
        LOG.debug("Request to save NextCustomerTheta : {}", nextCustomerTheta);
        return nextCustomerThetaRepository.save(nextCustomerTheta);
    }

    /**
     * Update a nextCustomerTheta.
     *
     * @param nextCustomerTheta the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerTheta update(NextCustomerTheta nextCustomerTheta) {
        LOG.debug("Request to update NextCustomerTheta : {}", nextCustomerTheta);
        return nextCustomerThetaRepository.save(nextCustomerTheta);
    }

    /**
     * Partially update a nextCustomerTheta.
     *
     * @param nextCustomerTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerTheta> partialUpdate(NextCustomerTheta nextCustomerTheta) {
        LOG.debug("Request to partially update NextCustomerTheta : {}", nextCustomerTheta);

        return nextCustomerThetaRepository
            .findById(nextCustomerTheta.getId())
            .map(existingNextCustomerTheta -> {
                if (nextCustomerTheta.getFirstName() != null) {
                    existingNextCustomerTheta.setFirstName(nextCustomerTheta.getFirstName());
                }
                if (nextCustomerTheta.getLastName() != null) {
                    existingNextCustomerTheta.setLastName(nextCustomerTheta.getLastName());
                }
                if (nextCustomerTheta.getEmail() != null) {
                    existingNextCustomerTheta.setEmail(nextCustomerTheta.getEmail());
                }
                if (nextCustomerTheta.getPhoneNumber() != null) {
                    existingNextCustomerTheta.setPhoneNumber(nextCustomerTheta.getPhoneNumber());
                }

                return existingNextCustomerTheta;
            })
            .map(nextCustomerThetaRepository::save);
    }

    /**
     * Get one nextCustomerTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerTheta> findOne(Long id) {
        LOG.debug("Request to get NextCustomerTheta : {}", id);
        return nextCustomerThetaRepository.findById(id);
    }

    /**
     * Delete the nextCustomerTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerTheta : {}", id);
        nextCustomerThetaRepository.deleteById(id);
    }
}
