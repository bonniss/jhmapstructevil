package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.repository.NextPaymentMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentMi}.
 */
@Service
@Transactional
public class NextPaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiService.class);

    private final NextPaymentMiRepository nextPaymentMiRepository;

    public NextPaymentMiService(NextPaymentMiRepository nextPaymentMiRepository) {
        this.nextPaymentMiRepository = nextPaymentMiRepository;
    }

    /**
     * Save a nextPaymentMi.
     *
     * @param nextPaymentMi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMi save(NextPaymentMi nextPaymentMi) {
        LOG.debug("Request to save NextPaymentMi : {}", nextPaymentMi);
        return nextPaymentMiRepository.save(nextPaymentMi);
    }

    /**
     * Update a nextPaymentMi.
     *
     * @param nextPaymentMi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMi update(NextPaymentMi nextPaymentMi) {
        LOG.debug("Request to update NextPaymentMi : {}", nextPaymentMi);
        return nextPaymentMiRepository.save(nextPaymentMi);
    }

    /**
     * Partially update a nextPaymentMi.
     *
     * @param nextPaymentMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentMi> partialUpdate(NextPaymentMi nextPaymentMi) {
        LOG.debug("Request to partially update NextPaymentMi : {}", nextPaymentMi);

        return nextPaymentMiRepository
            .findById(nextPaymentMi.getId())
            .map(existingNextPaymentMi -> {
                if (nextPaymentMi.getAmount() != null) {
                    existingNextPaymentMi.setAmount(nextPaymentMi.getAmount());
                }
                if (nextPaymentMi.getPaymentDate() != null) {
                    existingNextPaymentMi.setPaymentDate(nextPaymentMi.getPaymentDate());
                }
                if (nextPaymentMi.getPaymentMethod() != null) {
                    existingNextPaymentMi.setPaymentMethod(nextPaymentMi.getPaymentMethod());
                }

                return existingNextPaymentMi;
            })
            .map(nextPaymentMiRepository::save);
    }

    /**
     * Get one nextPaymentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentMi> findOne(Long id) {
        LOG.debug("Request to get NextPaymentMi : {}", id);
        return nextPaymentMiRepository.findById(id);
    }

    /**
     * Delete the nextPaymentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentMi : {}", id);
        nextPaymentMiRepository.deleteById(id);
    }
}
