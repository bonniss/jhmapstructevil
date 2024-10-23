package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.repository.NextPaymentSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentSigma}.
 */
@Service
@Transactional
public class NextPaymentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentSigmaService.class);

    private final NextPaymentSigmaRepository nextPaymentSigmaRepository;

    public NextPaymentSigmaService(NextPaymentSigmaRepository nextPaymentSigmaRepository) {
        this.nextPaymentSigmaRepository = nextPaymentSigmaRepository;
    }

    /**
     * Save a nextPaymentSigma.
     *
     * @param nextPaymentSigma the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentSigma save(NextPaymentSigma nextPaymentSigma) {
        LOG.debug("Request to save NextPaymentSigma : {}", nextPaymentSigma);
        return nextPaymentSigmaRepository.save(nextPaymentSigma);
    }

    /**
     * Update a nextPaymentSigma.
     *
     * @param nextPaymentSigma the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentSigma update(NextPaymentSigma nextPaymentSigma) {
        LOG.debug("Request to update NextPaymentSigma : {}", nextPaymentSigma);
        return nextPaymentSigmaRepository.save(nextPaymentSigma);
    }

    /**
     * Partially update a nextPaymentSigma.
     *
     * @param nextPaymentSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentSigma> partialUpdate(NextPaymentSigma nextPaymentSigma) {
        LOG.debug("Request to partially update NextPaymentSigma : {}", nextPaymentSigma);

        return nextPaymentSigmaRepository
            .findById(nextPaymentSigma.getId())
            .map(existingNextPaymentSigma -> {
                if (nextPaymentSigma.getAmount() != null) {
                    existingNextPaymentSigma.setAmount(nextPaymentSigma.getAmount());
                }
                if (nextPaymentSigma.getPaymentDate() != null) {
                    existingNextPaymentSigma.setPaymentDate(nextPaymentSigma.getPaymentDate());
                }
                if (nextPaymentSigma.getPaymentMethod() != null) {
                    existingNextPaymentSigma.setPaymentMethod(nextPaymentSigma.getPaymentMethod());
                }

                return existingNextPaymentSigma;
            })
            .map(nextPaymentSigmaRepository::save);
    }

    /**
     * Get one nextPaymentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentSigma> findOne(Long id) {
        LOG.debug("Request to get NextPaymentSigma : {}", id);
        return nextPaymentSigmaRepository.findById(id);
    }

    /**
     * Delete the nextPaymentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentSigma : {}", id);
        nextPaymentSigmaRepository.deleteById(id);
    }
}
