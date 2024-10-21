package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.InvoiceMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
@Service
@Transactional
public class InvoiceMiServiceImpl implements InvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiServiceImpl.class);

    private final InvoiceMiRepository invoiceMiRepository;

    public InvoiceMiServiceImpl(InvoiceMiRepository invoiceMiRepository) {
        this.invoiceMiRepository = invoiceMiRepository;
    }

    @Override
    public InvoiceMi save(InvoiceMi invoiceMi) {
        LOG.debug("Request to save InvoiceMi : {}", invoiceMi);
        return invoiceMiRepository.save(invoiceMi);
    }

    @Override
    public InvoiceMi update(InvoiceMi invoiceMi) {
        LOG.debug("Request to update InvoiceMi : {}", invoiceMi);
        return invoiceMiRepository.save(invoiceMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMi> findAll() {
        LOG.debug("Request to get all InvoiceMis");
        return invoiceMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceMi> findOne(Long id) {
        LOG.debug("Request to get InvoiceMi : {}", id);
        return invoiceMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMi : {}", id);
        invoiceMiRepository.deleteById(id);
    }
}
