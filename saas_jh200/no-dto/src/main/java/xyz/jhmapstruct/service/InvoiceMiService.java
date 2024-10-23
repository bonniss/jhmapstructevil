package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
@Service
@Transactional
public class InvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiService.class);

    private final InvoiceMiRepository invoiceMiRepository;

    public InvoiceMiService(InvoiceMiRepository invoiceMiRepository) {
        this.invoiceMiRepository = invoiceMiRepository;
    }

    /**
     * Save a invoiceMi.
     *
     * @param invoiceMi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMi save(InvoiceMi invoiceMi) {
        LOG.debug("Request to save InvoiceMi : {}", invoiceMi);
        return invoiceMiRepository.save(invoiceMi);
    }

    /**
     * Update a invoiceMi.
     *
     * @param invoiceMi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMi update(InvoiceMi invoiceMi) {
        LOG.debug("Request to update InvoiceMi : {}", invoiceMi);
        return invoiceMiRepository.save(invoiceMi);
    }

    /**
     * Partially update a invoiceMi.
     *
     * @param invoiceMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceMi> partialUpdate(InvoiceMi invoiceMi) {
        LOG.debug("Request to partially update InvoiceMi : {}", invoiceMi);

        return invoiceMiRepository
            .findById(invoiceMi.getId())
            .map(existingInvoiceMi -> {
                if (invoiceMi.getInvoiceNumber() != null) {
                    existingInvoiceMi.setInvoiceNumber(invoiceMi.getInvoiceNumber());
                }
                if (invoiceMi.getIssueDate() != null) {
                    existingInvoiceMi.setIssueDate(invoiceMi.getIssueDate());
                }
                if (invoiceMi.getDueDate() != null) {
                    existingInvoiceMi.setDueDate(invoiceMi.getDueDate());
                }
                if (invoiceMi.getAmount() != null) {
                    existingInvoiceMi.setAmount(invoiceMi.getAmount());
                }

                return existingInvoiceMi;
            })
            .map(invoiceMiRepository::save);
    }

    /**
     * Get one invoiceMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceMi> findOne(Long id) {
        LOG.debug("Request to get InvoiceMi : {}", id);
        return invoiceMiRepository.findById(id);
    }

    /**
     * Delete the invoiceMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMi : {}", id);
        invoiceMiRepository.deleteById(id);
    }
}
