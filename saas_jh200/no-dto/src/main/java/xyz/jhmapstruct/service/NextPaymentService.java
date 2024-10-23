package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.repository.NextPaymentRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPayment}.
 */
@Service
@Transactional
public class NextPaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentService.class);

    private final NextPaymentRepository nextPaymentRepository;

    public NextPaymentService(NextPaymentRepository nextPaymentRepository) {
        this.nextPaymentRepository = nextPaymentRepository;
    }

    /**
     * Save a nextPayment.
     *
     * @param nextPayment the entity to save.
     * @return the persisted entity.
     */
    public NextPayment save(NextPayment nextPayment) {
        LOG.debug("Request to save NextPayment : {}", nextPayment);
        return nextPaymentRepository.save(nextPayment);
    }

    /**
     * Update a nextPayment.
     *
     * @param nextPayment the entity to save.
     * @return the persisted entity.
     */
    public NextPayment update(NextPayment nextPayment) {
        LOG.debug("Request to update NextPayment : {}", nextPayment);
        return nextPaymentRepository.save(nextPayment);
    }

    /**
     * Partially update a nextPayment.
     *
     * @param nextPayment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPayment> partialUpdate(NextPayment nextPayment) {
        LOG.debug("Request to partially update NextPayment : {}", nextPayment);

        return nextPaymentRepository
            .findById(nextPayment.getId())
            .map(existingNextPayment -> {
                if (nextPayment.getAmount() != null) {
                    existingNextPayment.setAmount(nextPayment.getAmount());
                }
                if (nextPayment.getPaymentDate() != null) {
                    existingNextPayment.setPaymentDate(nextPayment.getPaymentDate());
                }
                if (nextPayment.getPaymentMethod() != null) {
                    existingNextPayment.setPaymentMethod(nextPayment.getPaymentMethod());
                }

                return existingNextPayment;
            })
            .map(nextPaymentRepository::save);
    }

    /**
     * Get one nextPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPayment> findOne(Long id) {
        LOG.debug("Request to get NextPayment : {}", id);
        return nextPaymentRepository.findById(id);
    }

    /**
     * Delete the nextPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPayment : {}", id);
        nextPaymentRepository.deleteById(id);
    }
}
