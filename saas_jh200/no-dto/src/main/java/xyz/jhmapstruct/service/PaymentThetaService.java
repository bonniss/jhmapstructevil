package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.repository.PaymentThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentTheta}.
 */
@Service
@Transactional
public class PaymentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentThetaService.class);

    private final PaymentThetaRepository paymentThetaRepository;

    public PaymentThetaService(PaymentThetaRepository paymentThetaRepository) {
        this.paymentThetaRepository = paymentThetaRepository;
    }

    /**
     * Save a paymentTheta.
     *
     * @param paymentTheta the entity to save.
     * @return the persisted entity.
     */
    public PaymentTheta save(PaymentTheta paymentTheta) {
        LOG.debug("Request to save PaymentTheta : {}", paymentTheta);
        return paymentThetaRepository.save(paymentTheta);
    }

    /**
     * Update a paymentTheta.
     *
     * @param paymentTheta the entity to save.
     * @return the persisted entity.
     */
    public PaymentTheta update(PaymentTheta paymentTheta) {
        LOG.debug("Request to update PaymentTheta : {}", paymentTheta);
        return paymentThetaRepository.save(paymentTheta);
    }

    /**
     * Partially update a paymentTheta.
     *
     * @param paymentTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentTheta> partialUpdate(PaymentTheta paymentTheta) {
        LOG.debug("Request to partially update PaymentTheta : {}", paymentTheta);

        return paymentThetaRepository
            .findById(paymentTheta.getId())
            .map(existingPaymentTheta -> {
                if (paymentTheta.getAmount() != null) {
                    existingPaymentTheta.setAmount(paymentTheta.getAmount());
                }
                if (paymentTheta.getPaymentDate() != null) {
                    existingPaymentTheta.setPaymentDate(paymentTheta.getPaymentDate());
                }
                if (paymentTheta.getPaymentMethod() != null) {
                    existingPaymentTheta.setPaymentMethod(paymentTheta.getPaymentMethod());
                }

                return existingPaymentTheta;
            })
            .map(paymentThetaRepository::save);
    }

    /**
     * Get one paymentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentTheta> findOne(Long id) {
        LOG.debug("Request to get PaymentTheta : {}", id);
        return paymentThetaRepository.findById(id);
    }

    /**
     * Delete the paymentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentTheta : {}", id);
        paymentThetaRepository.deleteById(id);
    }
}
