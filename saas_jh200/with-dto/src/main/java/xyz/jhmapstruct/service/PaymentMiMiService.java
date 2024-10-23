package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.repository.PaymentMiMiRepository;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
@Service
@Transactional
public class PaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiMiService.class);

    private final PaymentMiMiRepository paymentMiMiRepository;

    private final PaymentMiMiMapper paymentMiMiMapper;

    public PaymentMiMiService(PaymentMiMiRepository paymentMiMiRepository, PaymentMiMiMapper paymentMiMiMapper) {
        this.paymentMiMiRepository = paymentMiMiRepository;
        this.paymentMiMiMapper = paymentMiMiMapper;
    }

    /**
     * Save a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiMiDTO save(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to save PaymentMiMi : {}", paymentMiMiDTO);
        PaymentMiMi paymentMiMi = paymentMiMiMapper.toEntity(paymentMiMiDTO);
        paymentMiMi = paymentMiMiRepository.save(paymentMiMi);
        return paymentMiMiMapper.toDto(paymentMiMi);
    }

    /**
     * Update a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentMiMiDTO update(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to update PaymentMiMi : {}", paymentMiMiDTO);
        PaymentMiMi paymentMiMi = paymentMiMiMapper.toEntity(paymentMiMiDTO);
        paymentMiMi = paymentMiMiRepository.save(paymentMiMi);
        return paymentMiMiMapper.toDto(paymentMiMi);
    }

    /**
     * Partially update a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMiMiDTO> partialUpdate(PaymentMiMiDTO paymentMiMiDTO) {
        LOG.debug("Request to partially update PaymentMiMi : {}", paymentMiMiDTO);

        return paymentMiMiRepository
            .findById(paymentMiMiDTO.getId())
            .map(existingPaymentMiMi -> {
                paymentMiMiMapper.partialUpdate(existingPaymentMiMi, paymentMiMiDTO);

                return existingPaymentMiMi;
            })
            .map(paymentMiMiRepository::save)
            .map(paymentMiMiMapper::toDto);
    }

    /**
     * Get one paymentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentMiMi : {}", id);
        return paymentMiMiRepository.findById(id).map(paymentMiMiMapper::toDto);
    }

    /**
     * Delete the paymentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentMiMi : {}", id);
        paymentMiMiRepository.deleteById(id);
    }
}
