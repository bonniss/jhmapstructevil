package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.repository.PaymentGammaRepository;
import xyz.jhmapstruct.service.dto.PaymentGammaDTO;
import xyz.jhmapstruct.service.mapper.PaymentGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentGamma}.
 */
@Service
@Transactional
public class PaymentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGammaService.class);

    private final PaymentGammaRepository paymentGammaRepository;

    private final PaymentGammaMapper paymentGammaMapper;

    public PaymentGammaService(PaymentGammaRepository paymentGammaRepository, PaymentGammaMapper paymentGammaMapper) {
        this.paymentGammaRepository = paymentGammaRepository;
        this.paymentGammaMapper = paymentGammaMapper;
    }

    /**
     * Save a paymentGamma.
     *
     * @param paymentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentGammaDTO save(PaymentGammaDTO paymentGammaDTO) {
        LOG.debug("Request to save PaymentGamma : {}", paymentGammaDTO);
        PaymentGamma paymentGamma = paymentGammaMapper.toEntity(paymentGammaDTO);
        paymentGamma = paymentGammaRepository.save(paymentGamma);
        return paymentGammaMapper.toDto(paymentGamma);
    }

    /**
     * Update a paymentGamma.
     *
     * @param paymentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentGammaDTO update(PaymentGammaDTO paymentGammaDTO) {
        LOG.debug("Request to update PaymentGamma : {}", paymentGammaDTO);
        PaymentGamma paymentGamma = paymentGammaMapper.toEntity(paymentGammaDTO);
        paymentGamma = paymentGammaRepository.save(paymentGamma);
        return paymentGammaMapper.toDto(paymentGamma);
    }

    /**
     * Partially update a paymentGamma.
     *
     * @param paymentGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentGammaDTO> partialUpdate(PaymentGammaDTO paymentGammaDTO) {
        LOG.debug("Request to partially update PaymentGamma : {}", paymentGammaDTO);

        return paymentGammaRepository
            .findById(paymentGammaDTO.getId())
            .map(existingPaymentGamma -> {
                paymentGammaMapper.partialUpdate(existingPaymentGamma, paymentGammaDTO);

                return existingPaymentGamma;
            })
            .map(paymentGammaRepository::save)
            .map(paymentGammaMapper::toDto);
    }

    /**
     * Get one paymentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentGammaDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentGamma : {}", id);
        return paymentGammaRepository.findById(id).map(paymentGammaMapper::toDto);
    }

    /**
     * Delete the paymentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentGamma : {}", id);
        paymentGammaRepository.deleteById(id);
    }
}
