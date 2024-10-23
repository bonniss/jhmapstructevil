package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
@Service
@Transactional
public class PaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiService.class);

    private final PaymentMiRepository paymentMiRepository;

    private final PaymentMiMapper paymentMiMapper;

    public PaymentMiService(PaymentMiRepository paymentMiRepository, PaymentMiMapper paymentMiMapper) {
        this.paymentMiRepository = paymentMiRepository;
        this.paymentMiMapper = paymentMiMapper;
    }

    /**
     * Save a paymentMi.
     *
     * @param paymentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiDTO save(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to save PaymentMi : {}", paymentMiDTO);
        PaymentMi paymentMi = paymentMiMapper.toEntity(paymentMiDTO);
        paymentMi = paymentMiRepository.save(paymentMi);
        return paymentMiMapper.toDto(paymentMi);
    }

    /**
     * Update a paymentMi.
     *
     * @param paymentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiDTO update(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to update PaymentMi : {}", paymentMiDTO);
        PaymentMi paymentMi = paymentMiMapper.toEntity(paymentMiDTO);
        paymentMi = paymentMiRepository.save(paymentMi);
        return paymentMiMapper.toDto(paymentMi);
    }

    /**
     * Partially update a paymentMi.
     *
     * @param paymentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMiDTO> partialUpdate(PaymentMiDTO paymentMiDTO) {
        LOG.debug("Request to partially update PaymentMi : {}", paymentMiDTO);

        return paymentMiRepository
            .findById(paymentMiDTO.getId())
            .map(existingPaymentMi -> {
                paymentMiMapper.partialUpdate(existingPaymentMi, paymentMiDTO);

                return existingPaymentMi;
            })
            .map(paymentMiRepository::save)
            .map(paymentMiMapper::toDto);
    }

    /**
     * Get one paymentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMiDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentMi : {}", id);
        return paymentMiRepository.findById(id).map(paymentMiMapper::toDto);
    }

    /**
     * Delete the paymentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMi : {}", id);
        paymentMiRepository.deleteById(id);
    }
}
