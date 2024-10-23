package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.repository.PaymentAlphaRepository;
import xyz.jhmapstruct.service.dto.PaymentAlphaDTO;
import xyz.jhmapstruct.service.mapper.PaymentAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentAlpha}.
 */
@Service
@Transactional
public class PaymentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAlphaService.class);

    private final PaymentAlphaRepository paymentAlphaRepository;

    private final PaymentAlphaMapper paymentAlphaMapper;

    public PaymentAlphaService(PaymentAlphaRepository paymentAlphaRepository, PaymentAlphaMapper paymentAlphaMapper) {
        this.paymentAlphaRepository = paymentAlphaRepository;
        this.paymentAlphaMapper = paymentAlphaMapper;
    }

    /**
     * Save a paymentAlpha.
     *
     * @param paymentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentAlphaDTO save(PaymentAlphaDTO paymentAlphaDTO) {
        LOG.debug("Request to save PaymentAlpha : {}", paymentAlphaDTO);
        PaymentAlpha paymentAlpha = paymentAlphaMapper.toEntity(paymentAlphaDTO);
        paymentAlpha = paymentAlphaRepository.save(paymentAlpha);
        return paymentAlphaMapper.toDto(paymentAlpha);
    }

    /**
     * Update a paymentAlpha.
     *
     * @param paymentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentAlphaDTO update(PaymentAlphaDTO paymentAlphaDTO) {
        LOG.debug("Request to update PaymentAlpha : {}", paymentAlphaDTO);
        PaymentAlpha paymentAlpha = paymentAlphaMapper.toEntity(paymentAlphaDTO);
        paymentAlpha = paymentAlphaRepository.save(paymentAlpha);
        return paymentAlphaMapper.toDto(paymentAlpha);
    }

    /**
     * Partially update a paymentAlpha.
     *
     * @param paymentAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentAlphaDTO> partialUpdate(PaymentAlphaDTO paymentAlphaDTO) {
        LOG.debug("Request to partially update PaymentAlpha : {}", paymentAlphaDTO);

        return paymentAlphaRepository
            .findById(paymentAlphaDTO.getId())
            .map(existingPaymentAlpha -> {
                paymentAlphaMapper.partialUpdate(existingPaymentAlpha, paymentAlphaDTO);

                return existingPaymentAlpha;
            })
            .map(paymentAlphaRepository::save)
            .map(paymentAlphaMapper::toDto);
    }

    /**
     * Get one paymentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentAlpha : {}", id);
        return paymentAlphaRepository.findById(id).map(paymentAlphaMapper::toDto);
    }

    /**
     * Delete the paymentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentAlpha : {}", id);
        paymentAlphaRepository.deleteById(id);
    }
}
