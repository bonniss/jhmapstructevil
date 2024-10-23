package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.repository.NextPaymentViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentViVi}.
 */
@Service
@Transactional
public class NextPaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViViService.class);

    private final NextPaymentViViRepository nextPaymentViViRepository;

    public NextPaymentViViService(NextPaymentViViRepository nextPaymentViViRepository) {
        this.nextPaymentViViRepository = nextPaymentViViRepository;
    }

    /**
     * Save a nextPaymentViVi.
     *
     * @param nextPaymentViVi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViVi save(NextPaymentViVi nextPaymentViVi) {
        LOG.debug("Request to save NextPaymentViVi : {}", nextPaymentViVi);
        return nextPaymentViViRepository.save(nextPaymentViVi);
    }

    /**
     * Update a nextPaymentViVi.
     *
     * @param nextPaymentViVi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViVi update(NextPaymentViVi nextPaymentViVi) {
        LOG.debug("Request to update NextPaymentViVi : {}", nextPaymentViVi);
        return nextPaymentViViRepository.save(nextPaymentViVi);
    }

    /**
     * Partially update a nextPaymentViVi.
     *
     * @param nextPaymentViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentViVi> partialUpdate(NextPaymentViVi nextPaymentViVi) {
        LOG.debug("Request to partially update NextPaymentViVi : {}", nextPaymentViVi);

        return nextPaymentViViRepository
            .findById(nextPaymentViVi.getId())
            .map(existingNextPaymentViVi -> {
                if (nextPaymentViVi.getAmount() != null) {
                    existingNextPaymentViVi.setAmount(nextPaymentViVi.getAmount());
                }
                if (nextPaymentViVi.getPaymentDate() != null) {
                    existingNextPaymentViVi.setPaymentDate(nextPaymentViVi.getPaymentDate());
                }
                if (nextPaymentViVi.getPaymentMethod() != null) {
                    existingNextPaymentViVi.setPaymentMethod(nextPaymentViVi.getPaymentMethod());
                }

                return existingNextPaymentViVi;
            })
            .map(nextPaymentViViRepository::save);
    }

    /**
     * Get one nextPaymentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentViVi> findOne(Long id) {
        LOG.debug("Request to get NextPaymentViVi : {}", id);
        return nextPaymentViViRepository.findById(id);
    }

    /**
     * Delete the nextPaymentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentViVi : {}", id);
        nextPaymentViViRepository.deleteById(id);
    }
}
