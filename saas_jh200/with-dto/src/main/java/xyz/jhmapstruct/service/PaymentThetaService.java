package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.repository.PaymentThetaRepository;
import xyz.jhmapstruct.service.dto.PaymentThetaDTO;
import xyz.jhmapstruct.service.mapper.PaymentThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentTheta}.
 */
@Service
@Transactional
public class PaymentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentThetaService.class);

    private final PaymentThetaRepository paymentThetaRepository;

    private final PaymentThetaMapper paymentThetaMapper;

    public PaymentThetaService(PaymentThetaRepository paymentThetaRepository, PaymentThetaMapper paymentThetaMapper) {
        this.paymentThetaRepository = paymentThetaRepository;
        this.paymentThetaMapper = paymentThetaMapper;
    }

    /**
     * Save a paymentTheta.
     *
     * @param paymentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentThetaDTO save(PaymentThetaDTO paymentThetaDTO) {
        LOG.debug("Request to save PaymentTheta : {}", paymentThetaDTO);
        PaymentTheta paymentTheta = paymentThetaMapper.toEntity(paymentThetaDTO);
        paymentTheta = paymentThetaRepository.save(paymentTheta);
        return paymentThetaMapper.toDto(paymentTheta);
    }

    /**
     * Update a paymentTheta.
     *
     * @param paymentThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentThetaDTO update(PaymentThetaDTO paymentThetaDTO) {
        LOG.debug("Request to update PaymentTheta : {}", paymentThetaDTO);
        PaymentTheta paymentTheta = paymentThetaMapper.toEntity(paymentThetaDTO);
        paymentTheta = paymentThetaRepository.save(paymentTheta);
        return paymentThetaMapper.toDto(paymentTheta);
    }

    /**
     * Partially update a paymentTheta.
     *
     * @param paymentThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentThetaDTO> partialUpdate(PaymentThetaDTO paymentThetaDTO) {
        LOG.debug("Request to partially update PaymentTheta : {}", paymentThetaDTO);

        return paymentThetaRepository
            .findById(paymentThetaDTO.getId())
            .map(existingPaymentTheta -> {
                paymentThetaMapper.partialUpdate(existingPaymentTheta, paymentThetaDTO);

                return existingPaymentTheta;
            })
            .map(paymentThetaRepository::save)
            .map(paymentThetaMapper::toDto);
    }

    /**
     * Get one paymentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentThetaDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentTheta : {}", id);
        return paymentThetaRepository.findById(id).map(paymentThetaMapper::toDto);
    }

    /**
     * Delete the paymentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentTheta : {}", id);
        paymentThetaRepository.deleteById(id);
    }
}
