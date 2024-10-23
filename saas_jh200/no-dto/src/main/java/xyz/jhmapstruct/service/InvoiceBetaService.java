package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceBeta;
import xyz.jhmapstruct.repository.InvoiceBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceBeta}.
 */
@Service
@Transactional
public class InvoiceBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBetaService.class);

    private final InvoiceBetaRepository invoiceBetaRepository;

    public InvoiceBetaService(InvoiceBetaRepository invoiceBetaRepository) {
        this.invoiceBetaRepository = invoiceBetaRepository;
    }

    /**
     * Save a invoiceBeta.
     *
     * @param invoiceBeta the entity to save.
     * @return the persisted entity.
     */
    public InvoiceBeta save(InvoiceBeta invoiceBeta) {
        LOG.debug("Request to save InvoiceBeta : {}", invoiceBeta);
        return invoiceBetaRepository.save(invoiceBeta);
    }

    /**
     * Update a invoiceBeta.
     *
     * @param invoiceBeta the entity to save.
     * @return the persisted entity.
     */
    public InvoiceBeta update(InvoiceBeta invoiceBeta) {
        LOG.debug("Request to update InvoiceBeta : {}", invoiceBeta);
        return invoiceBetaRepository.save(invoiceBeta);
    }

    /**
     * Partially update a invoiceBeta.
     *
     * @param invoiceBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceBeta> partialUpdate(InvoiceBeta invoiceBeta) {
        LOG.debug("Request to partially update InvoiceBeta : {}", invoiceBeta);

        return invoiceBetaRepository
            .findById(invoiceBeta.getId())
            .map(existingInvoiceBeta -> {
                if (invoiceBeta.getInvoiceNumber() != null) {
                    existingInvoiceBeta.setInvoiceNumber(invoiceBeta.getInvoiceNumber());
                }
                if (invoiceBeta.getIssueDate() != null) {
                    existingInvoiceBeta.setIssueDate(invoiceBeta.getIssueDate());
                }
                if (invoiceBeta.getDueDate() != null) {
                    existingInvoiceBeta.setDueDate(invoiceBeta.getDueDate());
                }
                if (invoiceBeta.getAmount() != null) {
                    existingInvoiceBeta.setAmount(invoiceBeta.getAmount());
                }

                return existingInvoiceBeta;
            })
            .map(invoiceBetaRepository::save);
    }

    /**
     * Get one invoiceBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceBeta> findOne(Long id) {
        LOG.debug("Request to get InvoiceBeta : {}", id);
        return invoiceBetaRepository.findById(id);
    }

    /**
     * Delete the invoiceBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceBeta : {}", id);
        invoiceBetaRepository.deleteById(id);
    }
}
