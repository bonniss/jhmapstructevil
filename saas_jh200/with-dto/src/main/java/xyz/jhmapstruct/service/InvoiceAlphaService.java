package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceAlpha;
import xyz.jhmapstruct.repository.InvoiceAlphaRepository;
import xyz.jhmapstruct.service.dto.InvoiceAlphaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceAlpha}.
 */
@Service
@Transactional
public class InvoiceAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAlphaService.class);

    private final InvoiceAlphaRepository invoiceAlphaRepository;

    private final InvoiceAlphaMapper invoiceAlphaMapper;

    public InvoiceAlphaService(InvoiceAlphaRepository invoiceAlphaRepository, InvoiceAlphaMapper invoiceAlphaMapper) {
        this.invoiceAlphaRepository = invoiceAlphaRepository;
        this.invoiceAlphaMapper = invoiceAlphaMapper;
    }

    /**
     * Save a invoiceAlpha.
     *
     * @param invoiceAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceAlphaDTO save(InvoiceAlphaDTO invoiceAlphaDTO) {
        LOG.debug("Request to save InvoiceAlpha : {}", invoiceAlphaDTO);
        InvoiceAlpha invoiceAlpha = invoiceAlphaMapper.toEntity(invoiceAlphaDTO);
        invoiceAlpha = invoiceAlphaRepository.save(invoiceAlpha);
        return invoiceAlphaMapper.toDto(invoiceAlpha);
    }

    /**
     * Update a invoiceAlpha.
     *
     * @param invoiceAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceAlphaDTO update(InvoiceAlphaDTO invoiceAlphaDTO) {
        LOG.debug("Request to update InvoiceAlpha : {}", invoiceAlphaDTO);
        InvoiceAlpha invoiceAlpha = invoiceAlphaMapper.toEntity(invoiceAlphaDTO);
        invoiceAlpha = invoiceAlphaRepository.save(invoiceAlpha);
        return invoiceAlphaMapper.toDto(invoiceAlpha);
    }

    /**
     * Partially update a invoiceAlpha.
     *
     * @param invoiceAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceAlphaDTO> partialUpdate(InvoiceAlphaDTO invoiceAlphaDTO) {
        LOG.debug("Request to partially update InvoiceAlpha : {}", invoiceAlphaDTO);

        return invoiceAlphaRepository
            .findById(invoiceAlphaDTO.getId())
            .map(existingInvoiceAlpha -> {
                invoiceAlphaMapper.partialUpdate(existingInvoiceAlpha, invoiceAlphaDTO);

                return existingInvoiceAlpha;
            })
            .map(invoiceAlphaRepository::save)
            .map(invoiceAlphaMapper::toDto);
    }

    /**
     * Get one invoiceAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceAlpha : {}", id);
        return invoiceAlphaRepository.findById(id).map(invoiceAlphaMapper::toDto);
    }

    /**
     * Delete the invoiceAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceAlpha : {}", id);
        invoiceAlphaRepository.deleteById(id);
    }
}
