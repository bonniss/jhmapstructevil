package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.dto.PaymentViDTO;
import xyz.jhmapstruct.service.mapper.PaymentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
@Service
@Transactional
public class PaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViService.class);

    private final PaymentViRepository paymentViRepository;

    private final PaymentViMapper paymentViMapper;

    public PaymentViService(PaymentViRepository paymentViRepository, PaymentViMapper paymentViMapper) {
        this.paymentViRepository = paymentViRepository;
        this.paymentViMapper = paymentViMapper;
    }

    /**
     * Save a paymentVi.
     *
     * @param paymentViDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentViDTO save(PaymentViDTO paymentViDTO) {
        LOG.debug("Request to save PaymentVi : {}", paymentViDTO);
        PaymentVi paymentVi = paymentViMapper.toEntity(paymentViDTO);
        paymentVi = paymentViRepository.save(paymentVi);
        return paymentViMapper.toDto(paymentVi);
    }

    /**
     * Update a paymentVi.
     *
     * @param paymentViDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentViDTO update(PaymentViDTO paymentViDTO) {
        LOG.debug("Request to update PaymentVi : {}", paymentViDTO);
        PaymentVi paymentVi = paymentViMapper.toEntity(paymentViDTO);
        paymentVi = paymentViRepository.save(paymentVi);
        return paymentViMapper.toDto(paymentVi);
    }

    /**
     * Partially update a paymentVi.
     *
     * @param paymentViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one paymentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentViDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentVi : {}", id);
        return paymentViRepository.findById(id).map(paymentViMapper::toDto);
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
