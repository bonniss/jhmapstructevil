package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.PaymentViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
@Service
@Transactional
public class PaymentViServiceImpl implements PaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViServiceImpl.class);

    private final PaymentViRepository paymentViRepository;

    public PaymentViServiceImpl(PaymentViRepository paymentViRepository) {
        this.paymentViRepository = paymentViRepository;
    }

    @Override
    public PaymentVi save(PaymentVi paymentVi) {
        LOG.debug("Request to save PaymentVi : {}", paymentVi);
        return paymentViRepository.save(paymentVi);
    }

    @Override
    public PaymentVi update(PaymentVi paymentVi) {
        LOG.debug("Request to update PaymentVi : {}", paymentVi);
        return paymentViRepository.save(paymentVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<PaymentVi> findAll() {
        LOG.debug("Request to get all PaymentVis");
        return paymentViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentVi> findOne(Long id) {
        LOG.debug("Request to get PaymentVi : {}", id);
        return paymentViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentVi : {}", id);
        paymentViRepository.deleteById(id);
    }
}
