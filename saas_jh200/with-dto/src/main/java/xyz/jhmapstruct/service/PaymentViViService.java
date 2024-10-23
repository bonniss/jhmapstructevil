package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;
import xyz.jhmapstruct.service.mapper.PaymentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
@Service
@Transactional
public class PaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViService.class);

    private final PaymentViViRepository paymentViViRepository;

    private final PaymentViViMapper paymentViViMapper;

    public PaymentViViService(PaymentViViRepository paymentViViRepository, PaymentViViMapper paymentViViMapper) {
        this.paymentViViRepository = paymentViViRepository;
        this.paymentViViMapper = paymentViViMapper;
    }

    /**
     * Save a paymentViVi.
     *
     * @param paymentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentViViDTO save(PaymentViViDTO paymentViViDTO) {
        LOG.debug("Request to save PaymentViVi : {}", paymentViViDTO);
        PaymentViVi paymentViVi = paymentViViMapper.toEntity(paymentViViDTO);
        paymentViVi = paymentViViRepository.save(paymentViVi);
        return paymentViViMapper.toDto(paymentViVi);
    }

    /**
     * Update a paymentViVi.
     *
     * @param paymentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentViViDTO update(PaymentViViDTO paymentViViDTO) {
        LOG.debug("Request to update PaymentViVi : {}", paymentViViDTO);
        PaymentViVi paymentViVi = paymentViViMapper.toEntity(paymentViViDTO);
        paymentViVi = paymentViViRepository.save(paymentViVi);
        return paymentViViMapper.toDto(paymentViVi);
    }

    /**
     * Partially update a paymentViVi.
     *
     * @param paymentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one paymentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentViViDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentViVi : {}", id);
        return paymentViViRepository.findById(id).map(paymentViViMapper::toDto);
    }

    /**
     * Delete the paymentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentViVi : {}", id);
        paymentViViRepository.deleteById(id);
    }
}
