package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.repository.NextPaymentGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentGamma}.
 */
@Service
@Transactional
public class NextPaymentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentGammaService.class);

    private final NextPaymentGammaRepository nextPaymentGammaRepository;

    public NextPaymentGammaService(NextPaymentGammaRepository nextPaymentGammaRepository) {
        this.nextPaymentGammaRepository = nextPaymentGammaRepository;
    }

    /**
     * Save a nextPaymentGamma.
     *
     * @param nextPaymentGamma the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentGamma save(NextPaymentGamma nextPaymentGamma) {
        LOG.debug("Request to save NextPaymentGamma : {}", nextPaymentGamma);
        return nextPaymentGammaRepository.save(nextPaymentGamma);
    }

    /**
     * Update a nextPaymentGamma.
     *
     * @param nextPaymentGamma the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentGamma update(NextPaymentGamma nextPaymentGamma) {
        LOG.debug("Request to update NextPaymentGamma : {}", nextPaymentGamma);
        return nextPaymentGammaRepository.save(nextPaymentGamma);
    }

    /**
     * Partially update a nextPaymentGamma.
     *
     * @param nextPaymentGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentGamma> partialUpdate(NextPaymentGamma nextPaymentGamma) {
        LOG.debug("Request to partially update NextPaymentGamma : {}", nextPaymentGamma);

        return nextPaymentGammaRepository
            .findById(nextPaymentGamma.getId())
            .map(existingNextPaymentGamma -> {
                if (nextPaymentGamma.getAmount() != null) {
                    existingNextPaymentGamma.setAmount(nextPaymentGamma.getAmount());
                }
                if (nextPaymentGamma.getPaymentDate() != null) {
                    existingNextPaymentGamma.setPaymentDate(nextPaymentGamma.getPaymentDate());
                }
                if (nextPaymentGamma.getPaymentMethod() != null) {
                    existingNextPaymentGamma.setPaymentMethod(nextPaymentGamma.getPaymentMethod());
                }

                return existingNextPaymentGamma;
            })
            .map(nextPaymentGammaRepository::save);
    }

    /**
     * Get one nextPaymentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentGamma> findOne(Long id) {
        LOG.debug("Request to get NextPaymentGamma : {}", id);
        return nextPaymentGammaRepository.findById(id);
    }

    /**
     * Delete the nextPaymentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentGamma : {}", id);
        nextPaymentGammaRepository.deleteById(id);
    }
}
