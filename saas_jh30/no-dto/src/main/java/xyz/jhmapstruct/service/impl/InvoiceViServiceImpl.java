package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.InvoiceViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
@Service
@Transactional
public class InvoiceViServiceImpl implements InvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViServiceImpl.class);

    private final InvoiceViRepository invoiceViRepository;

    public InvoiceViServiceImpl(InvoiceViRepository invoiceViRepository) {
        this.invoiceViRepository = invoiceViRepository;
    }

    @Override
    public InvoiceVi save(InvoiceVi invoiceVi) {
        LOG.debug("Request to save InvoiceVi : {}", invoiceVi);
        return invoiceViRepository.save(invoiceVi);
    }

    @Override
    public InvoiceVi update(InvoiceVi invoiceVi) {
        LOG.debug("Request to update InvoiceVi : {}", invoiceVi);
        return invoiceViRepository.save(invoiceVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceVi> findAll() {
        LOG.debug("Request to get all InvoiceVis");
        return invoiceViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceVi> findOne(Long id) {
        LOG.debug("Request to get InvoiceVi : {}", id);
        return invoiceViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceVi : {}", id);
        invoiceViRepository.deleteById(id);
    }
}
