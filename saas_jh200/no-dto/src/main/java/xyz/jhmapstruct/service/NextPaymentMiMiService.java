package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.repository.NextPaymentMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentMiMi}.
 */
@Service
@Transactional
public class NextPaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiMiService.class);

    private final NextPaymentMiMiRepository nextPaymentMiMiRepository;

    public NextPaymentMiMiService(NextPaymentMiMiRepository nextPaymentMiMiRepository) {
        this.nextPaymentMiMiRepository = nextPaymentMiMiRepository;
    }

    /**
     * Save a nextPaymentMiMi.
     *
     * @param nextPaymentMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiMi save(NextPaymentMiMi nextPaymentMiMi) {
        LOG.debug("Request to save NextPaymentMiMi : {}", nextPaymentMiMi);
        return nextPaymentMiMiRepository.save(nextPaymentMiMi);
    }

    /**
     * Update a nextPaymentMiMi.
     *
     * @param nextPaymentMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiMi update(NextPaymentMiMi nextPaymentMiMi) {
        LOG.debug("Request to update NextPaymentMiMi : {}", nextPaymentMiMi);
        return nextPaymentMiMiRepository.save(nextPaymentMiMi);
    }

    /**
     * Partially update a nextPaymentMiMi.
     *
     * @param nextPaymentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentMiMi> partialUpdate(NextPaymentMiMi nextPaymentMiMi) {
        LOG.debug("Request to partially update NextPaymentMiMi : {}", nextPaymentMiMi);

        return nextPaymentMiMiRepository
            .findById(nextPaymentMiMi.getId())
            .map(existingNextPaymentMiMi -> {
                if (nextPaymentMiMi.getAmount() != null) {
                    existingNextPaymentMiMi.setAmount(nextPaymentMiMi.getAmount());
                }
                if (nextPaymentMiMi.getPaymentDate() != null) {
                    existingNextPaymentMiMi.setPaymentDate(nextPaymentMiMi.getPaymentDate());
                }
                if (nextPaymentMiMi.getPaymentMethod() != null) {
                    existingNextPaymentMiMi.setPaymentMethod(nextPaymentMiMi.getPaymentMethod());
                }

                return existingNextPaymentMiMi;
            })
            .map(nextPaymentMiMiRepository::save);
    }

    /**
     * Get one nextPaymentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentMiMi> findOne(Long id) {
        LOG.debug("Request to get NextPaymentMiMi : {}", id);
        return nextPaymentMiMiRepository.findById(id);
    }

    /**
     * Delete the nextPaymentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentMiMi : {}", id);
        nextPaymentMiMiRepository.deleteById(id);
    }
}
