package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.repository.NextPaymentThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentTheta}.
 */
@Service
@Transactional
public class NextPaymentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentThetaService.class);

    private final NextPaymentThetaRepository nextPaymentThetaRepository;

    public NextPaymentThetaService(NextPaymentThetaRepository nextPaymentThetaRepository) {
        this.nextPaymentThetaRepository = nextPaymentThetaRepository;
    }

    /**
     * Save a nextPaymentTheta.
     *
     * @param nextPaymentTheta the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentTheta save(NextPaymentTheta nextPaymentTheta) {
        LOG.debug("Request to save NextPaymentTheta : {}", nextPaymentTheta);
        return nextPaymentThetaRepository.save(nextPaymentTheta);
    }

    /**
     * Update a nextPaymentTheta.
     *
     * @param nextPaymentTheta the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentTheta update(NextPaymentTheta nextPaymentTheta) {
        LOG.debug("Request to update NextPaymentTheta : {}", nextPaymentTheta);
        return nextPaymentThetaRepository.save(nextPaymentTheta);
    }

    /**
     * Partially update a nextPaymentTheta.
     *
     * @param nextPaymentTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentTheta> partialUpdate(NextPaymentTheta nextPaymentTheta) {
        LOG.debug("Request to partially update NextPaymentTheta : {}", nextPaymentTheta);

        return nextPaymentThetaRepository
            .findById(nextPaymentTheta.getId())
            .map(existingNextPaymentTheta -> {
                if (nextPaymentTheta.getAmount() != null) {
                    existingNextPaymentTheta.setAmount(nextPaymentTheta.getAmount());
                }
                if (nextPaymentTheta.getPaymentDate() != null) {
                    existingNextPaymentTheta.setPaymentDate(nextPaymentTheta.getPaymentDate());
                }
                if (nextPaymentTheta.getPaymentMethod() != null) {
                    existingNextPaymentTheta.setPaymentMethod(nextPaymentTheta.getPaymentMethod());
                }

                return existingNextPaymentTheta;
            })
            .map(nextPaymentThetaRepository::save);
    }

    /**
     * Get one nextPaymentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentTheta> findOne(Long id) {
        LOG.debug("Request to get NextPaymentTheta : {}", id);
        return nextPaymentThetaRepository.findById(id);
    }

    /**
     * Delete the nextPaymentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentTheta : {}", id);
        nextPaymentThetaRepository.deleteById(id);
    }
}
