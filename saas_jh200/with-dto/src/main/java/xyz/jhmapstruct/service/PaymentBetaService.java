package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.repository.PaymentBetaRepository;
import xyz.jhmapstruct.service.dto.PaymentBetaDTO;
import xyz.jhmapstruct.service.mapper.PaymentBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentBeta}.
 */
@Service
@Transactional
public class PaymentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentBetaService.class);

    private final PaymentBetaRepository paymentBetaRepository;

    private final PaymentBetaMapper paymentBetaMapper;

    public PaymentBetaService(PaymentBetaRepository paymentBetaRepository, PaymentBetaMapper paymentBetaMapper) {
        this.paymentBetaRepository = paymentBetaRepository;
        this.paymentBetaMapper = paymentBetaMapper;
    }

    /**
     * Save a paymentBeta.
     *
     * @param paymentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentBetaDTO save(PaymentBetaDTO paymentBetaDTO) {
        LOG.debug("Request to save PaymentBeta : {}", paymentBetaDTO);
        PaymentBeta paymentBeta = paymentBetaMapper.toEntity(paymentBetaDTO);
        paymentBeta = paymentBetaRepository.save(paymentBeta);
        return paymentBetaMapper.toDto(paymentBeta);
    }

    /**
     * Update a paymentBeta.
     *
     * @param paymentBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentBetaDTO update(PaymentBetaDTO paymentBetaDTO) {
        LOG.debug("Request to update PaymentBeta : {}", paymentBetaDTO);
        PaymentBeta paymentBeta = paymentBetaMapper.toEntity(paymentBetaDTO);
        paymentBeta = paymentBetaRepository.save(paymentBeta);
        return paymentBetaMapper.toDto(paymentBeta);
    }

    /**
     * Partially update a paymentBeta.
     *
     * @param paymentBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentBetaDTO> partialUpdate(PaymentBetaDTO paymentBetaDTO) {
        LOG.debug("Request to partially update PaymentBeta : {}", paymentBetaDTO);

        return paymentBetaRepository
            .findById(paymentBetaDTO.getId())
            .map(existingPaymentBeta -> {
                paymentBetaMapper.partialUpdate(existingPaymentBeta, paymentBetaDTO);

                return existingPaymentBeta;
            })
            .map(paymentBetaRepository::save)
            .map(paymentBetaMapper::toDto);
    }

    /**
     * Get one paymentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentBetaDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentBeta : {}", id);
        return paymentBetaRepository.findById(id).map(paymentBetaMapper::toDto);
    }

    /**
     * Delete the paymentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentBeta : {}", id);
        paymentBetaRepository.deleteById(id);
    }
}
