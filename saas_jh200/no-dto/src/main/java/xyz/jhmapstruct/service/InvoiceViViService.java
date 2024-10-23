package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
@Service
@Transactional
public class InvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViService.class);

    private final InvoiceViViRepository invoiceViViRepository;

    public InvoiceViViService(InvoiceViViRepository invoiceViViRepository) {
        this.invoiceViViRepository = invoiceViViRepository;
    }

    /**
     * Save a invoiceViVi.
     *
     * @param invoiceViVi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViVi save(InvoiceViVi invoiceViVi) {
        LOG.debug("Request to save InvoiceViVi : {}", invoiceViVi);
        return invoiceViViRepository.save(invoiceViVi);
    }

    /**
     * Update a invoiceViVi.
     *
     * @param invoiceViVi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViVi update(InvoiceViVi invoiceViVi) {
        LOG.debug("Request to update InvoiceViVi : {}", invoiceViVi);
        return invoiceViViRepository.save(invoiceViVi);
    }

    /**
     * Partially update a invoiceViVi.
     *
     * @param invoiceViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceViVi> partialUpdate(InvoiceViVi invoiceViVi) {
        LOG.debug("Request to partially update InvoiceViVi : {}", invoiceViVi);

        return invoiceViViRepository
            .findById(invoiceViVi.getId())
            .map(existingInvoiceViVi -> {
                if (invoiceViVi.getInvoiceNumber() != null) {
                    existingInvoiceViVi.setInvoiceNumber(invoiceViVi.getInvoiceNumber());
                }
                if (invoiceViVi.getIssueDate() != null) {
                    existingInvoiceViVi.setIssueDate(invoiceViVi.getIssueDate());
                }
                if (invoiceViVi.getDueDate() != null) {
                    existingInvoiceViVi.setDueDate(invoiceViVi.getDueDate());
                }
                if (invoiceViVi.getAmount() != null) {
                    existingInvoiceViVi.setAmount(invoiceViVi.getAmount());
                }

                return existingInvoiceViVi;
            })
            .map(invoiceViViRepository::save);
    }

    /**
     * Get one invoiceViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceViVi> findOne(Long id) {
        LOG.debug("Request to get InvoiceViVi : {}", id);
        return invoiceViViRepository.findById(id);
    }

    /**
     * Delete the invoiceViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceViVi : {}", id);
        invoiceViViRepository.deleteById(id);
    }
}
