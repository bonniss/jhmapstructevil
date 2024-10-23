package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
@Service
@Transactional
public class PaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiService.class);

    private final PaymentMiRepository paymentMiRepository;

    public PaymentMiService(PaymentMiRepository paymentMiRepository) {
        this.paymentMiRepository = paymentMiRepository;
    }

    /**
     * Save a paymentMi.
     *
     * @param paymentMi the entity to save.
     * @return the persisted entity.
     */
    public PaymentMi save(PaymentMi paymentMi) {
        LOG.debug("Request to save PaymentMi : {}", paymentMi);
        return paymentMiRepository.save(paymentMi);
    }

    /**
     * Update a paymentMi.
     *
     * @param paymentMi the entity to save.
     * @return the persisted entity.
     */
    public PaymentMi update(PaymentMi paymentMi) {
        LOG.debug("Request to update PaymentMi : {}", paymentMi);
        return paymentMiRepository.save(paymentMi);
    }

    /**
     * Partially update a paymentMi.
     *
     * @param paymentMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMi> partialUpdate(PaymentMi paymentMi) {
        LOG.debug("Request to partially update PaymentMi : {}", paymentMi);

        return paymentMiRepository
            .findById(paymentMi.getId())
            .map(existingPaymentMi -> {
                if (paymentMi.getAmount() != null) {
                    existingPaymentMi.setAmount(paymentMi.getAmount());
                }
                if (paymentMi.getPaymentDate() != null) {
                    existingPaymentMi.setPaymentDate(paymentMi.getPaymentDate());
                }
                if (paymentMi.getPaymentMethod() != null) {
                    existingPaymentMi.setPaymentMethod(paymentMi.getPaymentMethod());
                }

                return existingPaymentMi;
            })
            .map(paymentMiRepository::save);
    }

    /**
     * Get one paymentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMi> findOne(Long id) {
        LOG.debug("Request to get PaymentMi : {}", id);
        return paymentMiRepository.findById(id);
    }

    /**
     * Delete the paymentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMi : {}", id);
        paymentMiRepository.deleteById(id);
    }
}
