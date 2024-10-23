package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
@Service
@Transactional
public class PaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViService.class);

    private final PaymentViRepository paymentViRepository;

    public PaymentViService(PaymentViRepository paymentViRepository) {
        this.paymentViRepository = paymentViRepository;
    }

    /**
     * Save a paymentVi.
     *
     * @param paymentVi the entity to save.
     * @return the persisted entity.
     */
    public PaymentVi save(PaymentVi paymentVi) {
        LOG.debug("Request to save PaymentVi : {}", paymentVi);
        return paymentViRepository.save(paymentVi);
    }

    /**
     * Update a paymentVi.
     *
     * @param paymentVi the entity to save.
     * @return the persisted entity.
     */
    public PaymentVi update(PaymentVi paymentVi) {
        LOG.debug("Request to update PaymentVi : {}", paymentVi);
        return paymentViRepository.save(paymentVi);
    }

    /**
     * Partially update a paymentVi.
     *
     * @param paymentVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentVi> partialUpdate(PaymentVi paymentVi) {
        LOG.debug("Request to partially update PaymentVi : {}", paymentVi);

        return paymentViRepository
            .findById(paymentVi.getId())
            .map(existingPaymentVi -> {
                if (paymentVi.getAmount() != null) {
                    existingPaymentVi.setAmount(paymentVi.getAmount());
                }
                if (paymentVi.getPaymentDate() != null) {
                    existingPaymentVi.setPaymentDate(paymentVi.getPaymentDate());
                }
                if (paymentVi.getPaymentMethod() != null) {
                    existingPaymentVi.setPaymentMethod(paymentVi.getPaymentMethod());
                }

                return existingPaymentVi;
            })
            .map(paymentViRepository::save);
    }

    /**
     * Get one paymentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentVi> findOne(Long id) {
        LOG.debug("Request to get PaymentVi : {}", id);
        return paymentViRepository.findById(id);
    }

    /**
     * Delete the paymentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentVi : {}", id);
        paymentViRepository.deleteById(id);
    }
}
