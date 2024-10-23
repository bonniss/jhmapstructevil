package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
@Service
@Transactional
public class InvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViService.class);

    private final InvoiceViRepository invoiceViRepository;

    public InvoiceViService(InvoiceViRepository invoiceViRepository) {
        this.invoiceViRepository = invoiceViRepository;
    }

    /**
     * Save a invoiceVi.
     *
     * @param invoiceVi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceVi save(InvoiceVi invoiceVi) {
        LOG.debug("Request to save InvoiceVi : {}", invoiceVi);
        return invoiceViRepository.save(invoiceVi);
    }

    /**
     * Update a invoiceVi.
     *
     * @param invoiceVi the entity to save.
     * @return the persisted entity.
     */
    public InvoiceVi update(InvoiceVi invoiceVi) {
        LOG.debug("Request to update InvoiceVi : {}", invoiceVi);
        return invoiceViRepository.save(invoiceVi);
    }

    /**
     * Partially update a invoiceVi.
     *
     * @param invoiceVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceVi> partialUpdate(InvoiceVi invoiceVi) {
        LOG.debug("Request to partially update InvoiceVi : {}", invoiceVi);

        return invoiceViRepository
            .findById(invoiceVi.getId())
            .map(existingInvoiceVi -> {
                if (invoiceVi.getInvoiceNumber() != null) {
                    existingInvoiceVi.setInvoiceNumber(invoiceVi.getInvoiceNumber());
                }
                if (invoiceVi.getIssueDate() != null) {
                    existingInvoiceVi.setIssueDate(invoiceVi.getIssueDate());
                }
                if (invoiceVi.getDueDate() != null) {
                    existingInvoiceVi.setDueDate(invoiceVi.getDueDate());
                }
                if (invoiceVi.getAmount() != null) {
                    existingInvoiceVi.setAmount(invoiceVi.getAmount());
                }

                return existingInvoiceVi;
            })
            .map(invoiceViRepository::save);
    }

    /**
     * Get one invoiceVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceVi> findOne(Long id) {
        LOG.debug("Request to get InvoiceVi : {}", id);
        return invoiceViRepository.findById(id);
    }

    /**
     * Delete the invoiceVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceVi : {}", id);
        invoiceViRepository.deleteById(id);
    }
}
