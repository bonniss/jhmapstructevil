package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.repository.PaymentMiMiRepository;
import xyz.jhmapstruct.service.PaymentMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
@Service
@Transactional
public class PaymentMiMiServiceImpl implements PaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiMiServiceImpl.class);

    private final PaymentMiMiRepository paymentMiMiRepository;

    public PaymentMiMiServiceImpl(PaymentMiMiRepository paymentMiMiRepository) {
        this.paymentMiMiRepository = paymentMiMiRepository;
    }

    @Override
    public PaymentMiMi save(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to save PaymentMiMi : {}", paymentMiMi);
        return paymentMiMiRepository.save(paymentMiMi);
    }

    @Override
    public PaymentMiMi update(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to update PaymentMiMi : {}", paymentMiMi);
        return paymentMiMiRepository.save(paymentMiMi);
    }

    @Override
    public Optional<PaymentMiMi> partialUpdate(PaymentMiMi paymentMiMi) {
        LOG.debug("Request to partially update PaymentMiMi : {}", paymentMiMi);

        return paymentMiMiRepository
            .findById(paymentMiMi.getId())
            .map(existingPaymentMiMi -> {
                if (paymentMiMi.getAmount() != null) {
                    existingPaymentMiMi.setAmount(paymentMiMi.getAmount());
                }
                if (paymentMiMi.getPaymentDate() != null) {
                    existingPaymentMiMi.setPaymentDate(paymentMiMi.getPaymentDate());
                }
                if (paymentMiMi.getPaymentMethod() != null) {
                    existingPaymentMiMi.setPaymentMethod(paymentMiMi.getPaymentMethod());
                }

                return existingPaymentMiMi;
            })
            .map(paymentMiMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMiMi> findAll() {
        LOG.debug("Request to get all PaymentMiMis");
        return paymentMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMiMi> findOne(Long id) {
        LOG.debug("Request to get PaymentMiMi : {}", id);
        return paymentMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMiMi : {}", id);
        paymentMiMiRepository.deleteById(id);
    }
}
