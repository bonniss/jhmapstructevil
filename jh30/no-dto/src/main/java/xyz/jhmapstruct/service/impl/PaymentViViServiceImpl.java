package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.PaymentViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
@Service
@Transactional
public class PaymentViViServiceImpl implements PaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViServiceImpl.class);

    private final PaymentViViRepository paymentViViRepository;

    public PaymentViViServiceImpl(PaymentViViRepository paymentViViRepository) {
        this.paymentViViRepository = paymentViViRepository;
    }

    @Override
    public PaymentViVi save(PaymentViVi paymentViVi) {
        LOG.debug("Request to save PaymentViVi : {}", paymentViVi);
        return paymentViViRepository.save(paymentViVi);
    }

    @Override
    public PaymentViVi update(PaymentViVi paymentViVi) {
        LOG.debug("Request to update PaymentViVi : {}", paymentViVi);
        return paymentViViRepository.save(paymentViVi);
    }

    @Override
    public Optional<PaymentViVi> partialUpdate(PaymentViVi paymentViVi) {
        LOG.debug("Request to partially update PaymentViVi : {}", paymentViVi);

        return paymentViViRepository
            .findById(paymentViVi.getId())
            .map(existingPaymentViVi -> {
                if (paymentViVi.getAmount() != null) {
                    existingPaymentViVi.setAmount(paymentViVi.getAmount());
                }
                if (paymentViVi.getPaymentDate() != null) {
                    existingPaymentViVi.setPaymentDate(paymentViVi.getPaymentDate());
                }
                if (paymentViVi.getPaymentMethod() != null) {
                    existingPaymentViVi.setPaymentMethod(paymentViVi.getPaymentMethod());
                }

                return existingPaymentViVi;
            })
            .map(paymentViViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentViVi> findAll() {
        LOG.debug("Request to get all PaymentViVis");
        return paymentViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentViVi> findOne(Long id) {
        LOG.debug("Request to get PaymentViVi : {}", id);
        return paymentViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentViVi : {}", id);
        paymentViViRepository.deleteById(id);
    }
}
