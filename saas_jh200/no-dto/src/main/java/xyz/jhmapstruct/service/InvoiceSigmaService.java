package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceSigma;
import xyz.jhmapstruct.repository.InvoiceSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceSigma}.
 */
@Service
@Transactional
public class InvoiceSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSigmaService.class);

    private final InvoiceSigmaRepository invoiceSigmaRepository;

    public InvoiceSigmaService(InvoiceSigmaRepository invoiceSigmaRepository) {
        this.invoiceSigmaRepository = invoiceSigmaRepository;
    }

    /**
     * Save a invoiceSigma.
     *
     * @param invoiceSigma the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSigma save(InvoiceSigma invoiceSigma) {
        LOG.debug("Request to save InvoiceSigma : {}", invoiceSigma);
        return invoiceSigmaRepository.save(invoiceSigma);
    }

    /**
     * Update a invoiceSigma.
     *
     * @param invoiceSigma the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSigma update(InvoiceSigma invoiceSigma) {
        LOG.debug("Request to update InvoiceSigma : {}", invoiceSigma);
        return invoiceSigmaRepository.save(invoiceSigma);
    }

    /**
     * Partially update a invoiceSigma.
     *
     * @param invoiceSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceSigma> partialUpdate(InvoiceSigma invoiceSigma) {
        LOG.debug("Request to partially update InvoiceSigma : {}", invoiceSigma);

        return invoiceSigmaRepository
            .findById(invoiceSigma.getId())
            .map(existingInvoiceSigma -> {
                if (invoiceSigma.getInvoiceNumber() != null) {
                    existingInvoiceSigma.setInvoiceNumber(invoiceSigma.getInvoiceNumber());
                }
                if (invoiceSigma.getIssueDate() != null) {
                    existingInvoiceSigma.setIssueDate(invoiceSigma.getIssueDate());
                }
                if (invoiceSigma.getDueDate() != null) {
                    existingInvoiceSigma.setDueDate(invoiceSigma.getDueDate());
                }
                if (invoiceSigma.getAmount() != null) {
                    existingInvoiceSigma.setAmount(invoiceSigma.getAmount());
                }

                return existingInvoiceSigma;
            })
            .map(invoiceSigmaRepository::save);
    }

    /**
     * Get one invoiceSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceSigma> findOne(Long id) {
        LOG.debug("Request to get InvoiceSigma : {}", id);
        return invoiceSigmaRepository.findById(id);
    }

    /**
     * Delete the invoiceSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceSigma : {}", id);
        invoiceSigmaRepository.deleteById(id);
    }
}
