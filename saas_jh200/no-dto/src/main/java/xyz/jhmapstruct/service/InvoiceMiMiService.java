package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
@Service
@Transactional
public class InvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiService.class);

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    public InvoiceMiMiService(InvoiceMiMiRepository invoiceMiMiRepository) {
        this.invoiceMiMiRepository = invoiceMiMiRepository;
    }

    /**
     * Save a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiMi save(InvoiceMiMi invoiceMiMi) {
        LOG.debug("Request to save InvoiceMiMi : {}", invoiceMiMi);
        return invoiceMiMiRepository.save(invoiceMiMi);
    }

    /**
     * Update a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiMi update(InvoiceMiMi invoiceMiMi) {
        LOG.debug("Request to update InvoiceMiMi : {}", invoiceMiMi);
        return invoiceMiMiRepository.save(invoiceMiMi);
    }

    /**
     * Partially update a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceMiMi> partialUpdate(InvoiceMiMi invoiceMiMi) {
        LOG.debug("Request to partially update InvoiceMiMi : {}", invoiceMiMi);

        return invoiceMiMiRepository
            .findById(invoiceMiMi.getId())
            .map(existingInvoiceMiMi -> {
                if (invoiceMiMi.getInvoiceNumber() != null) {
                    existingInvoiceMiMi.setInvoiceNumber(invoiceMiMi.getInvoiceNumber());
                }
                if (invoiceMiMi.getIssueDate() != null) {
                    existingInvoiceMiMi.setIssueDate(invoiceMiMi.getIssueDate());
                }
                if (invoiceMiMi.getDueDate() != null) {
                    existingInvoiceMiMi.setDueDate(invoiceMiMi.getDueDate());
                }
                if (invoiceMiMi.getAmount() != null) {
                    existingInvoiceMiMi.setAmount(invoiceMiMi.getAmount());
                }

                return existingInvoiceMiMi;
            })
            .map(invoiceMiMiRepository::save);
    }

    /**
     * Get one invoiceMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceMiMi> findOne(Long id) {
        LOG.debug("Request to get InvoiceMiMi : {}", id);
        return invoiceMiMiRepository.findById(id);
    }

    /**
     * Delete the invoiceMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMiMi : {}", id);
        invoiceMiMiRepository.deleteById(id);
    }
}
