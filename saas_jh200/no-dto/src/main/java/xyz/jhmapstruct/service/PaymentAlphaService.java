package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.repository.PaymentAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentAlpha}.
 */
@Service
@Transactional
public class PaymentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAlphaService.class);

    private final PaymentAlphaRepository paymentAlphaRepository;

    public PaymentAlphaService(PaymentAlphaRepository paymentAlphaRepository) {
        this.paymentAlphaRepository = paymentAlphaRepository;
    }

    /**
     * Save a paymentAlpha.
     *
     * @param paymentAlpha the entity to save.
     * @return the persisted entity.
     */
    public PaymentAlpha save(PaymentAlpha paymentAlpha) {
        LOG.debug("Request to save PaymentAlpha : {}", paymentAlpha);
        return paymentAlphaRepository.save(paymentAlpha);
    }

    /**
     * Update a paymentAlpha.
     *
     * @param paymentAlpha the entity to save.
     * @return the persisted entity.
     */
    public PaymentAlpha update(PaymentAlpha paymentAlpha) {
        LOG.debug("Request to update PaymentAlpha : {}", paymentAlpha);
        return paymentAlphaRepository.save(paymentAlpha);
    }

    /**
     * Partially update a paymentAlpha.
     *
     * @param paymentAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentAlpha> partialUpdate(PaymentAlpha paymentAlpha) {
        LOG.debug("Request to partially update PaymentAlpha : {}", paymentAlpha);

        return paymentAlphaRepository
            .findById(paymentAlpha.getId())
            .map(existingPaymentAlpha -> {
                if (paymentAlpha.getAmount() != null) {
                    existingPaymentAlpha.setAmount(paymentAlpha.getAmount());
                }
                if (paymentAlpha.getPaymentDate() != null) {
                    existingPaymentAlpha.setPaymentDate(paymentAlpha.getPaymentDate());
                }
                if (paymentAlpha.getPaymentMethod() != null) {
                    existingPaymentAlpha.setPaymentMethod(paymentAlpha.getPaymentMethod());
                }

                return existingPaymentAlpha;
            })
            .map(paymentAlphaRepository::save);
    }

    /**
     * Get one paymentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentAlpha> findOne(Long id) {
        LOG.debug("Request to get PaymentAlpha : {}", id);
        return paymentAlphaRepository.findById(id);
    }

    /**
     * Delete the paymentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentAlpha : {}", id);
        paymentAlphaRepository.deleteById(id);
    }
}
