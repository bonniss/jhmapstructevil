package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.repository.PaymentMiMiRepository;
import xyz.jhmapstruct.service.PaymentMiMiService;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
@Service
@Transactional
public class PaymentMiMiServiceImpl implements PaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiMiServiceImpl.class);

    private final PaymentMiMiRepository paymentMiMiRepository;

    private final PaymentMiMiMapper paymentMiMiMapper;

    public PaymentMiMiServiceImpl(PaymentMiMiRepository paymentMiMiRepository, PaymentMiMiMapper paymentMiMiMapper) {
        this.paymentMiMiRepository = paymentMiMiRepository;
        this.paymentMiMiMapper = paymentMiMiMapper;
    }

    @Override
    public PaymentMiMiDTO save(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to save PaymentMiMi : {}", paymentMiMiDTO);
        PaymentMiMi paymentMiMi = paymentMiMiMapper.toEntity(paymentMiMiDTO);
        paymentMiMi = paymentMiMiRepository.save(paymentMiMi);
        return paymentMiMiMapper.toDto(paymentMiMi);
    }

    @Override
    public PaymentMiMiDTO update(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to update PaymentMiMi : {}", paymentMiMiDTO);
        PaymentMiMi paymentMiMi = paymentMiMiMapper.toEntity(paymentMiMiDTO);
        paymentMiMi = paymentMiMiRepository.save(paymentMiMi);
        return paymentMiMiMapper.toDto(paymentMiMi);
    }

    @Override
    public Optional<PaymentMiMiDTO> partialUpdate(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to partially update PaymentMiMi : {}", paymentMiMiDTO);

        return paymentMiMiRepository
            .findById(paymentMiMiDTO.getId())
            .map(existingPaymentMiMi -> {
                paymentMiMiMapper.partialUpdate(existingPaymentMiMi, paymentMiMiDTO);

                return existingPaymentMiMi;
            })
            .map(paymentMiMiRepository::save)
            .map(paymentMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMiMiDTO> findAll() {
        LOG.debug("Request to get all PaymentMiMis");
        return paymentMiMiRepository.findAll().stream().map(paymentMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentMiMi : {}", id);
        return paymentMiMiRepository.findById(id).map(paymentMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMiMi : {}", id);
        paymentMiMiRepository.deleteById(id);
    }
}
