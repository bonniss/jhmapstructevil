package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Payment;
import xyz.jhmapstruct.repository.PaymentRepository;
import xyz.jhmapstruct.service.PaymentService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        LOG.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        LOG.debug("Request to update Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> partialUpdate(Payment payment) {
        LOG.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getAmount() != null) {
                    existingPayment.setAmount(payment.getAmount());
                }
                if (payment.getPaymentDate() != null) {
                    existingPayment.setPaymentDate(payment.getPaymentDate());
                }
                if (payment.getPaymentMethod() != null) {
                    existingPayment.setPaymentMethod(payment.getPaymentMethod());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        LOG.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
