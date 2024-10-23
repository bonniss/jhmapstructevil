package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.repository.PaymentMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
@Service
@Transactional
public class PaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiMiService.class);

    private final PaymentMiMiRepository paymentMiMiRepository;

    public PaymentMiMiService(PaymentMiMiRepository paymentMiMiRepository) {
        this.paymentMiMiRepository = paymentMiMiRepository;
    }

    /**
     * Save a paymentMiMi.
     *
     * @param paymentMiMi the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiMi save(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to save PaymentMiMi : {}", paymentMiMi);
        return paymentMiMiRepository.save(paymentMiMi);
    }

    /**
     * Update a paymentMiMi.
     *
     * @param paymentMiMi the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiMi update(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to update PaymentMiMi : {}", paymentMiMi);
        return paymentMiMiRepository.save(paymentMiMi);
    }

    /**
     * Partially update a paymentMiMi.
     *
     * @param paymentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMiMi> partialUpdate(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to partially update PaymentMiMi : {}", paymentMiMi);

        return paymentMiMiRepository
            .findById(paymentMiMi.getId())
            .map(existingPaymentMiMi -> {
                if (paymentMiMi.getAmount() != null) {
                    existingPaymentMiMi.setAmount(paymentMiMi.getAmount());
                }
                if (paymentMiMi.getPaymentDate() != null) {
                    existingPaymentMiMi.setPaymentDate(paymentMiMi.getPaymentDate());
                }
                if (paymentMiMi.getPaymentMethod() != null) {
                    existingPaymentMiMi.setPaymentMethod(paymentMiMi.getPaymentMethod());
                }

                return existingPaymentMiMi;
            })
            .map(paymentMiMiRepository::save);
    }

    /**
     * Get one paymentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMiMi> findOne(Long id) {
        LOG.debug("Request to get PaymentMiMi : {}", id);
        return paymentMiMiRepository.findById(id);
    }

    /**
     * Delete the paymentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMiMi : {}", id);
        paymentMiMiRepository.deleteById(id);
    }
}
