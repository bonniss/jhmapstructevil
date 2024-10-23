package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
@Service
@Transactional
public class PaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViService.class);

    private final PaymentViViRepository paymentViViRepository;

    public PaymentViViService(PaymentViViRepository paymentViViRepository) {
        this.paymentViViRepository = paymentViViRepository;
    }

    /**
     * Save a paymentViVi.
     *
     * @param paymentViVi the entity to save.
     * @return the persisted entity.
     */
    public PaymentViVi save(PaymentViVi paymentViVi) {
        LOG.debug("Request to save PaymentViVi : {}", paymentViVi);
        return paymentViViRepository.save(paymentViVi);
    }

    /**
     * Update a paymentViVi.
     *
     * @param paymentViVi the entity to save.
     * @return the persisted entity.
     */
    public PaymentViVi update(PaymentViVi paymentViVi) {
        LOG.debug("Request to update PaymentViVi : {}", paymentViVi);
        return paymentViViRepository.save(paymentViVi);
    }

    /**
     * Partially update a paymentViVi.
     *
     * @param paymentViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentViVi> partialUpdate(PaymentViVi paymentViVi) {
        LOG.debug("Request to partially update PaymentViVi : {}", paymentViVi);

        return paymentViViRepository
            .findById(paymentViVi.getId())
            .map(existingPaymentViVi -> {
                if (paymentViVi.getAmount() != null) {
                    existingPaymentViVi.setAmount(paymentViVi.getAmount());
                }
                if (paymentViVi.getPaymentDate() != null) {
                    existingPaymentViVi.setPaymentDate(paymentViVi.getPaymentDate());
                }
                if (paymentViVi.getPaymentMethod() != null) {
                    existingPaymentViVi.setPaymentMethod(paymentViVi.getPaymentMethod());
                }

                return existingPaymentViVi;
            })
            .map(paymentViViRepository::save);
    }

    /**
     * Get one paymentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentViVi> findOne(Long id) {
        LOG.debug("Request to get PaymentViVi : {}", id);
        return paymentViViRepository.findById(id);
    }

    /**
     * Delete the paymentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentViVi : {}", id);
        paymentViViRepository.deleteById(id);
    }
}
