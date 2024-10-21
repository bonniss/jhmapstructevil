package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.PaymentViViService;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;
import xyz.jhmapstruct.service.mapper.PaymentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
@Service
@Transactional
public class PaymentViViServiceImpl implements PaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViServiceImpl.class);

    private final PaymentViViRepository paymentViViRepository;

    private final PaymentViViMapper paymentViViMapper;

    public PaymentViViServiceImpl(PaymentViViRepository paymentViViRepository, PaymentViViMapper paymentViViMapper) {
        this.paymentViViRepository = paymentViViRepository;
        this.paymentViViMapper = paymentViViMapper;
    }

    @Override
    public PaymentViViDTO save(PaymentViViDTO paymentViViDTO) {
        LOG.debug("Request to save PaymentViVi : {}", paymentViViDTO);
        PaymentViVi paymentViVi = paymentViViMapper.toEntity(paymentViViDTO);
        paymentViVi = paymentViViRepository.save(paymentViVi);
        return paymentViViMapper.toDto(paymentViVi);
    }

    @Override
    public PaymentViViDTO update(PaymentViViDTO paymentViViDTO) {
        LOG.debug("Request to update PaymentViVi : {}", paymentViViDTO);
        PaymentViVi paymentViVi = paymentViViMapper.toEntity(paymentViViDTO);
        paymentViVi = paymentViViRepository.save(paymentViVi);
        return paymentViViMapper.toDto(paymentViVi);
    }

    @Override
    public Optional<PaymentViViDTO> partialUpdate(PaymentViViDTO paymentViViDTO) {
        LOG.debug("Request to partially update PaymentViVi : {}", paymentViViDTO);

        return paymentViViRepository
            .findById(paymentViViDTO.getId())
            .map(existingPaymentViVi -> {
                paymentViViMapper.partialUpdate(existingPaymentViVi, paymentViViDTO);

                return existingPaymentViVi;
            })
            .map(paymentViViRepository::save)
            .map(paymentViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentViViDTO> findAll() {
        LOG.debug("Request to get all PaymentViVis");
        return paymentViViRepository.findAll().stream().map(paymentViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentViViDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentViVi : {}", id);
        return paymentViViRepository.findById(id).map(paymentViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentViVi : {}", id);
        paymentViViRepository.deleteById(id);
    }
}
