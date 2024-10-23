package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceTheta;
import xyz.jhmapstruct.repository.InvoiceThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceTheta}.
 */
@Service
@Transactional
public class InvoiceThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceThetaService.class);

    private final InvoiceThetaRepository invoiceThetaRepository;

    public InvoiceThetaService(InvoiceThetaRepository invoiceThetaRepository) {
        this.invoiceThetaRepository = invoiceThetaRepository;
    }

    /**
     * Save a invoiceTheta.
     *
     * @param invoiceTheta the entity to save.
     * @return the persisted entity.
     */
    public InvoiceTheta save(InvoiceTheta invoiceTheta) {
        LOG.debug("Request to save InvoiceTheta : {}", invoiceTheta);
        return invoiceThetaRepository.save(invoiceTheta);
    }

    /**
     * Update a invoiceTheta.
     *
     * @param invoiceTheta the entity to save.
     * @return the persisted entity.
     */
    public InvoiceTheta update(InvoiceTheta invoiceTheta) {
        LOG.debug("Request to update InvoiceTheta : {}", invoiceTheta);
        return invoiceThetaRepository.save(invoiceTheta);
    }

    /**
     * Partially update a invoiceTheta.
     *
     * @param invoiceTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceTheta> partialUpdate(InvoiceTheta invoiceTheta) {
        LOG.debug("Request to partially update InvoiceTheta : {}", invoiceTheta);

        return invoiceThetaRepository
            .findById(invoiceTheta.getId())
            .map(existingInvoiceTheta -> {
                if (invoiceTheta.getInvoiceNumber() != null) {
                    existingInvoiceTheta.setInvoiceNumber(invoiceTheta.getInvoiceNumber());
                }
                if (invoiceTheta.getIssueDate() != null) {
                    existingInvoiceTheta.setIssueDate(invoiceTheta.getIssueDate());
                }
                if (invoiceTheta.getDueDate() != null) {
                    existingInvoiceTheta.setDueDate(invoiceTheta.getDueDate());
                }
                if (invoiceTheta.getAmount() != null) {
                    existingInvoiceTheta.setAmount(invoiceTheta.getAmount());
                }

                return existingInvoiceTheta;
            })
            .map(invoiceThetaRepository::save);
    }

    /**
     * Get one invoiceTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceTheta> findOne(Long id) {
        LOG.debug("Request to get InvoiceTheta : {}", id);
        return invoiceThetaRepository.findById(id);
    }

    /**
     * Delete the invoiceTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceTheta : {}", id);
        invoiceThetaRepository.deleteById(id);
    }
}
