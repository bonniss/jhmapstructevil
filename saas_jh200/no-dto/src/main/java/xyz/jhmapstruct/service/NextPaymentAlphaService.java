package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.repository.NextPaymentAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentAlpha}.
 */
@Service
@Transactional
public class NextPaymentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentAlphaService.class);

    private final NextPaymentAlphaRepository nextPaymentAlphaRepository;

    public NextPaymentAlphaService(NextPaymentAlphaRepository nextPaymentAlphaRepository) {
        this.nextPaymentAlphaRepository = nextPaymentAlphaRepository;
    }

    /**
     * Save a nextPaymentAlpha.
     *
     * @param nextPaymentAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentAlpha save(NextPaymentAlpha nextPaymentAlpha) {
        LOG.debug("Request to save NextPaymentAlpha : {}", nextPaymentAlpha);
        return nextPaymentAlphaRepository.save(nextPaymentAlpha);
    }

    /**
     * Update a nextPaymentAlpha.
     *
     * @param nextPaymentAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentAlpha update(NextPaymentAlpha nextPaymentAlpha) {
        LOG.debug("Request to update NextPaymentAlpha : {}", nextPaymentAlpha);
        return nextPaymentAlphaRepository.save(nextPaymentAlpha);
    }

    /**
     * Partially update a nextPaymentAlpha.
     *
     * @param nextPaymentAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentAlpha> partialUpdate(NextPaymentAlpha nextPaymentAlpha) {
        LOG.debug("Request to partially update NextPaymentAlpha : {}", nextPaymentAlpha);

        return nextPaymentAlphaRepository
            .findById(nextPaymentAlpha.getId())
            .map(existingNextPaymentAlpha -> {
                if (nextPaymentAlpha.getAmount() != null) {
                    existingNextPaymentAlpha.setAmount(nextPaymentAlpha.getAmount());
                }
                if (nextPaymentAlpha.getPaymentDate() != null) {
                    existingNextPaymentAlpha.setPaymentDate(nextPaymentAlpha.getPaymentDate());
                }
                if (nextPaymentAlpha.getPaymentMethod() != null) {
                    existingNextPaymentAlpha.setPaymentMethod(nextPaymentAlpha.getPaymentMethod());
                }

                return existingNextPaymentAlpha;
            })
            .map(nextPaymentAlphaRepository::save);
    }

    /**
     * Get one nextPaymentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentAlpha> findOne(Long id) {
        LOG.debug("Request to get NextPaymentAlpha : {}", id);
        return nextPaymentAlphaRepository.findById(id);
    }

    /**
     * Delete the nextPaymentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentAlpha : {}", id);
        nextPaymentAlphaRepository.deleteById(id);
    }
}
