package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceAlpha;
import xyz.jhmapstruct.repository.InvoiceAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceAlpha}.
 */
@Service
@Transactional
public class InvoiceAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAlphaService.class);

    private final InvoiceAlphaRepository invoiceAlphaRepository;

    public InvoiceAlphaService(InvoiceAlphaRepository invoiceAlphaRepository) {
        this.invoiceAlphaRepository = invoiceAlphaRepository;
    }

    /**
     * Save a invoiceAlpha.
     *
     * @param invoiceAlpha the entity to save.
     * @return the persisted entity.
     */
    public InvoiceAlpha save(InvoiceAlpha invoiceAlpha) {
        LOG.debug("Request to save InvoiceAlpha : {}", invoiceAlpha);
        return invoiceAlphaRepository.save(invoiceAlpha);
    }

    /**
     * Update a invoiceAlpha.
     *
     * @param invoiceAlpha the entity to save.
     * @return the persisted entity.
     */
    public InvoiceAlpha update(InvoiceAlpha invoiceAlpha) {
        LOG.debug("Request to update InvoiceAlpha : {}", invoiceAlpha);
        return invoiceAlphaRepository.save(invoiceAlpha);
    }

    /**
     * Partially update a invoiceAlpha.
     *
     * @param invoiceAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceAlpha> partialUpdate(InvoiceAlpha invoiceAlpha) {
        LOG.debug("Request to partially update InvoiceAlpha : {}", invoiceAlpha);

        return invoiceAlphaRepository
            .findById(invoiceAlpha.getId())
            .map(existingInvoiceAlpha -> {
                if (invoiceAlpha.getInvoiceNumber() != null) {
                    existingInvoiceAlpha.setInvoiceNumber(invoiceAlpha.getInvoiceNumber());
                }
                if (invoiceAlpha.getIssueDate() != null) {
                    existingInvoiceAlpha.setIssueDate(invoiceAlpha.getIssueDate());
                }
                if (invoiceAlpha.getDueDate() != null) {
                    existingInvoiceAlpha.setDueDate(invoiceAlpha.getDueDate());
                }
                if (invoiceAlpha.getAmount() != null) {
                    existingInvoiceAlpha.setAmount(invoiceAlpha.getAmount());
                }

                return existingInvoiceAlpha;
            })
            .map(invoiceAlphaRepository::save);
    }

    /**
     * Get one invoiceAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceAlpha> findOne(Long id) {
        LOG.debug("Request to get InvoiceAlpha : {}", id);
        return invoiceAlphaRepository.findById(id);
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
