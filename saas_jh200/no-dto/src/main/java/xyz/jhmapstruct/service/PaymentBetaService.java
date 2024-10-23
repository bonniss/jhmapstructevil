package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.repository.PaymentBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentBeta}.
 */
@Service
@Transactional
public class PaymentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentBetaService.class);

    private final PaymentBetaRepository paymentBetaRepository;

    public PaymentBetaService(PaymentBetaRepository paymentBetaRepository) {
        this.paymentBetaRepository = paymentBetaRepository;
    }

    /**
     * Save a paymentBeta.
     *
     * @param paymentBeta the entity to save.
     * @return the persisted entity.
     */
    public PaymentBeta save(PaymentBeta paymentBeta) {
        LOG.debug("Request to save PaymentBeta : {}", paymentBeta);
        return paymentBetaRepository.save(paymentBeta);
    }

    /**
     * Update a paymentBeta.
     *
     * @param paymentBeta the entity to save.
     * @return the persisted entity.
     */
    public PaymentBeta update(PaymentBeta paymentBeta) {
        LOG.debug("Request to update PaymentBeta : {}", paymentBeta);
        return paymentBetaRepository.save(paymentBeta);
    }

    /**
     * Partially update a paymentBeta.
     *
     * @param paymentBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentBeta> partialUpdate(PaymentBeta paymentBeta) {
        LOG.debug("Request to partially update PaymentBeta : {}", paymentBeta);

        return paymentBetaRepository
            .findById(paymentBeta.getId())
            .map(existingPaymentBeta -> {
                if (paymentBeta.getAmount() != null) {
                    existingPaymentBeta.setAmount(paymentBeta.getAmount());
                }
                if (paymentBeta.getPaymentDate() != null) {
                    existingPaymentBeta.setPaymentDate(paymentBeta.getPaymentDate());
                }
                if (paymentBeta.getPaymentMethod() != null) {
                    existingPaymentBeta.setPaymentMethod(paymentBeta.getPaymentMethod());
                }

                return existingPaymentBeta;
            })
            .map(paymentBetaRepository::save);
    }

    /**
     * Get one paymentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentBeta> findOne(Long id) {
        LOG.debug("Request to get PaymentBeta : {}", id);
        return paymentBetaRepository.findById(id);
    }

    /**
     * Delete the paymentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentBeta : {}", id);
        paymentBetaRepository.deleteById(id);
    }
}
