package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.PaymentMiService;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
@Service
@Transactional
public class PaymentMiServiceImpl implements PaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiServiceImpl.class);

    private final PaymentMiRepository paymentMiRepository;

    private final PaymentMiMapper paymentMiMapper;

    public PaymentMiServiceImpl(PaymentMiRepository paymentMiRepository, PaymentMiMapper paymentMiMapper) {
        this.paymentMiRepository = paymentMiRepository;
        this.paymentMiMapper = paymentMiMapper;
    }

    @Override
    public PaymentMiDTO save(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to save PaymentMi : {}", paymentMiDTO);
        PaymentMi paymentMi = paymentMiMapper.toEntity(paymentMiDTO);
        paymentMi = paymentMiRepository.save(paymentMi);
        return paymentMiMapper.toDto(paymentMi);
    }

    @Override
    public PaymentMiDTO update(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to update PaymentMi : {}", paymentMiDTO);
        PaymentMi paymentMi = paymentMiMapper.toEntity(paymentMiDTO);
        paymentMi = paymentMiRepository.save(paymentMi);
        return paymentMiMapper.toDto(paymentMi);
    }

    @Override
    public Optional<PaymentMiDTO> partialUpdate(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to partially update PaymentMi : {}", paymentMiDTO);

        return paymentMiRepository
            .findById(paymentMiDTO.getId())
            .map(existingPaymentMi -> {
                paymentMiMapper.partialUpdate(existingPaymentMi, paymentMiDTO);

                return existingPaymentMi;
            })
            .map(paymentMiRepository::save)
            .map(paymentMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMiDTO> findAll() {
        LOG.debug("Request to get all PaymentMis");
        return paymentMiRepository.findAll().stream().map(paymentMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMiDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentMi : {}", id);
        return paymentMiRepository.findById(id).map(paymentMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMi : {}", id);
        paymentMiRepository.deleteById(id);
    }
}
