package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.repository.NextPaymentBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentBeta}.
 */
@Service
@Transactional
public class NextPaymentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentBetaService.class);

    private final NextPaymentBetaRepository nextPaymentBetaRepository;

    public NextPaymentBetaService(NextPaymentBetaRepository nextPaymentBetaRepository) {
        this.nextPaymentBetaRepository = nextPaymentBetaRepository;
    }

    /**
     * Save a nextPaymentBeta.
     *
     * @param nextPaymentBeta the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentBeta save(NextPaymentBeta nextPaymentBeta) {
        LOG.debug("Request to save NextPaymentBeta : {}", nextPaymentBeta);
        return nextPaymentBetaRepository.save(nextPaymentBeta);
    }

    /**
     * Update a nextPaymentBeta.
     *
     * @param nextPaymentBeta the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentBeta update(NextPaymentBeta nextPaymentBeta) {
        LOG.debug("Request to update NextPaymentBeta : {}", nextPaymentBeta);
        return nextPaymentBetaRepository.save(nextPaymentBeta);
    }

    /**
     * Partially update a nextPaymentBeta.
     *
     * @param nextPaymentBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentBeta> partialUpdate(NextPaymentBeta nextPaymentBeta) {
        LOG.debug("Request to partially update NextPaymentBeta : {}", nextPaymentBeta);

        return nextPaymentBetaRepository
            .findById(nextPaymentBeta.getId())
            .map(existingNextPaymentBeta -> {
                if (nextPaymentBeta.getAmount() != null) {
                    existingNextPaymentBeta.setAmount(nextPaymentBeta.getAmount());
                }
                if (nextPaymentBeta.getPaymentDate() != null) {
                    existingNextPaymentBeta.setPaymentDate(nextPaymentBeta.getPaymentDate());
                }
                if (nextPaymentBeta.getPaymentMethod() != null) {
                    existingNextPaymentBeta.setPaymentMethod(nextPaymentBeta.getPaymentMethod());
                }

                return existingNextPaymentBeta;
            })
            .map(nextPaymentBetaRepository::save);
    }

    /**
     * Get one nextPaymentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentBeta> findOne(Long id) {
        LOG.debug("Request to get NextPaymentBeta : {}", id);
        return nextPaymentBetaRepository.findById(id);
    }

    /**
     * Delete the nextPaymentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentBeta : {}", id);
        nextPaymentBetaRepository.deleteById(id);
    }
}
