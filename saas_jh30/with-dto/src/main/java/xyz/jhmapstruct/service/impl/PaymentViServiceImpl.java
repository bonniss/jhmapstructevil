package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.PaymentViService;
import xyz.jhmapstruct.service.dto.PaymentViDTO;
import xyz.jhmapstruct.service.mapper.PaymentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
@Service
@Transactional
public class PaymentViServiceImpl implements PaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViServiceImpl.class);

    private final PaymentViRepository paymentViRepository;

    private final PaymentViMapper paymentViMapper;

    public PaymentViServiceImpl(PaymentViRepository paymentViRepository, PaymentViMapper paymentViMapper) {
        this.paymentViRepository = paymentViRepository;
        this.paymentViMapper = paymentViMapper;
    }

    @Override
    public PaymentViDTO save(PaymentViDTO paymentViDTO) {
        LOG.debug("Request to save PaymentVi : {}", paymentViDTO);
        PaymentVi paymentVi = paymentViMapper.toEntity(paymentViDTO);
        paymentVi = paymentViRepository.save(paymentVi);
        return paymentViMapper.toDto(paymentVi);
    }

    @Override
    public PaymentViDTO update(PaymentViDTO paymentViDTO) {
        LOG.debug("Request to update PaymentVi : {}", paymentViDTO);
        PaymentVi paymentVi = paymentViMapper.toEntity(paymentViDTO);
        paymentVi = paymentViRepository.save(paymentVi);
        return paymentViMapper.toDto(paymentVi);
    }

    @Override
    public Optional<PaymentViDTO> partialUpdate(PaymentViDTO paymentViDTO) {
        LOG.debug("Request to partially update PaymentVi : {}", paymentViDTO);

        return paymentViRepository
            .findById(paymentViDTO.getId())
            .map(existingPaymentVi -> {
                paymentViMapper.partialUpdate(existingPaymentVi, paymentViDTO);

                return existingPaymentVi;
            })
            .map(paymentViRepository::save)
            .map(paymentViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentViDTO> findAll() {
        LOG.debug("Request to get all PaymentVis");
        return paymentViRepository.findAll().stream().map(paymentViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentViDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentVi : {}", id);
        return paymentViRepository.findById(id).map(paymentViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentVi : {}", id);
        paymentViRepository.deleteById(id);
    }
}
