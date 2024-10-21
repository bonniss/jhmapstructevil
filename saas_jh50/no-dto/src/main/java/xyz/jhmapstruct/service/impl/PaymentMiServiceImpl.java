package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.PaymentMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
@Service
@Transactional
public class PaymentMiServiceImpl implements PaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiServiceImpl.class);

    private final PaymentMiRepository paymentMiRepository;

    public PaymentMiServiceImpl(PaymentMiRepository paymentMiRepository) {
        this.paymentMiRepository = paymentMiRepository;
    }

    @Override
    public PaymentMi save(PaymentMi paymentMi) {
        LOG.debug("Request to save PaymentMi : {}", paymentMi);
        return paymentMiRepository.save(paymentMi);
    }

    @Override
    public PaymentMi update(PaymentMi paymentMi) {
        LOG.debug("Request to update PaymentMi : {}", paymentMi);
        return paymentMiRepository.save(paymentMi);
    }

    @Override
    public Optional<PaymentMi> partialUpdate(PaymentMi paymentMi) {
        LOG.debug("Request to partially update PaymentMi : {}", paymentMi);

        return paymentMiRepository
            .findById(paymentMi.getId())
            .map(existingPaymentMi -> {
                if (paymentMi.getAmount() != null) {
                    existingPaymentMi.setAmount(paymentMi.getAmount());
                }
                if (paymentMi.getPaymentDate() != null) {
                    existingPaymentMi.setPaymentDate(paymentMi.getPaymentDate());
                }
                if (paymentMi.getPaymentMethod() != null) {
                    existingPaymentMi.setPaymentMethod(paymentMi.getPaymentMethod());
                }

                return existingPaymentMi;
            })
            .map(paymentMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMi> findAll() {
        LOG.debug("Request to get all PaymentMis");
        return paymentMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMi> findOne(Long id) {
        LOG.debug("Request to get PaymentMi : {}", id);
        return paymentMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMi : {}", id);
        paymentMiRepository.deleteById(id);
    }
}
