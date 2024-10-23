package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.repository.PaymentGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentGamma}.
 */
@Service
@Transactional
public class PaymentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGammaService.class);

    private final PaymentGammaRepository paymentGammaRepository;

    public PaymentGammaService(PaymentGammaRepository paymentGammaRepository) {
        this.paymentGammaRepository = paymentGammaRepository;
    }

    /**
     * Save a paymentGamma.
     *
     * @param paymentGamma the entity to save.
     * @return the persisted entity.
     */
    public PaymentGamma save(PaymentGamma paymentGamma) {
        LOG.debug("Request to save PaymentGamma : {}", paymentGamma);
        return paymentGammaRepository.save(paymentGamma);
    }

    /**
     * Update a paymentGamma.
     *
     * @param paymentGamma the entity to save.
     * @return the persisted entity.
     */
    public PaymentGamma update(PaymentGamma paymentGamma) {
        LOG.debug("Request to update PaymentGamma : {}", paymentGamma);
        return paymentGammaRepository.save(paymentGamma);
    }

    /**
     * Partially update a paymentGamma.
     *
     * @param paymentGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentGamma> partialUpdate(PaymentGamma paymentGamma) {
        LOG.debug("Request to partially update PaymentGamma : {}", paymentGamma);

        return paymentGammaRepository
            .findById(paymentGamma.getId())
            .map(existingPaymentGamma -> {
                if (paymentGamma.getAmount() != null) {
                    existingPaymentGamma.setAmount(paymentGamma.getAmount());
                }
                if (paymentGamma.getPaymentDate() != null) {
                    existingPaymentGamma.setPaymentDate(paymentGamma.getPaymentDate());
                }
                if (paymentGamma.getPaymentMethod() != null) {
                    existingPaymentGamma.setPaymentMethod(paymentGamma.getPaymentMethod());
                }

                return existingPaymentGamma;
            })
            .map(paymentGammaRepository::save);
    }

    /**
     * Get one paymentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentGamma> findOne(Long id) {
        LOG.debug("Request to get PaymentGamma : {}", id);
        return paymentGammaRepository.findById(id);
    }

    /**
     * Delete the paymentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentGamma : {}", id);
        paymentGammaRepository.deleteById(id);
    }
}
