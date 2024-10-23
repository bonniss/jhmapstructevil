package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceGamma;
import xyz.jhmapstruct.repository.InvoiceGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceGamma}.
 */
@Service
@Transactional
public class InvoiceGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceGammaService.class);

    private final InvoiceGammaRepository invoiceGammaRepository;

    public InvoiceGammaService(InvoiceGammaRepository invoiceGammaRepository) {
        this.invoiceGammaRepository = invoiceGammaRepository;
    }

    /**
     * Save a invoiceGamma.
     *
     * @param invoiceGamma the entity to save.
     * @return the persisted entity.
     */
    public InvoiceGamma save(InvoiceGamma invoiceGamma) {
        LOG.debug("Request to save InvoiceGamma : {}", invoiceGamma);
        return invoiceGammaRepository.save(invoiceGamma);
    }

    /**
     * Update a invoiceGamma.
     *
     * @param invoiceGamma the entity to save.
     * @return the persisted entity.
     */
    public InvoiceGamma update(InvoiceGamma invoiceGamma) {
        LOG.debug("Request to update InvoiceGamma : {}", invoiceGamma);
        return invoiceGammaRepository.save(invoiceGamma);
    }

    /**
     * Partially update a invoiceGamma.
     *
     * @param invoiceGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceGamma> partialUpdate(InvoiceGamma invoiceGamma) {
        LOG.debug("Request to partially update InvoiceGamma : {}", invoiceGamma);

        return invoiceGammaRepository
            .findById(invoiceGamma.getId())
            .map(existingInvoiceGamma -> {
                if (invoiceGamma.getInvoiceNumber() != null) {
                    existingInvoiceGamma.setInvoiceNumber(invoiceGamma.getInvoiceNumber());
                }
                if (invoiceGamma.getIssueDate() != null) {
                    existingInvoiceGamma.setIssueDate(invoiceGamma.getIssueDate());
                }
                if (invoiceGamma.getDueDate() != null) {
                    existingInvoiceGamma.setDueDate(invoiceGamma.getDueDate());
                }
                if (invoiceGamma.getAmount() != null) {
                    existingInvoiceGamma.setAmount(invoiceGamma.getAmount());
                }

                return existingInvoiceGamma;
            })
            .map(invoiceGammaRepository::save);
    }

    /**
     * Get one invoiceGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceGamma> findOne(Long id) {
        LOG.debug("Request to get InvoiceGamma : {}", id);
        return invoiceGammaRepository.findById(id);
    }

    /**
     * Delete the invoiceGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceGamma : {}", id);
        invoiceGammaRepository.deleteById(id);
    }
}
