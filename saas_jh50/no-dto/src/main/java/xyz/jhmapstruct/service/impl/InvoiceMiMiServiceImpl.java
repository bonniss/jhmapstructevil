package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.InvoiceMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
@Service
@Transactional
public class InvoiceMiMiServiceImpl implements InvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiServiceImpl.class);

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    public InvoiceMiMiServiceImpl(InvoiceMiMiRepository invoiceMiMiRepository) {
        this.invoiceMiMiRepository = invoiceMiMiRepository;
    }

    @Override
    public InvoiceMiMi save(InvoiceMiMi invoiceMiMi) {
        LOG.debug("Request to save InvoiceMiMi : {}", invoiceMiMi);
        return invoiceMiMiRepository.save(invoiceMiMi);
    }

    @Override
    public InvoiceMiMi update(InvoiceMiMi invoiceMiMi) {
        LOG.debug("Request to update InvoiceMiMi : {}", invoiceMiMi);
        return invoiceMiMiRepository.save(invoiceMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMiMi> findAll() {
        LOG.debug("Request to get all InvoiceMiMis");
        return invoiceMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceMiMi> findOne(Long id) {
        LOG.debug("Request to get InvoiceMiMi : {}", id);
        return invoiceMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMiMi : {}", id);
        invoiceMiMiRepository.deleteById(id);
    }
}
