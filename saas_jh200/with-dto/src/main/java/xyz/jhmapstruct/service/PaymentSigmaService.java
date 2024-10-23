package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.repository.PaymentSigmaRepository;
import xyz.jhmapstruct.service.dto.PaymentSigmaDTO;
import xyz.jhmapstruct.service.mapper.PaymentSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentSigma}.
 */
@Service
@Transactional
public class PaymentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSigmaService.class);

    private final PaymentSigmaRepository paymentSigmaRepository;

    private final PaymentSigmaMapper paymentSigmaMapper;

    public PaymentSigmaService(PaymentSigmaRepository paymentSigmaRepository, PaymentSigmaMapper paymentSigmaMapper) {
        this.paymentSigmaRepository = paymentSigmaRepository;
        this.paymentSigmaMapper = paymentSigmaMapper;
    }

    /**
     * Save a paymentSigma.
     *
     * @param paymentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentSigmaDTO save(PaymentSigmaDTO paymentSigmaDTO) {
        LOG.debug("Request to save PaymentSigma : {}", paymentSigmaDTO);
        PaymentSigma paymentSigma = paymentSigmaMapper.toEntity(paymentSigmaDTO);
        paymentSigma = paymentSigmaRepository.save(paymentSigma);
        return paymentSigmaMapper.toDto(paymentSigma);
    }

    /**
     * Update a paymentSigma.
     *
     * @param paymentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentSigmaDTO update(PaymentSigmaDTO paymentSigmaDTO) {
        LOG.debug("Request to update PaymentSigma : {}", paymentSigmaDTO);
        PaymentSigma paymentSigma = paymentSigmaMapper.toEntity(paymentSigmaDTO);
        paymentSigma = paymentSigmaRepository.save(paymentSigma);
        return paymentSigmaMapper.toDto(paymentSigma);
    }

    /**
     * Partially update a paymentSigma.
     *
     * @param paymentSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentSigmaDTO> partialUpdate(PaymentSigmaDTO paymentSigmaDTO) {
        LOG.debug("Request to partially update PaymentSigma : {}", paymentSigmaDTO);

        return paymentSigmaRepository
            .findById(paymentSigmaDTO.getId())
            .map(existingPaymentSigma -> {
                paymentSigmaMapper.partialUpdate(existingPaymentSigma, paymentSigmaDTO);

                return existingPaymentSigma;
            })
            .map(paymentSigmaRepository::save)
            .map(paymentSigmaMapper::toDto);
    }

    /**
     * Get one paymentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentSigma : {}", id);
        return paymentSigmaRepository.findById(id).map(paymentSigmaMapper::toDto);
    }

    /**
     * Delete the paymentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentSigma : {}", id);
        paymentSigmaRepository.deleteById(id);
    }
}
