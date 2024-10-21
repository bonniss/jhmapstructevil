package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.InvoiceViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
@Service
@Transactional
public class InvoiceViViServiceImpl implements InvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViServiceImpl.class);

    private final InvoiceViViRepository invoiceViViRepository;

    public InvoiceViViServiceImpl(InvoiceViViRepository invoiceViViRepository) {
        this.invoiceViViRepository = invoiceViViRepository;
    }

    @Override
    public InvoiceViVi save(InvoiceViVi invoiceViVi) {
        LOG.debug("Request to save InvoiceViVi : {}", invoiceViVi);
        return invoiceViViRepository.save(invoiceViVi);
    }

    @Override
    public InvoiceViVi update(InvoiceViVi invoiceViVi) {
        LOG.debug("Request to update InvoiceViVi : {}", invoiceViVi);
        return invoiceViViRepository.save(invoiceViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceViVi> findAll() {
        LOG.debug("Request to get all InvoiceViVis");
        return invoiceViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceViVi> findOne(Long id) {
        LOG.debug("Request to get InvoiceViVi : {}", id);
        return invoiceViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceViVi : {}", id);
        invoiceViViRepository.deleteById(id);
    }
}
