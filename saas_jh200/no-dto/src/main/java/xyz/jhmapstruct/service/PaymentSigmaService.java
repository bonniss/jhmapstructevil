package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.repository.PaymentSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentSigma}.
 */
@Service
@Transactional
public class PaymentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSigmaService.class);

    private final PaymentSigmaRepository paymentSigmaRepository;

    public PaymentSigmaService(PaymentSigmaRepository paymentSigmaRepository) {
        this.paymentSigmaRepository = paymentSigmaRepository;
    }

    /**
     * Save a paymentSigma.
     *
     * @param paymentSigma the entity to save.
     * @return the persisted entity.
     */
    public PaymentSigma save(PaymentSigma paymentSigma) {
        LOG.debug("Request to save PaymentSigma : {}", paymentSigma);
        return paymentSigmaRepository.save(paymentSigma);
    }

    /**
     * Update a paymentSigma.
     *
     * @param paymentSigma the entity to save.
     * @return the persisted entity.
     */
    public PaymentSigma update(PaymentSigma paymentSigma) {
        LOG.debug("Request to update PaymentSigma : {}", paymentSigma);
        return paymentSigmaRepository.save(paymentSigma);
    }

    /**
     * Partially update a paymentSigma.
     *
     * @param paymentSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentSigma> partialUpdate(PaymentSigma paymentSigma) {
        LOG.debug("Request to partially update PaymentSigma : {}", paymentSigma);

        return paymentSigmaRepository
            .findById(paymentSigma.getId())
            .map(existingPaymentSigma -> {
                if (paymentSigma.getAmount() != null) {
                    existingPaymentSigma.setAmount(paymentSigma.getAmount());
                }
                if (paymentSigma.getPaymentDate() != null) {
                    existingPaymentSigma.setPaymentDate(paymentSigma.getPaymentDate());
                }
                if (paymentSigma.getPaymentMethod() != null) {
                    existingPaymentSigma.setPaymentMethod(paymentSigma.getPaymentMethod());
                }

                return existingPaymentSigma;
            })
            .map(paymentSigmaRepository::save);
    }

    /**
     * Get one paymentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentSigma> findOne(Long id) {
        LOG.debug("Request to get PaymentSigma : {}", id);
        return paymentSigmaRepository.findById(id);
    }

    /**
     * Delete the paymentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentSigma : {}", id);
        paymentSigmaRepository.deleteById(id);
    }
}
